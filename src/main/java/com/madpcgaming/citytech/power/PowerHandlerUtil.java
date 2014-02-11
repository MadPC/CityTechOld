package com.madpcgaming.citytech.power;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PerditionCalculator;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.IPipeTile.PipeType;
import cofh.api.energy.IEnergyHandler;

public class PowerHandlerUtil
{
	public static IPowerInterface create(Object o)
	{
		if(o instanceof IEnergyHandler)
		{
			return new PowerInterfaceRF((IEnergyHandler) o);			
		} 
		else  if (o instanceof IPowerReceptor)
		{
			return new PowerInterfaceBC((IPowerReceptor) o);
		}
		return null;
	}
	
	public static boolean canConnectReceivePower(IPowerReceptor rec)
	{
		if(rec == null)
		{
			return false;
		}
		if(rec instanceof IPipeTile)
		{
			IPipeTile pipeTile = (IPipeTile) rec;
			return pipeTile.getPipeType() == PipeType.POWER;
		}
		return true;
	}
	
	public static float getStoredEnergyForItem(ItemStack item)
	{
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null)
		{
			return 0;
		}
		return tag.getFloat("storedEnergy");
	}
	
	public static void setStoredEnergyForItem(ItemStack item, float storedEnergy)
	{
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		tag.setFloat("storedEnergy", storedEnergy);
		item.setTagCompound(tag);
	}
	
	public static PowerHandler createHandler(ITeslaBat bat, IPowerReceptor pr, Type type)
	{
		PowerHandler ph = new PowerHandler(pr, type);
		ph.configure(bat.getMinEnergyReceived(), bat.getMaxEnergyReceived(), bat.getMinActivationEnergy(), bat.getMaxEnergyStored());
		ph.configurePowerPerdition(0, 0);
		ph.setPerdition(new NullPerditionCalculator());
		return ph;
	}
	
	public static void configure(PowerHandler ph, ITeslaBat bat)
	{
		ph.configure(bat.getMinEnergyReceived(), bat.getMaxEnergyReceived(), bat.getMinActivationEnergy(), bat.getMaxEnergyStored());
		if(ph.getEnergyStored() > ph.getMaxEnergyStored())
		{
			ph.setEnergy(ph.getMaxEnergyStored());
		}
		ph.configurePowerPerdition(0, 0);
		ph.setPerdition(new NullPerditionCalculator());
	}
	
	public static float transmitInternal(IInternalPowerReceptor receptor, PowerReceiver pp, float quantity, Type type, ForgeDirection from)
	{
		PowerHandler ph = receptor.getPowerHandler();
		if(ph == null)
		{
			return 0;
		}
		
		float energyStored = pp.getEnergyStored();
		float canUse = quantity;
		canUse = Math.min(canUse, pp.getMaxEnergyReceived());
		canUse = Math.min(canUse, pp.getMaxEnergyStored() - energyStored);
		if(canUse <= pp.getMinEnergyReceived())
		{
			pp.receiveEnergy(type, 0, null);
			ph.setEnergy(energyStored);
			return 0;
		}
		
		pp.receiveEnergy(type, 0, from);
		ph.setEnergy(energyStored);
		ph.setEnergy(ph.getEnergyStored() + canUse);
		receptor.applyPerdition();
		return canUse;
	}
	
	public static int receiveRedstoneFlux(ForgeDirection from, PowerHandler powerHandler, int maxReceive, boolean simulate)
	{
		return receiveRedstoneFlux(from, powerHandler, maxReceive, simulate, false);
	}
	
	public static int receiveRedstoneFlux(ForgeDirection from, PowerHandler powerHandler, int maxReceive, boolean simulate, boolean tickPH)
	{
		int canReceive = (int) getMaxEnergyReceivedMj(powerHandler, maxReceive /10, from);
		if(!simulate)
		{
			if(tickPH)
			{
				doBuildCraftEnergyTick(powerHandler, from);
			}
			powerHandler.setEnergy(powerHandler.getEnergyStored() + canReceive);
		}
		return canReceive * 10;
	}
	
	public static float getMaxEnergyReceivedMj(PowerHandler ph, float max, ForgeDirection from)
	{
		if(ph == null)
		{
			return 0;
		}
		float canReceive = max;
		canReceive = Math.min(canReceive, ph.getMaxEnergyReceived());
		canReceive = Math.min(canReceive, ph.getMaxEnergyStored() - ph.getEnergyStored());
		if(canReceive <= ph.getMinEnergyReceived())
		{
			return 0;
		}
		return canReceive;
	}
	
	public static void doBuildCraftEnergyTick(PowerHandler ph, ForgeDirection from)
	{
		if(ph == null)
		{
			return;
		}
		PowerReceiver pp = ph.getPowerReceiver();
		if(pp == null)
		{
			return;
		}
		float stored = ph.getEnergyStored();
		pp.receiveEnergy(Type.PIPE, 0, from);
		ph.setEnergy(stored);
	}
	
	private static class NullPerditionCalculator extends PerditionCalculator
	{
		@Override
		public float applyPerdition(PowerHandler powerHandler, float current, long ticksPassed)
		{
			if(current <= 0)
			{
				return 0;
			}
			float decAmount = 0.001f;
			float res;
			do{
				res = current - decAmount;
				decAmount *= 10;
			} while (res >= current && decAmount < PerditionCalculator.MIN_POWERLOSS);
			return res;
		}
	}
}
