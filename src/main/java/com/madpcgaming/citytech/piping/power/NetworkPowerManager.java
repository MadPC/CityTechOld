package com.madpcgaming.citytech.piping.power;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.World;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

import com.madpcgaming.citytech.lib.CityTechConfig;
import com.madpcgaming.citytech.machine.power.TileTeslaBat;
import com.madpcgaming.citytech.piping.power.PowerPipingNetwork.ReceptorEntry;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.power.PowerInterfaceRF;
import com.madpcgaming.citytech.util.BlockCoord;

public class NetworkPowerManager
{
	private PowerPipingNetwork network;
	
	int maxEnergyStored;
	float energyStored;
	private float reserved;
	private int updateRenderTicks = 10;
	private int inactiveTicks = 100;
	private final List<ReceptorEntry> receptors = new ArrayList<PowerPipingNetwork.ReceptorEntry>();
	private ListIterator<ReceptorEntry> receptorIterator = receptors.listIterator();
	private final List<ReceptorEntry> storageRecptors = new ArrayList<ReceptorEntry>();
	private boolean lastActiveValue = false;
	private int ticksWithNoPower = 0;
	private final Map<BlockCoord, StarveBuffer> starveBuffers = new HashMap<BlockCoord, NetworkPowerManager.StarveBuffer>();
	private final Map<IPowerPiping, PowerTracker> powerTrackers = new HashMap<IPowerPiping, PowerTracker>();
	private PowerTracker networkPowerTracker = new PowerTracker();
	private final TeslaBatSupply batSupply = new TeslaBatSupply();
	
	public NetworkPowerManager(PowerPipingNetwork network, World world)
	{
		this.network = network;
		maxEnergyStored = 64;
	}
	
	public PowerTracker getTracker(IPowerPiping piping)
	{
		return powerTrackers.get(piping);
	}
	
	public PowerTracker getNetworkPowerTracker()
	{
		return networkPowerTracker;
	}
	
	public float getPowerInPiping()
	{
		return energyStored;
	}
	
	public float getMaxPowerInPiping()
	{
		return maxEnergyStored;
	}
	
	public float getPowerInTeslaBanks()
	{
		if(batSupply == null)
		{
			return 0;
		}
		
		return batSupply.stored;
	}
	
	public float getMaxPowerInTeslaBanks()
	{
		if(batSupply == null)
		{
			return 0;
		}
		return batSupply.maxCap;
	}
	
	public float getPowerInReceptors()
	{
		float result = 0;
		Set<IPowerInterface> done = new HashSet<IPowerInterface>();
		for(ReceptorEntry re : receptors)
		{
			IPowerInterface powerReceptor = re.powerInterface;
			if(!done.contains(powerReceptor))
			{
				done.add(powerReceptor);
				result += powerReceptor.getEnergyStored(re.direction);
			}
		}
		return result;
	}
	
	public float getMaxPowerInReceptors()
	{
		float result = 0;
		Set<IPowerInterface> done = new HashSet<IPowerInterface>();
		for(ReceptorEntry re : receptors)
		{
			IPowerInterface powerReceptor = re.powerInterface;
			if(!done.contains(powerReceptor))
			{
				done.add(powerReceptor);
				result += powerReceptor.getMaxEnergyStored(re.direction);
			}
		}
		return result;
	}
	
	public void applyreceivedPower()
	{
		trackerStartTick();
		updateNetworkStorage();
		networkPowerTracker.tickStart(energyStored);
		checkReserves();
		updateActiveState();
		batSupply.init();
		int appliedCount = 0;
		int numReceptors = receptors.size();
		float available = energyStored + batSupply.canExtract;
		float wasAvailable = available;
		
		if(available <= 0 || (receptors.isEmpty() && storageRecptors.isEmpty()))
		{
			trackerEndTick();
			return;
		}
		while (available > 0 && appliedCount < numReceptors)
		{
			if(!receptors.isEmpty() && !receptorIterator.hasNext())
			{
				receptorIterator = receptors.listIterator();
			}
			
			ReceptorEntry r = receptorIterator.next();
			if(r.emitter.getPowerHandler() != null && r.emitter.getPowerHandler().isPowerSource(r.direction))
			{
				float es = r.emitter.getPowerHandler().getEnergyStored();
				PowerReceiver pr = r.emitter.getPowerReceiver(r.direction.getOpposite());
				pr.receiveEnergy(Type.STORAGE, 0, null);
				r.emitter.getPowerHandler().setEnergy(es);
			} else {
				IPowerInterface pp = r.powerInterface;
				if(pp != null)
				{
					float used = 0;
					
					if(pp.getClass() == PowerInterfaceRF.class)
					{
						used = pp.receiveEnergy(r.direction.getOpposite(), available);
						trackerSend(r.emitter, used, false);
					} else {
						float reservedForEntry = removeReservedEnergy(r);
						available += reservedForEntry;
						float canOffer = Math.min(r.emitter.getMaxEnergyExtracted(r.direction), available);
						float requested = pp.getPowerRequest(r.direction.getOpposite());
						
						if(pp.getMinEnergyReceived(r.direction) <= r.emitter.getMaxEnergyReceived(r.direction) && requested > 0)
						{
							if(pp.getMinEnergyReceived(r.direction) > canOffer)
							{
								reserveEnergy(r, canOffer);
								used += canOffer;
							} else {
								used = pp.receiveEnergy(r.direction.getOpposite(), canOffer);
								trackerSend(r.emitter, used, false);
							}
						}
					}
					available -= used;
					if(available <= 0)
					{
						break;
					}
				}
			}
			appliedCount++;
		}
		
		float used = wasAvailable - available;
		energyStored -= used;
		
		if(!batSupply.teslaBat.isEmpty())
		{
			float batBankChange = 0;
			if(energyStored < 0)
			{
				batBankChange = energyStored;
				energyStored = 0;
			} 
			else if (energyStored > 0)
			{
				batBankChange = Math.min(energyStored, batSupply.canFill);
				energyStored -= batBankChange;
			}
			
			if(batBankChange < 0)
			{
				batSupply.remove(Math.abs(batBankChange));
			}
			else if(batBankChange > 0)
			{
				batSupply.add(batBankChange);
			}
			
			batSupply.balance();
		}
		
		distributeStorageToPiping();
		
		trackerEndTick();
		
		networkPowerTracker.tickEnd(energyStored);
	}
	
	private void trackerStartTick()
	{
		if(CityTechConfig.detailedPowerTrackingEnabled)
		{
			return;
		}
		for(IPowerPiping pipe : network.getPiping())
		{
			if(pipe.hasExternalConnections())
			{
				PowerTracker tracker = getOrCreateTracker(pipe);
				tracker.tickStart(pipe.getPowerHandler().getEnergyStored());
			}
		}
	}
	
	private void trackerSend(IPowerPiping pipe, float sent, boolean fromBank)
	{
		if(!fromBank)
		{
			networkPowerTracker.powerSent(sent);
		}
		if(!CityTechConfig.detailedPowerTrackingEnabled)
		{
			return;
		}
		getOrCreateTracker(pipe).powerSent(sent);
	}
	
	private void trackerreceive(IPowerPiping pipe, float received, boolean fromBank)
	{
		if(!fromBank)
		{
			networkPowerTracker.powerreceived(received);
		}
		if(!CityTechConfig.detailedPowerTrackingEnabled)
		{
			return;
		}
		getOrCreateTracker(pipe).powerreceived(received);
	}
	
	private void trackerEndTick()
	{
		if(!CityTechConfig.detailedPowerTrackingEnabled)
		{
			return;
		}
		for(IPowerPiping pipe : network.getPiping())
		{
			if(pipe.hasExternalConnections())
			{
				PowerTracker tracker = getOrCreateTracker(pipe);
				tracker.tickEnd(pipe.getPowerHandler().getEnergyStored());
			}
		}
	}
	
	private PowerTracker getOrCreateTracker(IPowerPiping pipe)
	{
		PowerTracker result = powerTrackers.get(pipe);
		if(result == null)
		{
			result = new PowerTracker();
			powerTrackers.put(pipe, result);
		}
		return result;
	}
	
	private void updateActiveState()
	{
		boolean active;
		if(energyStored > 0)
		{
			ticksWithNoPower = 0;
			active = true;
		} else {
			ticksWithNoPower++;
			active = false;
		}
		
		boolean doRender = active != lastActiveValue && (active || (!active && ticksWithNoPower > updateRenderTicks));
		if(doRender)
		{
			lastActiveValue = active;
		}
	}
	
	private float removeReservedEnergy(ReceptorEntry r)
	{
		StarveBuffer starveBuf = starveBuffers.remove(r.coord);
		if(starveBuf == null)
		{
			return 0;
		}
		float result = starveBuf.stored;
		reserved -= result;
		return result;
	}
	
	private void reserveEnergy(ReceptorEntry r, float amount)
	{
		starveBuffers.put(r.coord, new StarveBuffer(amount));
		reserved += amount;
	}
	
	private void checkReserves()
	{
		if(reserved > maxEnergyStored * 0.9)
		{
			starveBuffers.clear();
			reserved = 0;
		}
	}
	
	private void distributeStorageToPiping()
	{
		if(maxEnergyStored <= 0 || energyStored <= 0)
		{
			for(IPowerPiping pipe : network.getPiping())
			{
				pipe.setEnergyStored(0);
			}
			return;
		}
		if(energyStored > maxEnergyStored)
		{
			energyStored = maxEnergyStored;
		}
		
		float filledRatio = energyStored / maxEnergyStored;
		float energyLeft = energyStored;
		float given = 0;
		for(IPowerPiping pipe : network.getPiping())
		{
			if(energyLeft >= 0)
			{
				float give = (float) Math.ceil(pipe.getTeslaBat().getMaxEnergyStored()* filledRatio);
				give = Math.min(give, pipe.getTeslaBat().getMaxEnergyStored());
				give = Math.min(give, energyLeft);
				pipe.setEnergyStored(give);
				given += give;
				energyLeft -= give;
			} else {
				pipe.setEnergyStored(0);
			}
		}
	}
	
	boolean isActive()
	{
		return energyStored > 0;
	}
	
	private void updateNetworkStorage()
	{
		maxEnergyStored = 0;
		energyStored = 0;
		for(IPowerPiping pipe : network.getPiping())
		{
			maxEnergyStored += pipe.getTeslaBat().getMaxEnergyStored();
			pipe.onTick();
			energyStored += pipe.getEnergyStored();
		}
	}
	
	public void receptorsChanged()
	{
		receptors.clear();
		storageRecptors.clear();
		for(ReceptorEntry rec: network.getPowerReceptors())
		{
			if(rec.powerInterface.getDelegate() instanceof TileTeslaBat)
			{
				storageRecptors.add(rec);
			} else {
				receptors.add(rec);
			}
		}
		receptorIterator = receptors.listIterator();
	}
	
	void onNetworkDestroyed()
	{
	}
	
	private static class StarveBuffer
	{
		float stored;
		
		public StarveBuffer(float stored)
		{
			this.stored = stored;
		}
		
		void addToStore(float val)
		{
			stored += val;
		}
	}
	
	private float minAbs(float amount, float limit)
	{
		if(amount < 0)
		{
			return Math.max(amount, -limit);
		} else {
			return Math.min(amount, limit);
		}
	}
	
	private class TeslaBatSupply
	{
		float canExtract;
		float canFill;
		Set<TileTeslaBat> teslaBat = new HashSet<TileTeslaBat>();
		float filledRatio;
		float stored = 0;
		float maxCap = 0;
		List<TeslaBatSupplyEntry> enteries = new ArrayList<NetworkPowerManager.TeslaBatSupplyEntry>();
		
		TeslaBatSupply()
		{
		}
		
		void init()
		{
			teslaBat.clear();
			enteries.clear();
			canExtract = 0;
			canFill = 0;
			stored = 0;
			maxCap = 0;
			for(ReceptorEntry rec : storageRecptors)
			{
				TileTeslaBat tb = (TileTeslaBat) rec.powerInterface.getDelegate();
				boolean processed = teslaBat.contains(tb);
				if(!processed)
				{
					stored += tb.getEnergyStored();
					maxCap += tb.getMaxEnergyStored();
					teslaBat.add(tb);
					
					float canGet = 0;
					
					if(tb.isOutputEnabled(rec.direction.getOpposite()))
					{
						canGet = Math.min(tb.getEnergyStored(), tb.getMaxOutput());
						canGet = Math.min(canGet, rec.emitter.getMaxEnergyReceived(rec.direction));
						canExtract += canGet;
					}
					float canFill = 0;
					if(tb.isInputEnabled(rec.direction.getOpposite()))
					{
						canFill = Math.min(tb.getMaxEnergyStored() - tb.getEnergyStored(), tb.getMaxInput());
						canFill = Math.min(canFill, rec.emitter.getMaxEnergyExtracted(rec.direction));
						this.canFill += canFill;
					}
					enteries.add(new TeslaBatSupplyEntry(tb, canGet, canFill, rec.emitter));
				}
			}
			
			filledRatio = 0;
			if(maxCap > 0)
			{
				filledRatio = stored / maxCap;
			}
		}
		
		void balance()
		{
			if(enteries.size() < 2)
			{
				return;
			}
			init();
			int canRemove = 0;
			int canAdd = 0;
			for(TeslaBatSupplyEntry entry : enteries)
			{
				entry.calcToBalance(filledRatio);
				if(entry.toBalance < 0)
				{
					canRemove += -entry.toBalance;
				} else {
					canAdd += entry.toBalance;
				}
			}
			
			float totalTransferAmount = Math.min(canAdd, canRemove);
			
			for(int i = 0; i < enteries.size() && totalTransferAmount > 0; i ++)
			{
				TeslaBatSupplyEntry from = enteries.get(i);
				float amount = from.toBalance;
				amount = minAbs(amount, totalTransferAmount);
				from.teslaBat.addEnergy(amount);
				totalTransferAmount -= Math.abs(amount);
				float toTransfer = Math.abs(amount);
				
				for(int j = i + 1; j < enteries.size() && toTransfer > 0; j++)
				{
					TeslaBatSupplyEntry to = enteries.get(j);
					if(Math.signum(amount) != Math.signum(to.toBalance))
					{
						float toAmount = Math.min(toTransfer, Math.abs(to.toBalance));
						to.teslaBat.addEnergy(toAmount * Math.signum(to.toBalance));
						toTransfer -= toAmount;
					}
				}
			}
		}
		
		void remove(float amount)
		{
			if(canExtract <= 0 || amount <= 0)
			{
				return;
			}
			float ratio = amount / canExtract;
			
			for(TeslaBatSupplyEntry entry : enteries)
			{
				double use = Math.ceil(ratio * entry.canExtract);
				use = Math.min(use, amount);
				use = Math.min(use, entry.canExtract);
				entry.teslaBat.addEnergy(-(float) use);
				trackerreceive(entry.emitter, (float) use, true);
				amount -= use;
				if(amount == 0)
				{
					return;
				}
			}
		}
		
		void add(float amount)
		{
			if(canFill <= 0 || amount <= 0)
			{
				return;
			}
			float ratio = amount /canFill;
			
			for(TeslaBatSupplyEntry entry : enteries)
			{
				double add = (int) Math.ceil(ratio * entry.canFill);
				add = Math.min(add, entry.canFill);
				add = Math.min(add, amount);
				entry.teslaBat.addEnergy((float) add);
				trackerSend(entry.emitter, (float) add, true);
				amount -= add;
				if(amount == 0)
				{
					return;
				}
			}
		}
	}
	
	private static class TeslaBatSupplyEntry
	{
		final TileTeslaBat teslaBat;
		final float canExtract;
		final float canFill;
		float toBalance;
		IPowerPiping emitter;
		
		private TeslaBatSupplyEntry(TileTeslaBat teslaBat, float available, float canFill, IPowerPiping emitter)
		{
			this.teslaBat = teslaBat;
			this.canExtract = available;
			this.canFill = canFill;
		}
		
		void calcToBalance(float targetRatio)
		{
			float targetAmount = teslaBat.getMaxEnergyStored() * targetRatio;
			toBalance = targetAmount - teslaBat.getEnergyStored();
			if(toBalance < 0)
			{
				toBalance = Math.max(toBalance, -canExtract);
			} else {
				toBalance = Math.min(toBalance, canFill);
			}
		}
	}
}
