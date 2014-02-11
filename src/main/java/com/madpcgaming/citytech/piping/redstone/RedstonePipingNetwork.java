package com.madpcgaming.citytech.piping.redstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.util.BlockCoord;

public class RedstonePipingNetwork extends AbstractPipingNetwork<IRedstonePiping, IRedstonePiping>
{
	private final Set<Signal> signals = new HashSet<Signal>();
	boolean updatingNetwork = false;
	private boolean networkEnabled = true;
	
	public RedstonePipingNetwork()
	{
		super(IRedstonePiping.class);
	}
	
	@Override
	public Class<IRedstonePiping> getBasePipingType()
	{
		return IRedstonePiping.class;
	}
	
	@Override
	public void init(IPipingBundle tile, Collection<IRedstonePiping> connections, World world)
	{
		super.init(tile, connections, world);
		updatingNetwork = true;
		notifyNeighborsOfSignals();
		updatingNetwork = false;
	}
	
	@Override
	public void destroyNetwork()
	{
		updatingNetwork = true;
		for(IRedstonePiping pipe : pipes)
		{
			pipe.setActive(false);
		}
		
		List<Signal> copy = new ArrayList<Signal>(signals);
		signals.clear();
		for(Signal s : copy)
		{
			notifyNeighborsOfSignalUpdate(s);
		}
		updatingNetwork = false;
		super.destroyNetwork();
	}
	
	@Override
	public void addPiping(IRedstonePiping pipe)
	{
		updatingNetwork = true;
		super.addPiping(pipe);
		Set<Signal> newInputs = pipe.getNetworkInputs();
		signals.addAll(newInputs);
		for(Signal signal : newInputs)
		{
			notifyNeighborsOfSignalUpdate(signal);
		}
		for(Signal signal: signals)
		{
			notifyPipingNeighbors(pipe, signal);
		}
		updatingNetwork = false;
	}
	
	public Set<Signal> getSignals()
	{
		if(networkEnabled)
		{
			return signals;
		}else
		{
			return Collections.emptySet();
		}
	}
	
	void setNetworkEnabled(boolean enabled)
	{
		networkEnabled = enabled;
	}
	
	public boolean isNetworkEnabled()
	{
		return networkEnabled;
	}
	
	public void addSignals(Set<Signal> newSignals)
	{
		for(Signal signal : newSignals)
		{
			addSignal(signal);
		}
	}
	
	public void addSignal(Signal signal)
	{
		updatingNetwork = true;
		signals.add(signal);
		notifyNetworkOfUpdate();
		notifyNeighborsOfSignalUpdate(signal);
		updatingNetwork = false;
	}
	
	public void removeSignals(Set<Signal> remove)
	{
		for(Signal signal: remove)
		{
			removeSignal(signal);
		}
	}
	
	public void removeSignal(Signal signal)
	{
		updatingNetwork = true;
		signals.remove(signal);
		notifyNetworkOfUpdate();
		notifyNeighborsOfSignalUpdate(signal);
		updatingNetwork = false;
	}
	
	public void replaceSignal(Signal oldSig, Signal newSig) 
	{
		updatingNetwork = true;
		signals.remove(oldSig);
		signals.add(newSig);
		notifyNetworkOfUpdate();
		notifyNeighborsOfSignalUpdate(newSig);
		updatingNetwork = false;
	}

	@Override
	public void notifyNetworkOfUpdate() 
	{
		for (IRedstonePiping pipe : pipes)
		{
			pipe.setActive(!getSignals().isEmpty());
		}
		super.notifyNetworkOfUpdate();
	}
	
	@Override
	  public String toString() {
	    return "RedstonePipingNetwork [signals=" + signalsString() + ", pipes=" + pipesString() + "]";
	  }

	  private String pipesString() {
	    StringBuilder sb = new StringBuilder();
	    for (IRedstonePiping pipe : pipes) {
	      TileEntity te = pipe.getBundle().getEntity();
	      sb.append("<");
	      sb.append(te.xCoord + "," + te.yCoord + "," + te.zCoord);
	      sb.append(">");
	    }
	    return sb.toString();
	  }

	  String signalsString() {
	    StringBuilder sb = new StringBuilder();
	    for (Signal s : signals) {
	      sb.append("<");
	      sb.append(s);
	      sb.append(">");

	    }
	    return sb.toString();
	  }

	  public void notifyNeighborsOfSignals() {
	    for (Signal signal : signals) {
	      notifyNeighborsOfSignalUpdate(signal);
	    }
	  }

	  public void notifyNeighborsOfSignalUpdate(Signal signal) {
	    ArrayList<IRedstonePiping> pipesCopy = new ArrayList<IRedstonePiping>(pipes);
	    for (IRedstonePiping pipe : pipesCopy) {
	      notifyPipingNeighbors(pipe, signal);
	    }
	  }

	  private void notifyPipingNeighbors(IRedstonePiping pipe, Signal signal) {
	    if(pipe.getBundle() == null) {
	      System.out.println("RedstoneConduitNetwork.notifyNeigborsOfSignalUpdate: NULL BUNDLE!!!!");
	      return;
	    }
	    TileEntity te = pipe.getBundle().getEntity();

	    te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord));

	    if(signal != null && signal.strength >= 15 && signal.x == te.xCoord && signal.y == te.yCoord && signal.z == te.zCoord) {

	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord + 1, te.yCoord, te.zCoord, te.getWorldObj().getBlock(te.xCoord + 1, te.yCoord, te.zCoord));
	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord - 1, te.yCoord, te.zCoord, te.getWorldObj().getBlock(te.xCoord - 1, te.yCoord, te.zCoord));
	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord + 1, te.zCoord, te.getWorldObj().getBlock(te.xCoord, te.yCoord + 1, te.zCoord));
	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord - 1, te.zCoord, te.getWorldObj().getBlock(te.xCoord, te.yCoord - 1, te.zCoord));
	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord, te.zCoord + 1, te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord + 1));
	      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord, te.zCoord - 1, te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord - 1));

	    }
	    broadcastRednetUpdate(pipe);
	  }

	  private void broadcastRednetUpdate(IRedstonePiping pipe) {
	    World worldObj = pipe.getBundle().getEntity().getWorldObj();
	    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
	      BlockCoord to = pipe.getLocation().getLocation(dir);
	      //Block b = Util.getBlock(worldObj.getBlockId(to.x, to.y, to.z));
	      //if(b instanceof IRedNetNetworkContainer) {
	        //((IRedNetNetworkContainer) b).updateNetwork(worldObj, to.x, to.y, to.z);
	      //}
	    }
	  }

}
