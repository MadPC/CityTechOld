package com.madpcgaming.mt.tileentitys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import com.madpcgaming.mt.core.interfaces.IMachine;
import com.madpcgaming.mt.energy.EnergyHandler;
import com.madpcgaming.mt.energy.interfaces.IEnergyConductor;

public class TileElectrolyser extends TileEntity implements IFluidHandler, IEnergyConductor, IInventory, IMachine
{
	
	public static int		LIQUID_PER_INPUT	= FluidContainerRegistry.BUCKET_VOLUME * 3;
	public static int		LIQUID_PER_OUTPUT	= FluidContainerRegistry.BUCKET_VOLUME * 6;
	
	private ItemStack[]		inv;
	
	/** Slot 0 */
	public FluidTank		ingredient			= new FluidTank(LIQUID_PER_INPUT);
	
	/** Slot 1 */
	public FluidTank		result1				= new FluidTank(LIQUID_PER_OUTPUT);
	
	/** Slot 2 */
	public FluidTank		result2				= new FluidTank(LIQUID_PER_OUTPUT);
	private EnergyHandler	powerProvider;
	
	private boolean active;
	
	public TileElectrolyser()
	{
		powerProvider = new EnergyHandler(this.xCoord, this.yCoord, this.zCoord);
		this.inv = new ItemStack[3];
		active = false;
	}
	
	// STANDART BLOCK STUFF
	
	// INTERNAL CHECKS
	@Override
	public boolean isActive()
	{
		return active;
	}
	
	@Override
	public boolean manageLiquids()
	{
		return true;
	}
	
	@Override
	public boolean manageSolids()
	{
		return true;
	}
	
	// INVENTORY STUFF
	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		if (i >= 0 && i < inv.length)
			return inv[i];
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inv[i] != null)
		{
			ItemStack itemstack;
			
			if (this.inv[i].stackSize <= j)
			{
				itemstack = this.inv[i];
				this.inv[i] = null;
				return itemstack;
			} else
			{
				itemstack = this.inv[i].splitStack(j);
				
				if (this.inv[i].stackSize == 0)
				{
					this.inv[i] = null;
				}
				
				return itemstack;
			}
		} else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (i >= 0 && i < inv.length)
		{
			ItemStack it = inv[i];
			inv[i] = null;
			return it;
		}
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if (i >= 0 && i < inv.length)
			inv[i] = itemstack;
	}
	
	@Override
	public String getInvName()
	{
		return "electronical.splitter";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this) && (entityplayer.getDistanceSq(xCoord, yCoord, zCoord) <=64);
	}
	
	@Override
	public void openChest()
	{
	}
	
	@Override
	public void closeChest()
	{
	}
	
	// ENERGY SPECIFIC STUFF
	@Override
	public float transferTo(ForgeDirection d, float power)
	{
		return this.powerProvider.placeEnegry(d, power);
	}
	
	/**
	 * NOP
	 */
	@Override
	public void requestFrom(ForgeDirection d, float power)
	{
		
	}
	
	@Override
	public float getEnergyLevel()
	{
		return this.powerProvider.getEnergyLevel();
	}
	
	// Fluid part
	
	/**
	 * Returns information about the possible tanks for the chosen direction:
	 * Up/down: ingredient slot 
	 * Sides: result1 and result2 slots. 
	 * Unknown: ingredient, result1 and result2 slots.
	 * 
	 * @return A array of possible Tanks for the chosen direction.
	 */
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		switch (from)
		{
			case UP:
			case DOWN:
				return new FluidTankInfo[] { new FluidTankInfo(this.ingredient) };
			case EAST:
			case NORTH:
			case WEST:
			case SOUTH:
				return new FluidTankInfo[] { new FluidTankInfo(this.result1), new FluidTankInfo(this.result2) };
			default:
				return new FluidTankInfo[] { new FluidTankInfo(this.ingredient), new FluidTankInfo(this.result1),
						new FluidTankInfo(this.result2) };
		}
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (from == ForgeDirection.DOWN && from == ForgeDirection.UP)
			return this.fill(0, resource, doFill);
		return 0;
	}
	
	public int fill(int tankIndex, FluidStack resource, boolean doFill)
	{
		if (tankIndex != 0)
			return 0;
		return this.ingredient.fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(from, resource.amount, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if (from != ForgeDirection.DOWN && from != ForgeDirection.UP)
		{
			if ((from.ordinal() % 2) == 0)
				return drain(1, maxDrain, doDrain);
			else
				return drain(2, maxDrain, doDrain);
		}
		return null;
	}
	
	/**
	 * 
	 * @param tankIndex
	 *            1 or 2 is the respective result slots. should not be 0.
	 * @param maxDrain
	 *            Maximum amount of fluid to drain.
	 * @param doDrain
	 *            If false, the drain will only be simulated
	 * @return Amount of fluid that was removed from the tank.
	 */
	public FluidStack drain(int tankIndex, int maxDrain, boolean doDrain)
	{
		
		if (tankIndex == 1)
			return this.result1.drain(maxDrain, doDrain);
		else if (tankIndex == 2)
			return this.result2.drain(maxDrain, doDrain);
		else
			return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (from == ForgeDirection.DOWN || from == ForgeDirection.UP)
			if (this.ingredient.getFluid() == null || this.ingredient.getFluid().getFluid().getID() == fluid.getID())
				return true;
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		if (from != ForgeDirection.DOWN && from != ForgeDirection.UP)
			return true;
		return false;
	}
	
	// Fluid Helper methods
	
	public IFluidTank[] getTank(int slot)
	{
		if (slot == 1)
			return new IFluidTank[] { result1 };
		else if (slot == 2)
			return new IFluidTank[] { result2 };
		else
			return new IFluidTank[] { ingredient };
		
	}
	
	// STANDART VANILLA THINGS
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		this.powerProvider.updatePosition(this.xCoord, this.yCoord, this.zCoord);
		this.powerProvider.readFromNBT(par1);
		
		//own stuff
		NBTTagCompound fluids = par1.getCompoundTag("fluids");
		
		this.ingredient.readFromNBT(fluids.getCompoundTag("Input"));
		this.result1.readFromNBT(fluids.getCompoundTag("Output1"));
		this.result2.readFromNBT(fluids.getCompoundTag("Output2"));
		
		NBTTagCompound invtag = par1.getCompoundTag("Inventory");
		for (int i = 0; i < this.inv.length; i++)
		{
			NBTTagCompound itemtag = invtag.getCompoundTag("Slot" + i);
			inv[i] = ItemStack.loadItemStackFromNBT(itemtag);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		
		//power
		this.powerProvider.writeToNBT(par1);
		
		//own stuff
		//fluids
		NBTTagCompound fluids = new NBTTagCompound();
		
		NBTTagCompound ingredient = new NBTTagCompound();
		NBTTagCompound res1 = new NBTTagCompound();
		NBTTagCompound res2 = new NBTTagCompound();
		this.ingredient.writeToNBT(ingredient);
		this.result1.writeToNBT(res1);
		this.result2.writeToNBT(res2);
		
		fluids.setCompoundTag("Input", ingredient);
		fluids.setCompoundTag("Output1", res1);
		fluids.setCompoundTag("Output2", res2);
		
		par1.setCompoundTag("Fluids", fluids);
		
		//inventory
		NBTTagCompound invtag = new NBTTagCompound();
		for (int i = 0; i < this.inv.length; i++)
		{
			ItemStack item = inv[i];
			NBTTagCompound itemtag = new NBTTagCompound();
			item.writeToNBT(itemtag);
			invtag.setCompoundTag("Slot" + i, itemtag);
		}
		
		par1.setCompoundTag("Inventory", invtag);
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if (itemstack.isStackable() != true)
			return true;
		return false;
	}
	
}
