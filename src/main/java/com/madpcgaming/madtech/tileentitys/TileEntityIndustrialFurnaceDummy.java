package com.madpcgaming.madtech.tileentitys;

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
		coreX = core.field_145851_c;
		coreY = core.field_145848_d;
		coreZ = core.field_145849_e;
		tileEntityCore = core;
	}
	
	public TileEntityIndustrialFurnaceCore getCore()
	{
		if(tileEntityCore == null)
			tileEntityCore = (TileEntityIndustrialFurnaceCore)field_145850_b.func_147438_o(coreX, coreY, coreZ);
		
		return tileEntityCore;
	}
	
	@Override
	public void func_145841_b(NBTTagCompound tagCompound)
	{
		super.func_145841_b(tagCompound);
		
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
	public String func_145825_b()
	{
		return tileEntityCore.func_145825_b();
	}

	@Override
	public boolean func_145818_k_()
	{
		return tileEntityCore.func_145818_k_();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return tileEntityCore.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return field_145850_b.func_147438_o(field_145851_c, field_145848_d, field_145849_e) != this ? false : entityPlayer.getDistanceSq((double)field_145851_c +0.5f, (double)field_145848_d + 0.5f, (double)field_145849_e + 0.5f) <= 64.0;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
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
