package com.madpcgaming.citytech.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TemplateSlot extends Slot
{
	int slotIndex;
	
	public TemplateSlot(IInventory inventory, int slotIndex, int x, int y)
	{
		super(inventory, slotIndex, x, y);
		this.slotIndex = slotIndex;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack decrStackSize(int stack)
	{
		return null;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		if(stack != null)
		{
			stack.stackSize = 0;
		}
		inventory.setInventorySlotContents(slotIndex, stack);
		onSlotChanged();
	}
}
