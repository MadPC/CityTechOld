package com.madpcgaming.citytech.piping.liquid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.util.BlockCoord;

public class LiquidPipingNetwork extends AbstractTankPipingNetwork<LiquidPiping>
{
	public LiquidPipingNetwork()
	{
		super(LiquidPiping.class);
	}
	
	private long timeAtLastApply;
	private int maxFlowPerTick = 10;
	private int lastFlowIndex = 0;
	private boolean printFlowTiming = false;
	private int pushToken = 0;
	@SuppressWarnings("unused")
	private int inputVolume;
	@SuppressWarnings("unused")
	private int outputVolume;
	private boolean inputLocked = false;
	
	public boolean lockNetworkForFill()
	{
		if(inputLocked)
		{
			return false;
		}
		inputLocked = true;
		return true;
	}
	
	public void unlockNetworkForFill()
	{
		inputLocked = false;
	}
	
	@Override
	public void onUpdateEntity(IPiping piping)
	{
		World world = piping.getBundle().getEntity().getWorldObj();
		if(world == null)
		{
			return;
		}
		if(world.isRemote || liquidType == null)
		{
			return;
		}
		
		long curTime = world.getTotalWorldTime();
		if(curTime > 0 && curTime != timeAtLastApply)
		{
			timeAtLastApply = curTime;
			if(liquidType != null && liquidType.getFluid() != null)
			{
				int visc = Math.max(1000, liquidType.getFluid().getViscosity());
				if(curTime % (visc / 500) == 0)
				{
					long start = System.nanoTime();
					if(doFlow() && printFlowTiming)
					{
						long took = System.nanoTime() - start;
						double secs = took / 1000000000.0;
						System.out.println("LiquidPipingNetwork.onUpdateEntity: took " + secs + " secs, " + (secs * 1000) + " millis");
					}
				}
			}
		}
		if(!fluidTypeLocked && isEmpty())
		{
			setFluidType(null);
		}
	}
	
	void addedFromExternal(int res)
	{
		inputVolume += res;
	}
	
	void outputedToExternal(int filled)
	{
		outputVolume += filled;
	}
	
	int getNextPushToken()
	{
		return ++pushToken;
	}
	
	private boolean doFlow() {

	    int pushToken = getNextPushToken();
	    List<FlowAction> actions = new ArrayList<FlowAction>();
	    for (int i = 0; i < Math.min(maxFlowPerTick, pipes.size()); i++) {

	      if(lastFlowIndex >= pipes.size()) {
	        lastFlowIndex = 0;
	      }
	      flowFrom(pipes.get(lastFlowIndex), actions, pushToken);
	      ++lastFlowIndex;

	    }
	    for (FlowAction action : actions) {
	      action.apply();
	    }

	    boolean result = !actions.isEmpty();

	    List<LiquidPiping> toEmpty = new ArrayList<LiquidPiping>();
	    for (LiquidPiping pipe : pipes) {
	      if(pipe != null && pipe.getTank().getFluidAmount() < 10) {
	        toEmpty.add(pipe);
	      } else {
	        return result;
	      }

	    }
	    if(toEmpty.isEmpty()) {
	      return result;
	    }

	    List<LocatedFluidHandler> externals = new ArrayList<LocatedFluidHandler>();
	    for (AbstractTankPiping pipe : pipes) {
	      Set<ForgeDirection> extCons = pipe.getExternalConnections();

	      for (ForgeDirection dir : extCons) {
	        if(pipe.canOutputToDir(dir)) {
	          IFluidHandler externalTank = pipe.getExternalHandler(dir);
	          if(externalTank != null) {
	            externals.add(new LocatedFluidHandler(externalTank, pipe.getLocation().getLocation(dir), dir.getOpposite()));
	          }
	        }
	      }
	    }
	    if(externals.isEmpty()) {
	      return result;
	    }

	    for (LiquidPiping pipe : toEmpty) {
	      drainConduitToNearestExternal(pipe, externals);
	    }

	    return result;
	  }
	
	@Override
	  public void setFluidTypeLocked(boolean fluidTypeLocked) {
	    super.setFluidTypeLocked(fluidTypeLocked);
	    if(!fluidTypeLocked && isEmpty()) {
	      setFluidType(null);
	    }
	  }

	  private boolean isEmpty() {
	    for (LiquidPiping pipe : pipes) {
	      if(pipe.tank.getFluidAmount() > 0) {
	        return false;
	      }
	    }
	    return true;
	  }

	  private void drainConduitToNearestExternal(LiquidPiping pipe, List<LocatedFluidHandler> externals) {
	    BlockCoord conLoc = pipe.getLocation();
	    FluidStack toDrain = pipe.getTank().getFluid();
	    if(toDrain == null) {
	      return;
	    }
	    int closestDistance = Integer.MAX_VALUE;
	    LocatedFluidHandler closestTank = null;
	    for (LocatedFluidHandler lh : externals) {
	      int distance = lh.bc.distanceSquared(conLoc);
	      if(distance < closestDistance) {
	        int couldFill = lh.tank.fill(lh.dir, toDrain.copy(), false);
	        if(couldFill > 0) {
	          closestTank = lh;
	          closestDistance = distance;
	        }
	      }
	    }

	    if(closestTank != null) {
	      int filled = closestTank.tank.fill(closestTank.dir, toDrain.copy(), true);
	      pipe.getTank().addAmount(-filled);
	    }

	  }

	  @SuppressWarnings("unused")
	private void flowFrom(LiquidPiping pipe, List<FlowAction> actions, int pushToken) {

	    PipingTank tank = pipe.getTank();
	    int totalAmount = tank.getFluidAmount();
	    if(totalAmount <= 0) {
	      return;
	    }

	    int maxFlowVolume = 20;

	    // First flow all we can down, then balance the rest
	    if(pipe.getPipingConnections().contains(ForgeDirection.DOWN)) {
	      BlockCoord loc = pipe.getLocation().getLocation(ForgeDirection.DOWN);
	      ILiquidPiping dc = PipingUtil.getPiping(pipe.getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, ILiquidPiping.class);
	      if(dc instanceof LiquidPiping) {
	        LiquidPiping downCon = (LiquidPiping) dc;
	        int filled = ((LiquidPiping) downCon).fill(ForgeDirection.UP, tank.getFluid().copy(), false, false, pushToken);
	        int actual = filled;
	        actual = Math.min(actual, tank.getFluidAmount());
	        actual = Math.min(actual, downCon.getTank().getAvailableSpace());
	        tank.addAmount(-actual);
	        downCon.getTank().addAmount(actual);
	      }
	    }

	    totalAmount = tank.getFluidAmount();
	    if(totalAmount <= 0) {
	      return;
	    }
	    FluidStack available = tank.getFluid();
	    int totalRequested = 0;
	    int numRequests = 0;
	    // Then to external connections
	    for (ForgeDirection dir : pipe.getExternalConnections()) {
	      if(pipe.canOutputToDir(dir)) {
	        IFluidHandler extCon = pipe.getExternalHandler(dir);
	        if(extCon != null) {
	          int amount = extCon.fill(dir.getOpposite(), available.copy(), false);
	          if(amount > 0) {
	            totalRequested += amount;
	            numRequests++;
	          }
	        }
	      }
	    }

	    if(numRequests > 0) {
	      int amountPerRequest = Math.min(totalAmount, totalRequested) / numRequests;
	      amountPerRequest = Math.min(maxFlowVolume, amountPerRequest);

	      FluidStack requestSource = available.copy();
	      requestSource.amount = amountPerRequest;
	      for (ForgeDirection dir : pipe.getExternalConnections()) {
	        if(pipe.canOutputToDir(dir)) {
	          IFluidHandler extCon = pipe.getExternalHandler(dir);
	          if(extCon != null) {
	            int amount = extCon.fill(dir.getOpposite(), requestSource.copy(), true);
	            if(amount > 0) {
	              outputedToExternal(amount);
	              tank.addAmount(-amount);
	            }
	          }
	        }
	      }
	    }

	    totalAmount = tank.getFluidAmount();
	    if(totalAmount <= 0) {
	      return;
	    }
	    int totalCapacity = tank.getCapacity();

	    BlockCoord loc = pipe.getLocation();
	    Collection<ILiquidPiping> connections =
	        PipingUtil.getConnectedPiping(pipe.getBundle().getEntity().getWorldObj(),
	            loc.x, loc.y, loc.z, ILiquidPiping.class);
	    int numTargets = 0;
	    for (ILiquidPiping n : connections) {
	      LiquidPiping neighbor = (LiquidPiping) n;
	      if(canFlowTo(pipe, neighbor)) { // can only flow within same network
	        totalAmount += neighbor.getTank().getFluidAmount();
	        totalCapacity += neighbor.getTank().getCapacity();
	        numTargets++;
	      }
	    }

	    float targetRatio = (float) totalAmount / totalCapacity;
	    int flowVolume = (int) Math.floor((targetRatio - tank.getFilledRatio()) * tank.getCapacity());
	    flowVolume = Math.min(maxFlowVolume, flowVolume);

	    if(Math.abs(flowVolume) < 2) {
	      return; // dont bother with transfers of less than a thousands of a bucket
	    }

	    for (ILiquidPiping n : connections) {
	      LiquidPiping neigbor = (LiquidPiping) n;
	      if(canFlowTo(pipe, neigbor)) { // can only flow within same network
	        flowVolume = (int) Math.floor((targetRatio - neigbor.getTank().getFilledRatio()) * neigbor.getTank().getCapacity());
	        if(flowVolume != 0) {
	          actions.add(new FlowAction(pipe, neigbor, flowVolume));
	        }
	      }
	    }

	  }

	  private boolean canFlowTo(LiquidPiping pipe, LiquidPiping neighbor) {
	    if(pipe == null || neighbor == null) {
	      return false;
	    }
	    if(neighbor.getNetwork() != this) {
	      return false;
	    }
	    if(neighbor.getLocation().y > pipe.getLocation().y) {
	      return false;
	    }
	    float nr = neighbor.getTank().getFilledRatio();
	    if(nr >= pipe.getTank().getFilledRatio()) {
	      return false;
	    }
	    return true;
	  }
	  
	  static class FlowAction
	  {
		  final LiquidPiping from;
		  final LiquidPiping to;
		  final int amount;
		  
		  FlowAction(LiquidPiping fromIn, LiquidPiping toIn, int amountIn)
		  {
			  if(amountIn < 0)
			  {
				  to = fromIn;
				  from = toIn;
				  amount = -amountIn;
			  }
			  else 
			  {
				  to = toIn;
				  from = fromIn;
				  amount = amountIn;
			  }
		  }
		  
		  void apply()
		  {
			  if(amount != 0)
			  {
				  int actual = Math.min(amount, from.getTank().getFluidAmount());
				  actual = Math.min(actual, to.getTank().getAvailableSpace());
				  if(from != null && to != null)
				  {
					  from.getTank().addAmount(-actual);
					  to.getTank().addAmount(actual);
				  }
			  }
		  }
	  }

	  static class LocatedFluidHandler {
	    final IFluidHandler tank;
	    final BlockCoord bc;
	    final ForgeDirection dir;

	    LocatedFluidHandler(IFluidHandler tank, BlockCoord bc, ForgeDirection dir) {
	      this.tank = tank;
	      this.bc = bc;
	      this.dir = dir;
	    }

	  }

}
