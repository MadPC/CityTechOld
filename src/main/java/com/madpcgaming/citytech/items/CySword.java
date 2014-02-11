package com.madpcgaming.citytech.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.madpcgaming.citytech.items.interfaces.ILevelableItem;
import com.madpcgaming.citytech.lib.Strings;

public class CySword extends ItemSword implements ILevelableItem
{
	private ToolMaterial mat;
	private final Item.ToolMaterial field_150933_b;
	private String unlocName = Strings.CYBERSWORD_ITEM_NAME;
	
	public CySword(ToolMaterial mat)
	{
		super(mat);
		this.mat = mat;
		this.field_150933_b = mat;
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocName;
	}
	
	@Override
	public boolean getShareTag()
	{
		return true;
	}
	
	@Override
	public void recalculate(ItemStack item)
	{
		item.stackTagCompound.setInteger("durability", (item.stackTagCompound.getInteger("level") * 50 + 50 + this.mat.getMaxUses()));
		this.getDamageVsEntity(new EntityBlaze(null), item);
	}
	
	public int getMaxDamage(ItemStack stack)
    {
		NBTTagCompound tag = stack.stackTagCompound;
		return tag == null ? 0 : tag.getInteger("durability");
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
		NBTTagCompound tag = par1ItemStack.stackTagCompound;
		if (tag.getInteger("durability") > 0)
			par1ItemStack.damageItem(1, par3EntityLivingBase);
        return true;
    }
	
	public float getDamageVsEntity(Entity par1Entity, ItemStack itemStack)
	{
		float damage = this.field_150933_b.getDamageVsEntity();
		NBTTagCompound tag = itemStack.stackTagCompound;
		int durability = tag.getInteger("durability");
		if (durability > 0)
		{
			damage += tag.getInteger("level") * 2;
		}
		else
		{
			damage = 1;
		}
		return damage;
	}
	
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		NBTTagCompound tag = par1ItemStack.stackTagCompound;
		if (tag == null)
		{
			tag = new NBTTagCompound();
			tag.setInteger("level", 1);
		} else if (!tag.hasKey("level"))
		{
			tag.setInteger("level", 1);
		}
		tag.setInteger("durability", (int) (this.mat.getMaxUses() + tag.getInteger("level") * 50 + 50));
		par1ItemStack.setTagCompound(tag);
	}
}
