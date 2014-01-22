package com.madpcgaming.madtech.tileentitys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.madtech.energy.EnergyHandler;
import com.madpcgaming.madtech.energy.interfaces.IEnergyConductor;
import com.madpcgaming.madtech.lib.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileSimpleEFurnace extends TileEntity implements ISidedInventory, IEnergyConductor
{

	private ItemStack[] inv;
	public int	furnaceCookTime;
	public int	furnaceBurnTime;
	public int currentItemBurnTime;
	private EnergyHandler energy;
	
	public TileSimpleEFurnace()
	{
		super();
		inv = new ItemStack[this.getSizeInventory()];
		furnaceCookTime = 0;
		furnaceBurnTime = 0;
		currentItemBurnTime = 0;
		
		energy = new EnergyHandler(this.field_145851_c, this.field_145848_d, this.field_145849_e);
	}
	
	//Update
	@Override
	public void func_145845_h()
	{
		
		//Furnace updating
		boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0)
        {
            --this.furnaceBurnTime;
        }

        if (!this.field_145850_b.isRemote)
        {
        	this.energy.update(field_145850_b);
        	
        	
            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
            	if (energy.getEnergyLevel() >= 50.0f)
                {
             	   this.currentItemBurnTime = this.furnaceBurnTime = 6400;
             	   energy.placeEnegry(ForgeDirection.UNKNOWN, -50);
                }
            	else
            	{
            		this.energy.requestEnergy(50 - this.energy.getEnergyLevel());
            	}
            	
                if (this.furnaceBurnTime > 0)
                {
                    flag1 = true;
                }
            }

            if (this.isBurning() && this.canSmelt())
            {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime == 200)
                {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                flag1 = true;
                //BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1)
        {
            this.onInventoryChanged();
        }
	}
	
	//Inventory Stuff
	
	@Override
	public int getSizeInventory()
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inv[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inv[i] != null) {
			ItemStack itemStack;

			itemStack = inv[i].splitStack(j);

			if (inv[i].stackSize <= 0)
				inv[i] = null;

			return itemStack;
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (inv[i] != null) {
			ItemStack stack = inv[i];
			inv[i] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack)
	{
		inv[i] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
			itemStack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String func_145825_b()
	{
		return "MadTech.container." + Strings.SIMPLE_SE_FURNACE_NAME;
	}

	@Override
	public boolean func_145818_k_()
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
		return field_145850_b.func_147438_o(field_145851_c, field_145848_d, field_145849_e) != this ? false
				: entityPlayer.getDistanceSq((double) field_145851_c + 0.5,
						(double) field_145848_d + 0.5, (double) field_145849_e + 0.5) <= 64.0;
	}

	@Override
	public void openChest()	{}

	@Override
	public void closeChest() {}
	
	//Hopper stuff
	//For now disabled.
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}
	
	
	//Furnace Stuff
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
	
	private boolean canSmelt()
	{
		if (inv[0] == null)
			return false;
		else {
			ItemStack itemStack = FurnaceRecipes.smelting().func_151395_a(inv[0]);
			if (itemStack == null)
				return false;
			if (inv[1] == null)
				return true;
			if (!inv[1].isItemEqual(itemStack))
				return false;

			int resultingStackSize = inv[1].stackSize + itemStack.stackSize;
			return (resultingStackSize <= getInventoryStackLimit() && resultingStackSize <= itemStack.getMaxStackSize());
		}
	}

	public void smeltItem()
	{
		if (canSmelt()) 
		{
			ItemStack itemStack = FurnaceRecipes.smelting().func_151395_a(inv[0]);

			if (inv[1] == null)
				inv[1] = itemStack.copy();
			else if (inv[1].isItemEqual(itemStack))
				inv[1].stackSize += itemStack.stackSize;

			inv[0].stackSize--;
			if (inv[0].stackSize <= 0)
				inv[0] = null;
		}
	}
	
	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	
	//Saving / Loading
	
	@Override
	public void func_145841_b(NBTTagCompound tagCompound)
	{
		super.func_145841_b(tagCompound);
		tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
		tagCompound.setShort("CookTime", (short) furnaceCookTime);
		NBTTagList itemsList = new NBTTagList();

		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				NBTTagCompound slotTag = new NBTTagCompound();
				slotTag.setByte("Slot", (byte) i);
				inv[i].writeToNBT(slotTag);
				itemsList.appendTag(slotTag);
			}

			tagCompound.setTag("Items", itemsList);
		}
		energy.writeToNBT(tagCompound);
	}
	
	@Override
	public void func_145839_a(NBTTagCompound tagCompound)
	{
		super.func_145839_a(tagCompound);


		NBTTagList itemsTag = tagCompound.func_150295_c("Items", 0);
		inv = new ItemStack[getSizeInventory()];

		for (int i = 0; i < itemsTag.tagCount(); i++) {
			NBTTagCompound slotTag = (NBTTagCompound) itemsTag.func_150305_b(i);
			byte slot = slotTag.getByte("slot");

			if (slot >= 0 && slot < inv.length)
				inv[slot] = ItemStack.loadItemStackFromNBT(slotTag);
		}

		furnaceBurnTime = tagCompound.getShort("BurnTime");
		furnaceCookTime = tagCompound.getShort("CookTime");
		currentItemBurnTime = TileEntityFurnace.func_145952_a(inv[1]);
		
		if (energy == null)
		{
			energy = new EnergyHandler(this.field_145851_c, this.field_145848_d, this.field_145849_e);
		}
		energy.readFromNBT(tagCompound);
	}

	@Override
	public float transferTo(ForgeDirection d, float power)
	{
		return this.energy.placeEnegry(d, power);
	}

	@Override
	public void requestFrom(ForgeDirection d, float power)
	{
		
	}

	@Override
	public float getEnergyLevel()
	{
		return this.energy.getEnergyLevel();
	}
}
