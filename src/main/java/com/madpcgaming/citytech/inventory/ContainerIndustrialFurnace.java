package com.madpcgaming.citytech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;

public class ContainerIndustrialFurnace extends Container
{
	private TileEntityIndustrialFurnaceCore tileEntity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;
	

	public ContainerIndustrialFurnace(InventoryPlayer playerInventory, TileEntityIndustrialFurnaceCore tileEntity)
	{
		this.tileEntity = tileEntity;
		
		//input
		addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		
		//fuel
		addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
		
		//output
		addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, 2, 116, 35));
		
		bindPlayerInventory(playerInventory);
		
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.tileEntity.furnaceCookTime);
		crafting.sendProgressBarUpdate(this, 1, this.tileEntity.furnaceBurnTime);
		crafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if(this.lastCookTime != this.tileEntity.furnaceCookTime)
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.furnaceCookTime);
			if(this.lastBurnTime != this.tileEntity.furnaceBurnTime)
				icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.furnaceBurnTime);
			if(this.lastItemBurnTime != this.tileEntity.currentItemBurnTime);
				icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
		}
		
		this.lastCookTime = this.tileEntity.furnaceCookTime;
		this.lastBurnTime = this.tileEntity.furnaceBurnTime;
		this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
	}
	
	@Override
	public void updateProgressBar(int par1, int par2)
	{
		if(par1 == 0)
		{
			this.tileEntity.furnaceCookTime = par2;
		}
		
		if(par1 == 1)
		{
			this.tileEntity.furnaceBurnTime = par2;
		}
		
		if(par1 == 2)
		{
			this.tileEntity.currentItemBurnTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return tileEntity.isUseableByPlayer(entityPlayer);
	}
	
	private void bindPlayerInventory(InventoryPlayer playerInventory)
	{
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 9; x++)
				addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
		
		for(int x = 0; x < 9; x++)
			addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = null;
		Slot slotObject = (Slot)inventorySlots.get(slot);
		if(slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			if(slot < 3)
			{
				if(!this.mergeItemStack(stackInSlot, 3, 39, true))
					return null;
			}
			else if(!this.mergeItemStack(stackInSlot, 0, 3, false))
				return null;
			
			if(stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();
			
			if(stackInSlot.stackSize == stack.stackSize)
				return null;
			
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

}
