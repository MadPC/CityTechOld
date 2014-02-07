package com.madpcgaming.citytech.piping;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public enum PipingDisplayMode
{
	ALL,
	ITEM,
	FLUID,
	REDSTONE,
	POWER;
	
	public static PipingDisplayMode next(PipingDisplayMode mode)
	{
		int index = mode.ordinal() + 1;
		if(index >= values().length)
		{
			index = 0;
		}
		return values()[index];
	}
	
	public static PipingDisplayMode previous(PipingDisplayMode mode)
	{
		int index = mode.ordinal() - 1;
		if(index < 0)
		{
			index = values().length - 1;
		}
		return values()[index];
	}
	
	public static PipingDisplayMode getDisplayMode(ItemStack equipped)
	{
		if(equipped == null)
		{
			return ALL;
		}
		int index = equipped.getItemDamage();
		index = MathHelper.clamp_int(index, 0, PipingDisplayMode.values().length - 1);
		return PipingDisplayMode.values()[index];
	}
	
	public static void setDisplayMode(ItemStack equipped, PipingDisplayMode mode)
	{
		if(mode == null || equipped == null)
		{
			return;
		}
		equipped.setItemDamage(mode.ordinal());
	}
	
	public PipingDisplayMode next()
	{
		return next(this);
	}
	
	public PipingDisplayMode previous()
	{
		return previous(this);
	}
}
