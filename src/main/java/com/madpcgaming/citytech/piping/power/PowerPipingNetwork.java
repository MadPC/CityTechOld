package com.madpcgaming.citytech.piping.power;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.util.BlockCoord;

public class PowerPipingNetwork extends AbstractPipingNetwork<IPowerPiping, IPowerPiping>
{
	NetworkPowerManager powerManager;
	
	private Map<ReceptorKey, ReceptorEntry> powerReceptors = new HashMap<ReceptorKey, ReceptorEntry>();
	
	private long timeAtLastApply = -1;
	
	public PowerPipingNetwork()
	{
		super(IPowerPiping.class);
	}
	
	@Override
	public void init(IPipingBundle tile, Collection<IPowerPiping> connections, World world)
	{
		super.init(tile, connections, world);
		powerManager = new NetworkPowerManager(this, world);
		powerManager.receptorsChanged();
	}
	
	@Override
	public void destroyNetwork()
	{
		for(IPowerPiping pipe : pipes)
		{
			pipe.setActive(false);
		}
		powerManager.onNetworkDestroyed();
		super.destroyNetwork();
	}
	
	public NetworkPowerManager getPowerManager()
	{
		return powerManager;
	}
	
	@Override
	public void addPiping(IPowerPiping pipe)
	{
		super.addPiping(pipe);
		Set<ForgeDirection> externalDirs = pipe.getExternalConnections();
		for(ForgeDirection dir : externalDirs)
		{
			IPowerInterface pr = pipe.getExternalPowerReceptor(dir);
			if(pr != null)
			{
				TileEntity te = pipe.getBundle().getEntity();
				powerReceptorAdded(pipe, dir, te.xCoord + dir.offsetX, te.yCoord + dir.offsetY, te.zCoord + dir.offsetZ, pr);
			}
		}
		if(powerManager != null)
		{
			pipe.setActive(powerManager.isActive());
		}
	}
	
	@Override
	public Class<IPowerPiping> getBasePipingType()
	{
		return IPowerPiping.class;
	}
	
	public void powerReceptorAdded(IPowerPiping powerPiping, ForgeDirection direction, int x, int y, int z, IPowerInterface powerReceptor)
	{
		if(powerReceptor == null)
		{
			return;
		}
		BlockCoord location = new BlockCoord(x,y,z);
		ReceptorKey key = new ReceptorKey(location, direction);
		ReceptorEntry re = powerReceptors.get(key);
		if(re == null)
		{
			re = new ReceptorEntry(powerReceptor, location, powerPiping, direction);
			powerReceptors.put(key, re);
		}
		if(powerManager != null)
		{
			powerManager.receptorsChanged();
		}
	}
	
	public void powerReceptorRemoved(int x, int y, int z)
	{
		BlockCoord bc = new BlockCoord(x,y,z);
		List<ReceptorKey> remove = new ArrayList<ReceptorKey>();
		for(ReceptorKey key : powerReceptors.keySet())
		{
			if(key != null && key.coord.equals(bc))
			{
				remove.add(key);
			}
		}
		for(ReceptorKey key : remove)
		{
			powerReceptors.remove(key);
		}
		powerManager.receptorsChanged();
	}
	
	public Collection<ReceptorEntry> getPowerReceptors()
	{
		return powerReceptors.values();
	}
	
	@Override
	public void onUpdateEntity(IPiping piping)
	{
		World world = piping.getBundle().getEntity().getWorldObj();
		if(world == null)
		{
			return;
		}
		if(world.isRemote)
		{
			return;
		}
		long curTime = world.getTotalWorldTime();
		if(curTime != timeAtLastApply)
		{
			timeAtLastApply = curTime;
			powerManager.applyreceivedPower();
		}
	}
	
	public static class ReceptorEntry
	{
		IPowerPiping emitter;
		BlockCoord coord;
		ForgeDirection direction;
		IPowerInterface powerInterface;
		
		public ReceptorEntry(IPowerInterface powerReceptor, BlockCoord coord, IPowerPiping emitter, ForgeDirection direction)
		{
			powerInterface = powerReceptor;
			this.coord = coord;
			this.emitter = emitter;
			this.direction = direction;
		}
	}
	
	private static class ReceptorKey {
	    BlockCoord coord;
	    ForgeDirection direction;

	    ReceptorKey(BlockCoord coord, ForgeDirection direction) {
	      this.coord = coord;
	      this.direction = direction;
	    }

	    @Override
	    public int hashCode() {
	      final int prime = 31;
	      int result = 1;
	      result = prime * result + ((coord == null) ? 0 : coord.hashCode());
	      result = prime * result + ((direction == null) ? 0 : direction.hashCode());
	      return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	      if(this == obj)
	        return true;
	      if(obj == null)
	        return false;
	      if(getClass() != obj.getClass())
	        return false;
	      ReceptorKey other = (ReceptorKey) obj;
	      if(coord == null) {
	        if(other.coord != null)
	          return false;
	      } else if(!coord.equals(other.coord))
	        return false;
	      if(direction != other.direction)
	        return false;
	      return true;
	    }

	  }

	}