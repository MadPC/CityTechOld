package com.madpcgaming.citytech.piping.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import cofh.api.transport.IItemConduit;

public class ItemUtil
{

	public static int doInsertItem(Object into, ItemStack item,
			ForgeDirection side)
	{
		if (into == null || item == null) {
			return 0;
		}
		if (into instanceof ISidedInventory) {
			return ItemUtil.doInsertItem((ISidedInventory) into, item, side);
		}
		else if (into instanceof IInventory) {
			return ItemUtil.doInsertItem(getInventory((IInventory) into), item);
		}
		else if (into instanceof IItemConduit) {
			return ItemUtil.doInsertItem((IItemConduit) into, item, side);
		}
		else if (into instanceof IPipeTile) {
			return ((IPipeTile) into).injectItem(item, true, side);
		}
		return 0;
	}

	public static int doInsertItem(IItemConduit con, ItemStack item,
			ForgeDirection inventorySide)
	{
		int startedWith = item.stackSize;
		ItemStack remaining = con.insertItem(inventorySide, item);
		if (remaining == null) {
			return startedWith;
		}
		return startedWith - remaining.stackSize;
	}

	public static int doInsertItem(ISidedInventory sidedInv, ItemStack item,
			ForgeDirection inventorySide)
	{

		if (inventorySide == null) {
			inventorySide = ForgeDirection.UNKNOWN;
		}

		int numInserted = 0;
		int numToInsert = item.stackSize;
		int[] slots = sidedInv.getAccessibleSlotsFromSide(inventorySide
				.ordinal());
		for (int i = 0; i < slots.length && numToInsert > 0; i++) {
			int slot = slots[i];
			if (!isStackFull(sidedInv.getStackInSlot(slot))) {
				if (sidedInv.canInsertItem(slot, item, inventorySide.ordinal())) {
					ItemStack contents = sidedInv.getStackInSlot(slot);
					ItemStack toInsert = item.copy();
					toInsert.stackSize = Math.min(toInsert.stackSize,
							sidedInv.getInventoryStackLimit());
					toInsert.stackSize = Math.min(toInsert.stackSize,
							numToInsert);
					int inserted = 0;
					if (contents == null) {
						inserted = toInsert.stackSize;
					}
					else {
						if (contents.isItemEqual(item)
								&& ItemStack.areItemStackTagsEqual(contents,
										item)) {
							int space = sidedInv.getInventoryStackLimit()
									- contents.stackSize;
							space = Math.min(space, contents.getMaxStackSize()
									- contents.stackSize);
							inserted += Math.min(space, toInsert.stackSize);
							toInsert.stackSize = contents.stackSize + inserted;
						}
						else {
							toInsert.stackSize = 0;
						}
					}

					if (inserted > 0) {
						numInserted += inserted;
						numToInsert -= inserted;
						sidedInv.setInventorySlotContents(slot, toInsert);
					}
				}
			}
		}
		if (numInserted > 0) {
			sidedInv.markDirty();
		}
		return numInserted;
	}

	public static int doInsertItem(IInventory inv, ItemStack item)
	{
		int numInserted = 0;
		int numToInsert = item.stackSize;
		for (int slot = 0; slot < inv.getSizeInventory() && numToInsert > 0; slot++) {
			ItemStack contents = inv.getStackInSlot(slot);
			if (!isStackFull(contents)) {
				ItemStack toInsert = item.copy();
				toInsert.stackSize = Math.min(toInsert.stackSize,
						inv.getInventoryStackLimit());
				toInsert.stackSize = Math.min(toInsert.stackSize, numToInsert);
				int inserted = 0;
				if (contents == null) {
					inserted = toInsert.stackSize;
				}
				else {
					if (contents.isItemEqual(item)
							&& ItemStack.areItemStackTagsEqual(contents, item)) {
						int space = inv.getInventoryStackLimit()
								- contents.stackSize;
						space = Math.min(space, contents.getMaxStackSize()
								- contents.stackSize);
						inserted += Math.min(space, toInsert.stackSize);
						toInsert.stackSize = contents.stackSize + inserted;
					}
					else {
						toInsert.stackSize = 0;
					}
				}

				if (inserted > 0) {
					numInserted += inserted;
					numToInsert -= inserted;
					inv.setInventorySlotContents(slot, toInsert);
				}
			}
		}
		if (numInserted > 0) {
			inv.markDirty();
		}
		return numInserted;
	}

	public static boolean isStackFull(ItemStack contents)
	{
		if (contents == null) {
			return false;
		}
		return contents.stackSize >= contents.getMaxStackSize();
	}

	public static IInventory getInventory(IInventory inv)
	{
		if (inv instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) inv;
			TileEntityChest neighbor = null;
			if (chest.adjacentChestXNeg != null) {
				neighbor = chest.adjacentChestXNeg;
			}
			else if (chest.adjacentChestXPos != null) {
				neighbor = chest.adjacentChestXPos;
			}
			else if (chest.adjacentChestZNeg != null) {
				neighbor = chest.adjacentChestZNeg;
			}
			else if (chest.adjacentChestZPos != null) {
				neighbor = chest.adjacentChestZPos;
			}
			if (neighbor != null) {
				return new InventoryLargeChest("", inv, neighbor);
			}
			return inv;
		}
		return inv;
	}

}