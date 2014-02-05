package com.madpcgaming.citytech.items.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class AbstractPipingNetwork<T extends IPiping>
{
	protected final List<T> piping = new ArrayList<T>();
	
	protected AbstractPipingNetwork()
	{
		
	}
	
	public void init(IPipingBundle tile, Collection<T> connections, World world)
	{
		if(world.isRemote)
		{
			throw new UnsupportedOperationException();
		}
		
		for(T pipe : connections)
		{
			AbstractPipingNetwork<?> network = pipe.getNetwork();
			if (network != null)
			{
				network.destroyNetwork();
			}
		}
		setNetwork(world, tile);
		notifyNetworkOfUpdate();
	}
	
	public abstract Class<? extends T> getBasePipingType();
	
	protected void setNetwork(World world, IPipingBundle tile)
	{
		T piping = tile.getPiping(getBasePipingType());
		if(piping.setNetwork(this))
		{
			addPiping(piping);
			TileEntity te = tile.getEntity();
			Collection<? extends T> connections = PipingUtil.getConnectedPiping(world, te.field_145851_c, te.field_145848_d, te.field_145849_e, getBasePipingType());
			for(T pipe : connections)
			{
				if(pipe.getNetwork() == null)
				{
					setNetwork(world, pipe.getBundle());
				}
				else if(pipe.getNetwork() != this)
				{
					pipe.getNetwork().destroyNetwork();
					setNetwork(world, pipe.getBundle());
				}
			}
		}
	}
	
	public void addPiping(T pipe)
	{
		if(!piping.contains(pipe)){
			piping.add(pipe);
		}
	}
	
	public void destroyNetwork()
	{
		for(T pipe : piping)
		{
			pipe.setNetwork(null);
		}
		piping.clear();
	}
	
	public List<T> getPiping()
	{
		return piping;
	}
	
	public void notifyNetworkOfUpdate()
	{
		for(T pipe : piping)
		{
			TileEntity te = pipe.getBundle().getEntity();
			te.func_145831_w().func_147439_a(te.field_145851_c, te.field_145848_d, te.field_145849_e);
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(IPiping pipe : piping)
		{
			sb.append(pipe.getLocation());
			sb.append(", ");
		}
		return "AbstractPipingNetwork [piping=" + sb.toString() + "]";
	}
	
	public void onUpdateEntity(IPiping piping)
	{
		
	}
}
