package com.madpcgaming.citytech.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class AbstractPipingNetwork<T extends IPiping, I extends T>
{
	protected final List<I> pipes = new ArrayList<I>();
	
	protected Class<I> implClass;
	
	protected AbstractPipingNetwork(Class<I> implClass)
	{
		this.implClass = implClass;
	}
	
	public void init(IPipingBundle tile, Collection<I> connections, World world)
	{
		if(world.isRemote)
		{
			throw new UnsupportedOperationException();
		}
		
		for(I pipe : connections)
		{
			AbstractPipingNetwork<?,?> network = con.getNetwork();
			if(network != null)
			{
				network.destoryNetwork();
			}
		}
		setNetwork(world, tile);
		notifyNetworkOfUpdate();
	}
	
	public abstract Class<T> getBasePipingType();
	
	protected void setNetwork(World world, IPipingBundle tile)
	{
		T piping = tile.getPiping(getBasePipingType());
		
		if(piping != null && implClass.isAssignableFrom(piping.getClass()) && piping.setNetwork(this))
		{
			addPiping(implClass.cast(piping));
			TileEntity te = tile.getEntity();
			Collection<T> connections = PipingUtil.getConnectedPiping(world, te.xCoord, te.yCoord, te.zCoord, getBasePipingType());
			for(T pipe : connections)
			{
				if(pipe.getNetwork() == null)
				{
					setNetwork(world, pipe.getBundle());
				} 
				else if(pipe.getNetwork() != this)
				{
					pipe.getNetwork().destoryNetwork();
					setNetwork(world, pipe.getBundle());
				}
			}
		}
	}
	
	public void addPiping(I pipe)
	{
		if(!pipes.contains(pipe))
		{
			pipes.add(pipe);
		}
	}
	
	public void destoryNetwork()
	{
		for(I pipe : pipes)
		{
			pipe.setNetwork(null);
		}
		pipes.clear();
	}
	
	public List<I> getPiping()
	{
		return pipes;
	}
	
	public void notifyNetworkOfUpdate()
	{
		for(I pipe : pipes)
		{
			TileEntity te = pipe.getBundle().getEntity();
			te.getWorldObj().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(IPiping pipe: pipes)
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
