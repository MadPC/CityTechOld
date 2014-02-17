package com.madpcgaming.citytech.piping.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.gui.TemplateSlot;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.item.IItemPiping;
import com.madpcgaming.citytech.piping.item.ItemFilter;

public class ExternalConnectionContainer extends Container
{
	private InventoryPlayer playerInv;
	private IPipingBundle bundle;
	private ForgeDirection dir;
	private IItemPiping itemPiping;
	private ItemFilter inputFilter;
	private ItemFilter outputFilter;
	private List<Point> slotLocations = new ArrayList<Point>();
	
	public ExternalConnectionContainer(InventoryPlayer playerInv, IPipingBundle bundle, ForgeDirection dir)
	{
		this.playerInv = playerInv;
		this.bundle = bundle;
		this.dir = dir;
		
		itemPiping = bundle.getPiping(IItemPiping.class);
		if(itemPiping != null)
		{
			addFilterSlots(dir);
		}
		
		int topY = 113;
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				int x = 8 + j * 18;
				int y = topY + i * 18;
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, x, y));
				slotLocations.add(new Point(x, y));
			}
		}
		
		int y = 171;
		for(int i = 0; i < 9; ++i)
		{
			int x = 8 + i * 18;
			addSlotToContainer(new Slot(playerInv, i, x, y));
			slotLocations.add(new Point(x, y));
		}
	}
	
	private void addFilterSlots(ForgeDirection dir)
	{
		boolean isAdvanced = itemPiping.getMetaData() == 1;
		inputFilter = itemPiping.getInputFilter(dir);
		
		int topY = 67;
		int leftX = 16;
		int index = 0;
		
		for(int row = 0; row < 2; ++row)
		{
			for(int col = 0; col < 5; ++col)
			{
				int x = leftX + col * 18;
				int y = topY + row * 18;
				if(!isAdvanced && row == 1)
				{
					x = -30000;
					y = -30000;
				}
				addSlotToContainer(new TemplateSlot(inputFilter, index, x, y));
				slotLocations.add(new Point(x, y));
				index++;
			}
		}
		
		outputFilter = itemPiping.getOutputFilter(dir);
		
		leftX = 16;
		index = 0;
		for(int row = 0; row < 2; ++row)
		{
			for(int col = 0; col < 5; ++col)
			{
				int x = leftX + col * 18;
				int y = topY + row * 18;
				if(!isAdvanced && row == 1)
				{
					x = -30000;
					y = -30000;
				}
				addSlotToContainer(new TemplateSlot(outputFilter, index, x, y));
				slotLocations.add(new Point(x,y));
				index++;
			}
		}
	}
	
	public void setInputSlotsVisible(boolean visible)
	{
		if(inputFilter == null)
		{
			return;
		}
		int startIndex = 0;
		int endIndex = inputFilter.getSizeInventory();
		setSlotsVisible(visible, startIndex, endIndex);
	}
	
	public void setOutputSlotsVisible(boolean visible)
	{
		if(outputFilter == null)
		{
			return;
		}
		int startIndex = inputFilter.getSizeInventory();
		int endIndex = startIndex + outputFilter.getSizeInventory();
		setSlotsVisible(visible, startIndex, endIndex);
	}
	
	public void setInventorySlotsVisible(boolean visible)
	{
		int startIndex;
		if(inputFilter == null || outputFilter == null)
		{
			startIndex = 0;
		} else {
			startIndex = inputFilter.getSizeInventory() + outputFilter.getSizeInventory();
		}
		int endIndex = inventorySlots.size();
		setSlotsVisible(visible, startIndex, endIndex);
	}
	
	private void setSlotsVisible(boolean visible, int startIndex, int endIndex)
	{
		for(int i = startIndex; i < endIndex; i++)
		{
			Slot s = (Slot) inventorySlots.get(i);
			if(visible)
			{
				s.xDisplayPosition = slotLocations.get(i).x;
				s.yDisplayPosition = slotLocations.get(i).y;
			} else {
				s.xDisplayPosition = -30000;
				s.yDisplayPosition = -30000;
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer entityPlayer)
	{
		if(entityPlayer.worldObj != null)
		{
			if(itemPiping != null && par1 < 20)
			{
				itemPiping.setInputFilter(dir, inputFilter);
				itemPiping.setOutputFilter(dir, outputFilter);
				if(entityPlayer.worldObj.isRemote)
				{
					entityPlayer.worldObj.markBlockForUpdate(bundle.getEntity().xCoord, bundle.getEntity().yCoord, bundle.getEntity().zCoord);
				}
			}
		}
		try {
			return super.slotClick(par1, par2, par3, entityPlayer);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex)
	{
		return null;
	}

}
