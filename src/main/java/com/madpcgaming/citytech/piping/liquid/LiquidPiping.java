package com.madpcgaming.citytech.piping.liquid;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.madpcgaming.citytech.lib.CityTechConfig;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.util.BlockCoord;


public class LiquidPiping extends AbstractTankPiping
{
	static final int VOLUME_PER_CONNECTION = FluidContainerRegistry.BUCKET_VOLUME / 4;
	
	private LiquidPipingNetwork network;
	private float lastSyncRatio = -99;
	private int currentPushToken;
	@SuppressWarnings("unused")
	private long lastEmptyTick = 0;
	@SuppressWarnings("unused")
	private int numEmptyEvents = 0;
	
	public static final int MAX_EXTRACT_PER_TICK = CityTechConfig.fluidPipingExtractRate;
	public static final int MAX_IO_PER_TICK = CityTechConfig.fluidPipingMaxIoRate;
	private ForgeDirection startPushDir = ForgeDirection.DOWN;
	private final Set<BlockCoord> filledFromThisTick = new HashSet<BlockCoord>();
	private long ticksSinceFailedExtract = 0;
	
	@SuppressWarnings("unused")
	@Override
	public void updateEntity(World world)
	{
		super.updateEntity(world);
		if(world.isRemote)
		{
			return;
		}
		filledFromThisTick.clear();
		updateStartPushDir();
		doExtract();
		
		if(stateDirty)
		{
			getBundle().dirty();
			stateDirty = false;
			lastSyncRatio = tank.getFilledRatio();
		}
		else if((lastSyncRatio != tank.getFilledRatio() && world.getTotalWorldTime() % 2 == 0))
		{
			BlockCoord loc = getLocation();
			lastSyncRatio = tank.getFilledRatio();
		}
	}
	
	@SuppressWarnings("unused")
	private void doExtract()
	{
		BlockCoord loc = getLocation();
		if(!hasConnectionMode(ConnectionMode.INPUT))
		{
			return;
		}
		
		ticksSinceFailedExtract++;
		if(ticksSinceFailedExtract > 9 && ticksSinceFailedExtract % 10 != 0)
		{
			return;
		}
		
		Fluid f = tank.getFluid() == null ? null : tank.getFluid().getFluid();
		int token = network == null ? -1 : network.getNextPushToken();
		for(ForgeDirection dir : externalConnections)
		{
			if(autoExtractForDir(dir))
			{
				IFluidHandler extTank = getTankContainer(getLocation().getLocation(dir));
				if(extTank != null)
				{
					FluidStack couldDrain = extTank.drain(dir.getOpposite(), MAX_EXTRACT_PER_TICK, false);
					if(couldDrain != null && couldDrain.amount > 0 &&  canFill(dir, couldDrain.getFluid()))
					{
						int used = pushLiquid(dir, couldDrain, true, network == null ? -1 : network.getNextPushToken());
						extTank.drain(dir.getOpposite(), used, true);
						if(used > 0 && network != null && network.getFluidType() == null)
						{
							network.setFluidType(couldDrain);
						}
						if(used > 0)
						{
							ticksSinceFailedExtract = 0;
						}
					}
				}
			}
		}
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(network == null || resource == null)
		{
			return 0;
		}
		if(!canFill(from, resource.getFluid()))
		{
			return 0;
		}
		if(filledFromThisTick.contains(getLocation().getLocation(from)))
		{
			return 0;
		}
		
		if(network.lockNetworkForFill())
		{
			if(doFill)
			{
				filledFromThisTick.add(getLocation().getLocation(from));
			}
			try
			{
				int res = fill(from, resource, doFill, true, network == null ? -1 : network.getNextPushToken());
				if(doFill && externalConnections.contains(from) && network != null)
				{
					network.addedFromExternal(res);
				}
				return res;
			} finally {
				network.unlockNetworkForFill();
			} 
		} else {
			return 0;
		}
	}
	
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill, boolean doPush, int pushToken)
	{
		if(resource == null || resource.amount <= 0)
		{
			return 0;
		}
		if(!canFill(from, resource.getFluid()))
		{
			return 0;
		}
		if(network == null)
		{
			return 0;
		}
		if(network.canAcceptLiquid(resource))
		{
			network.setFluidType(resource);
		} else
		{
			return 0;
		}
		
		resource.amount = Math.min(MAX_IO_PER_TICK, resource.amount);
		
		if(doPush)
		{
			return pushLiquid(from, resource, doFill, pushToken);
		} else
		{
			return tank.fill(resource, doFill);
		}
	}
	
	private void updateStartPushDir()
	{
		ForgeDirection newVal = getNextDir(startPushDir);
		boolean foundNewStart = false;
		while(newVal != startPushDir && !foundNewStart)
		{
			foundNewStart = getPipingConnections().contains(newVal) || getExternalConnections().contains(newVal);
			newVal = getNextDir(newVal);
		}
		startPushDir = newVal;
	}
	
	private ForgeDirection getNextDir(ForgeDirection dir)
	{
		if(dir.ordinal() >= ForgeDirection.UNKNOWN.ordinal() - 1)
		{
			return ForgeDirection.VALID_DIRECTIONS[0];
		}
		return ForgeDirection.VALID_DIRECTIONS[dir.ordinal() + 1];
	}
	
	private int pushLiquid(ForgeDirection from, FluidStack pushStack, boolean doPush, int token)
	{
		if(token == currentPushToken || pushStack == null || pushStack.amount <= 0 || network == null)
		{
			return 0;
		}
		currentPushToken = token;
		int pushed = 0;
		int total = pushStack.amount;
		
		ForgeDirection dir = startPushDir;
		FluidStack toPush = pushStack.copy();
		
		int filledLocal = tank.fill(toPush, doPush);
		toPush.amount -= filledLocal;
		pushed += filledLocal;
		
		do{
			if(dir != from && canOutputToDir(dir))
			{
				ILiquidPiping pipingCon = getFluidPiping(dir);
				if(pipingCon != null)
				{
					int toCon = ((LiquidPiping) pipingCon).pushLiquid(dir.getOpposite(), toPush, doPush, token);
					toPush.amount -= toCon;
					pushed += toCon;
				}
			} else if (getExternalConnections().contains(dir))
			{
				IFluidHandler pipe = getTankContainer(getLocation().getLocation(dir));
				if(pipe != null)
				{
					int toExt = pipe.fill(dir.getOpposite(), toPush, doPush);
					toPush.amount -= toExt;
					pushed += toExt;
					if(doPush)
					{
						network.outputedToExternal(toExt);
					}
				}
			}
			dir = getNextDir(dir);
		}
		while(dir != startPushDir && pushed < total);
		return pushed;
	} 
	
	private ILiquidPiping getFluidPiping(ForgeDirection dir)
	{
		TileEntity ent = getBundle().getEntity();
		return PipingUtil.getPiping(ent.getWorldObj(), ent, dir, ILiquidPiping.class);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if(getConnectionMode(from) == ConnectionMode.INPUT || getConnectionMode(from) == ConnectionMode.DISABLED)
		{
			return null;
		}
		return tank.drain(maxDrain, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if(resource != null && !resource.isFluidEqual(tank.getFluid()))
		{
			return null;
		}
		return drain(from, resource.amount, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if(getConnectionMode(from) == ConnectionMode.OUTPUT || getConnectionMode(from) == ConnectionMode.DISABLED)
		{
			return false;
		}
		if(tank.getFluid() == null)
		{
			return true;
		}
		if(fluid != null && fluid.getID() == tank.getFluid().fluidID)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		if(getConnectionMode(from) == ConnectionMode.INPUT || getConnectionMode(from) == ConnectionMode.DISABLED || tank.getFluid() == null || fluid == null)
		{
			return false;
		}
		return tank.getFluid().getFluid().getID() == fluid.getID();
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{	tank.getInfo()};
	}
	
	@Override
	protected void connectionsChanged()
	{
		super.connectionsChanged();
		updateTank();
	}
	
	@Override
	public ItemStack createItem()
	{
	return null;// new ItemStack();
	}
	
	
	@Override
	public AbstractPipingNetwork<?,?> getNetwork()
	{
		return network;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean setNetwork(AbstractPipingNetwork<?,?> network)
	{
		if(network == null)
		{
			this.network = null;
			return true;
		}
		if(!(network instanceof AbstractTankPipingNetwork))
		{
			return false;
		}
		
		AbstractTankPipingNetwork n = (AbstractTankPipingNetwork) network;
		if(tank.getFluid() == null)
		{
			tank.setLiquid(n.getFluidType() == null ? null : n.getFluidType().copy());
		} else if (n.getFluidType() == null)
		{
			n.setFluidType(tank.getFluid());
		} else if(!tank.getFluid().isFluidEqual(n.getFluidType()))
		{
			return false;
		}
		this.network = (LiquidPipingNetwork) network;
		return true;
	}
	

	@Override
	public IIcon getTextureForState(CollidableComponent component)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IIcon getTransmitionTextureForState(CollidableComponent component)
	{
		if(tank.getFluid() != null && tank.getFluid().getFluid() != null)
		{
			return tank.getFluid().getFluid().getStillIcon();
		}
		return null;
	}
	
	@Override
	public float getTransmitionGeometryScale()
	{
		return tank.getFilledRatio();
	}
	
	@Override
	protected void updateTank()
	{
		int totalConnections = getPipingConnections().size() + getExternalConnections().size();
		tank.setCapacity(totalConnections * VOLUME_PER_CONNECTION);
	}
	
	@Override
	protected boolean canJoinNeighbor(ILiquidPiping n)
	{
		return n instanceof LiquidPiping;
	}
	
	@Override
	public AbstractTankPipingNetwork<? extends AbstractTankPiping> getTankNetwork()
	{
		return network;
	}

}
	
