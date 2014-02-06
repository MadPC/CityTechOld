	package com.madpcgaming.citytech.tileentitys;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.blocks.multiblocks.IndustrialFurnaceCore;
import com.madpcgaming.citytech.lib.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityIndustrialFurnaceCore extends TileEntity implements
		ISidedInventory
{

	private static final int[]	sidedSlotSides		= new int[] { 0 };
	private static final int[]	sidedSlotBottom		= new int[] { 2, 1 };
	private static final int[]	sidedSlotTop		= new int[] { 1 };

	private ItemStack[]			furnaceItems		= new ItemStack[3];
	public int					furnaceBurnTime		= 0;
	public int					currentItemBurnTime	= 0;
	public int					furnaceCookTime		= 0;

	private boolean				isValidMultiBlock	= false;

	public TileEntityIndustrialFurnaceCore()
	{
	}

	public boolean getIsValid()
	{
		return isValidMultiBlock;
	}

	public void invalidateMultiBlock()
	{
		isValidMultiBlock = false;

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord , zCoord);
		metadata = metadata & IndustrialFurnaceCore.MASK_DIR;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord , zCoord, metadata, 2);

		furnaceBurnTime = 0;
		currentItemBurnTime = 0;
		furnaceCookTime = 0;

		revertDummies();
	}

	public boolean checkIfProperlyFormed()
	{
		int dir = (getBlockMetadata() & IndustrialFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == IndustrialFurnaceCore.META_DIR_NORTH || dir == IndustrialFurnaceCore.META_DIR_WEST) ? 1
				: -1);
		boolean forwardZ = ((dir == IndustrialFurnaceCore.META_DIR_NORTH) || (dir == IndustrialFurnaceCore.META_DIR_SOUTH));

		for (int horiz = -1; horiz <= 1; horiz++) {
			for (int vert = -1; vert <= 1; vert++) {
				for (int depth = 0; depth <= 2; depth++) {
					int x = xCoord
							+ (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord  + vert;
					int z = zCoord
							+ (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x,y,z);

					if (horiz == 0 && vert == 0) {
						if (depth == 0)
							continue;

						if (depth == 1) {
							if (blockId != Blocks.air)
								return false;
							else
								continue;
						}
					}

					if (blockId != Blocks.iron_block)
						return false;
				}
			}
		}

		return true;
	}

	public void convertDummies()
	{
		int dir = (getBlockMetadata() & IndustrialFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == IndustrialFurnaceCore.META_DIR_NORTH || dir == IndustrialFurnaceCore.META_DIR_WEST) ? 1
				: -1);
		boolean forwardZ = ((dir == IndustrialFurnaceCore.META_DIR_NORTH) || (dir == IndustrialFurnaceCore.META_DIR_SOUTH));

		for (int horiz = -1; horiz <= 1; horiz++) {
			for (int vert = -1; vert <= 1; vert++) {
				for (int depth = 0; depth <= 2; depth++) {
					int x = xCoord
							+ (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord  + vert;
					int z = zCoord
							+ (forwardZ ? (depth * depthMultiplier) : horiz);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;

					worldObj.setBlock(x, y, z, ModBlocks.IndustrialFurnaceDummy);
					worldObj.markBlockForUpdate(x, y, z);
					TileEntityIndustrialFurnaceDummy dummyTE = (TileEntityIndustrialFurnaceDummy) worldObj
							.getTileEntity(x, y, z);
					dummyTE.setCore(this);
				}
			}
		}

		isValidMultiBlock = true;
	}

	private void revertDummies()
	{
		int dir = (getBlockMetadata() & IndustrialFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == IndustrialFurnaceCore.META_DIR_NORTH || dir == IndustrialFurnaceCore.META_DIR_WEST) ? 1
				: -1);
		boolean forwardZ = ((dir == IndustrialFurnaceCore.META_DIR_NORTH) || (dir == IndustrialFurnaceCore.META_DIR_SOUTH));

		for (int horiz = -1; horiz <= 1; horiz++) {
			for (int vert = -1; vert <= 1; vert++) {
				for (int depth = 0; depth <= 2; depth++) {
					int x = xCoord
							+ (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord  + vert;
					int z = zCoord
							+ (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x, y, z);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;

					if (blockId != ModBlocks.IndustrialFurnaceDummy)
						continue;

					worldObj.setBlock(x, y, z, Blocks.iron_block);
					worldObj.markBlockForUpdate(x, y, z);
				}
			}
		}

		isValidMultiBlock = false;
	}

	@Override
	public void updateEntity()
	{
		if (!isValidMultiBlock)
			return;

		boolean flag = furnaceBurnTime > 0;
		boolean flag1 = false;
		int metadata = getBlockMetadata();
		int isActive = (metadata >> 3);

		if (flag)
			furnaceBurnTime--;

		if (!this.worldObj.isRemote) {
			if (furnaceBurnTime == 0 && canSmelt()) {
				currentItemBurnTime = furnaceBurnTime = TileEntityFurnace
						.getItemBurnTime(furnaceItems[1]);

				if (flag) {
					flag1 = true;

					if (furnaceItems[1] != null) {
						furnaceItems[1].stackSize--;

						if (furnaceItems[1].stackSize == 0)
							furnaceItems[1] = furnaceItems[1].getItem()
									.getContainerItem(furnaceItems[1]);
					}
				}
			}

			if (isBurning() && canSmelt()) {
				furnaceCookTime++;

				if (furnaceCookTime == 100) {
					furnaceCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				furnaceCookTime = 0;
			}

			if (isActive == 0 && flag) {
				flag1 = true;
				metadata = getBlockMetadata();
				isActive = 1;
				metadata = (isActive << 3)
						| (metadata & IndustrialFurnaceCore.META_ISACTIVE);

				worldObj.setBlockMetadataWithNotify(xCoord, yCoord , zCoord,
						metadata, 2);
			}
		}

		if (flag1)
			markDirty();
	}

	@Override
	public int getSizeInventory()
	{
		return furnaceItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return furnaceItems[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		if (this.furnaceItems[slot] != null) {
			ItemStack itemStack;

			itemStack = furnaceItems[slot].splitStack(count);

			if (furnaceItems[slot].stackSize <= 0)
				furnaceItems[slot] = null;

			return itemStack;
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (furnaceItems[slot] != null) {
			ItemStack stack = furnaceItems[slot];
			furnaceItems[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		furnaceItems[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
			itemStack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInventoryName()
	{
		return Strings.GUI_INDUSTRIAL_FURNACE;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return worldObj.getTileEntity(xCoord, yCoord , zCoord) != this ? false
				: entityPlayer.getDistanceSq((double) xCoord + 0.5,
						(double) yCoord  + 0.5, (double) zCoord + 0.5) <= 64.0;
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
		return slot == 2 ? false : (slot == 1 ? TileEntityFurnace
				.isItemFuel(itemStack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int par1)
	{
		return par1 == 0 ? sidedSlotBottom : (par1 == 1 ? sidedSlotTop
				: sidedSlotSides);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int count)
	{
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int count)
	{
		return count != 0 || slot != 1
				|| itemStack.getItem() == Items.bucket;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		int md = tagCompound.getInteger("BlockMeta");
		isValidMultiBlock = tagCompound.getBoolean("isValidMultiBlock");

		NBTTagList itemsTag = tagCompound.getTagList("Items", md);
		furnaceItems = new ItemStack[getSizeInventory()];

		for (int i = 0; i < itemsTag.tagCount(); i++) {
			NBTTagCompound slotTag = (NBTTagCompound) itemsTag.getCompoundTagAt(i);
			byte slot = slotTag.getByte("slot");

			if (slot >= 0 && slot < furnaceItems.length)
				furnaceItems[slot] = ItemStack.loadItemStackFromNBT(slotTag);
		}

		furnaceBurnTime = tagCompound.getShort("BurnTime");
		furnaceCookTime = tagCompound.getShort("CookTime");
		currentItemBurnTime = TileEntityFurnace
				.getItemBurnTime(furnaceItems[1]);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setBoolean("isValidMultiBlock", isValidMultiBlock);
		System.out.println("Is Valid? " + (isValidMultiBlock ? "Yes" : "NO"));

		tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
		tagCompound.setShort("CookTime", (short) furnaceCookTime);
		NBTTagList itemsList = new NBTTagList();

		for (int i = 0; i < furnaceItems.length; i++) {
			if (furnaceItems[i] != null) {
				NBTTagCompound slotTag = new NBTTagCompound();
				slotTag.setByte("Slot", (byte) i);
				furnaceItems[i].writeToNBT(slotTag);
				itemsList.appendTag(slotTag);
			}

			tagCompound.setTag("Items", itemsList);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int scaleVal)
	{
		return furnaceCookTime * scaleVal / 100;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scaleVal)
	{
		if (currentItemBurnTime == 0)
			currentItemBurnTime = 100;

		return furnaceBurnTime * scaleVal / currentItemBurnTime;
	}

	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	private boolean canSmelt()
	{
		if (furnaceItems[0] == null)
			return false;
		else {
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					furnaceItems[0]);
			if (itemStack == null)
				return false;
			if (furnaceItems[2] == null)
				return true;
			if (!furnaceItems[2].isItemEqual(itemStack))
				return false;

			int resultingStackSize = furnaceItems[2].stackSize
					+ itemStack.stackSize;
			return (resultingStackSize <= getInventoryStackLimit() && resultingStackSize <= itemStack
					.getMaxStackSize());
		}
	}

	public void smeltItem()
	{
		if (canSmelt()) {
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(
					furnaceItems[0]);

			if (furnaceItems[2] == null)
				furnaceItems[2] = itemStack.copy();
			else if (furnaceItems[2].isItemEqual(itemStack))
				furnaceItems[2].stackSize += itemStack.stackSize;

			furnaceItems[0].stackSize--;
			if (furnaceItems[0].stackSize <= 0)
				furnaceItems[0] = null;
		}
	}
}
