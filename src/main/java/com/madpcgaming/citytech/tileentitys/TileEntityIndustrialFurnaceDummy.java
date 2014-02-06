package com.madpcgaming.citytech.tileentitys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityIndustrialFurnaceDummy extends TileEntity implements
		ISidedInventory
{
	TileEntityIndustrialFurnaceCore tileEntityCore;
	int coreX;
	int coreY;
	int coreZ;

	public TileEntityIndustrialFurnaceDummy()
	{
	}
	
	public void setCore(TileEntityIndustrialFurnaceCore core)
	{
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileEntityCore = core;
	}
	
	public TileEntityIndustrialFurnaceCore getCore()
	{
		if(tileEntityCore == null)
			tileEntityCore = (TileEntityIndustrialFurnaceCore)worldObj.getTileEntity(coreX, coreY, coreZ);
		
		return tileEntityCore;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
	}

	@Override
	public int getSizeInventory()
	{
		return tileEntityCore.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return tileEntityCore.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		return tileEntityCore.decrStackSize(slot, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return tileEntityCore.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		tileEntityCore.setInventorySlotContents(slot, itemStack);
	}

	@Override
	public String getInventoryName()
	{
		return tileEntityCore.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return tileEntityCore.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return tileEntityCore.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double)xCoord +0.5f, (double)yCoord + 0.5f, (double)zCoord + 0.5f) <= 64.0;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack)
	{
		return tileEntityCore.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int par1)
	{
		return tileEntityCore.getAccessibleSlotsFromSide(par1);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int count)
	{
		return tileEntityCore.canInsertItem(slot, itemStack, count);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int count)
	{
		return tileEntityCore.canExtractItem(slot, itemStack, count);
	}

}
