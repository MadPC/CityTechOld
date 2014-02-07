package com.madpcgaming.citytech.piping.liquid;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.piping.RaytraceResult;
import com.madpcgaming.citytech.util.BlockCoord;


public abstract class AbstractTankPiping extends AbstractLiquidPiping
{
	protected PipingTank tank = new PipingTank(0);
	protected boolean stateDirty = false;
	protected long lastEmptyTick = 0;
	protected int numEmptyEvents = 0;
	protected boolean fluidTypeLocked = false;
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, RaytraceResult res, List<RaytraceResult> all)
	{
		if(player.getHeldItem() == null)
		{
			return false;
		}
		AbstractTankPipingNetwork<? extends AbstractTankPiping> network = getTankNetwork();
		if(PipingUtil.isToolEquipped(player))
		{
			if(!getBundle().getEntity().getWorldObj().isRemote)
			{
				if(res != null && res.component != null)
				{
					ForgeDirection connDir = res.component.dir;
					ForgeDirection faceHit = ForgeDirection.getOrientation(res.movingObjectPosition.sideHit);
					
					if(connDir == ForgeDirection.UNKNOWN || connDir == faceHit)
					{
						BlockCoord loc = getLocation().getLocation(faceHit);
						ILiquidPiping n = PipingUtil.getPiping(getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, ILiquidPiping.class);
						if(n == null)
						{
							return false;
						}
						if(!canJoinNeighbor(n))
						{
							return false;
						}
						if(!(n instanceof AbstractTankPiping))
						{
							return false;
						}
						AbstractTankPiping neighbor = (AbstractTankPiping) n;
						if(neighbor.getFluidType() == null || getFluidType() == null)
						{
							FluidStack type = getFluidType();
							type = type != null ? type : neighbor.getFluidType();
							neighbor.setFluidTypeOnNetwork(neighbor, type);
							setFluidTypeOnNetwork(this, type);
						}
						return PipingUtil.joinPiping(this, faceHit);
					} else if(containsExternalConnection(connDir))
					{
						setConnectionMode(connDir, getNextConnectionMode(connDir));
					} else if(containsPipingConnection(connDir))
					{
						FluidStack curFluidType = null;
						if(getTankNetwork() != null)
						{
							curFluidType = getTankNetwork().getFluidType();
						}
						PipingUtil.disconnectPiping(this, connDir);
						setFluidType(curFluidType);
					}
				}
			}
			return true;
		} else if(player.getCurrentEquippedItem().getItem() == Items.bucket)
		{
			if(!getBundle().getEntity().getWorldObj().isRemote)
			{
				long curTick = getBundle().getEntity().getWorldObj().getWorldTime();
				if(curTick - lastEmptyTick < 20)
				{
					numEmptyEvents++;
				}else
				{
					numEmptyEvents = 1;
				}
				lastEmptyTick = curTick;
				
				if(numEmptyEvents < 2)
				{
					if(network.fluidTypeLocked)
					{
						network.setFluidTypeLocked(false);
						numEmptyEvents = 0;
						ChatComponentText c = new ChatComponentText(Lang.localize("itemLiquidPiping.unlockedType"));
						player.addChatMessage(c);
					}
				} else if(network != null)
				{
					network.setFluidType(null);
					numEmptyEvents = 0;
				}
			}
			return true;
		} else {
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(player.getCurrentEquippedItem());
			if(fluid != null)
			{
				if(!getBundle().getEntity().getWorldObj().isRemote)
				{
					if(network != null && (network.getFluidType() == null || network.getTotalVolume() < 500 || LiquidPipingNetwork.areFluidsCompatable(getFluidType(), fluid)))
					{
						network.setFluidType(fluid);
						network.setFluidTypeLocked(true);
						ChatComponentText c = new ChatComponentText(Lang.localize("itemLiquidPiping.lockedType") + " " + FluidRegistry.getFluidName(fluid));
						player.addChatMessage(c);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	void setFluidTypeLocked(boolean fluidTypeLocked)
	{
		if(fluidTypeLocked == this.fluidTypeLocked)
		{
			return;
		}
		this.fluidTypeLocked = fluidTypeLocked;
		stateDirty = true;
	}
	
	private void setFluidTypeOnNetwork(AbstractTankPiping pipe, FluidStack type)
	{
		AbstractPipingNetwork<?,?> n = pipe.getNetwork();
		if(n != null)
		{
			AbstractTankPipingNetwork<?> network = (AbstractTankPipingNetwork<?>) n;
			network.setFluidType(type);
		}
	}
	
	protected abstract boolean canJoinNeighbor(ILiquidPiping n);
	
	public abstract AbstractTankPipingNetwork<? extends AbstractTankPiping> getTankNetwork();
	
	public void setFluidType(FluidStack liquidType)
	{
		if(tank.getFluid() != null && tank.getFluid().isFluidEqual(liquidType))
		{
			return;
		}
		if(liquidType != null)
		{
			liquidType = liquidType.copy();
		} else if(tank.getFluid() == null)
		{
			return;
		}
		tank.setLiquid(liquidType);
		stateDirty = true;
	}
	
	public PipingTank getTank()
	{
		return tank;
	}
	
	public FluidStack getFluidType()
	{
		FluidStack result = null;
		if(getTankNetwork() != null)
		{
			result = getTankNetwork().getFluidType();
		}
		if(result == null)
		{
			result = tank.getFluid();
		}
		return result;
	}
	
	protected abstract void updateTank();
	
	@Override
	public void readFromNBT(NBTTagCompound nbtRoot)
	{
		super.readFromNBT(nbtRoot);
		updateTank();
		if(nbtRoot.hasKey("tank"))
		{
			FluidStack liquid = FluidStack.loadFluidStackFromNBT(nbtRoot.getCompoundTag("tank"));
			tank.setLiquid(liquid);
		} else {
			tank.setLiquid(null);
		}
		fluidTypeLocked = nbtRoot.getBoolean("fluidLocked");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtRoot)
	{
		super.writeToNBT(nbtRoot);
		FluidStack ft = getFluidType();
		if(PipingUtil.isFluidValid(ft))
		{
			updateTank();
			ft = ft.copy();
			ft.amount = tank.getFluidAmount();
			nbtRoot.setTag("tank", ft.writeToNBT(new NBTTagCompound()));
		}
		nbtRoot.setBoolean("fluidLocked", fluidTypeLocked);
	}
}
