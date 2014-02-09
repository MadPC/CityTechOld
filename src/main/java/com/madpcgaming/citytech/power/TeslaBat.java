package com.madpcgaming.citytech.power;

import net.minecraft.item.ItemStack;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.Lang;

public enum TeslaBat
{
	BASIC_TESLA_BAT
	(new BasicTeslaBat(20, 10000, 2), "basicTelsaBat"),
	
	ACTIVATED_TESLA_BAT
	(new BasicTeslaBat(40, 20000, 6), "activatedTelsaBat"),
	
	ADVANCE_TESLA_BAT
	(new BasicTeslaBat(100, 100000, 10), "advanceTeslabat");
	
	public final ITeslaBat teslaBat;
	public final String unlocalizedName;
	public final String uiName;
	public final String iconKey;
	
	private TeslaBat(ITeslaBat teslaBat, String iconKey)
	{
		this.teslaBat = teslaBat;
		this.uiName = Lang.localize(iconKey);
		this.iconKey = "citytech:" + iconKey;
		this.unlocalizedName = name();
	}
	
	public ItemStack getItemStack()
	{
		return new ItemStack(ModBlocks.itemBasicTelsaBat, 1, ordinal());
	}
}
