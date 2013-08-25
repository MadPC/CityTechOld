package com.madpcgaming.mt.items;

import net.minecraft.item.Item;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Reference;

public class ItemMT extends Item
{

	public ItemMT(int par1)
	{
		super(par1);
		this.setCreativeTab(MadTech.tabsMT);
	}
	
	protected void afterInit()
	{
		this.func_111206_d(Reference.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
	}
}
