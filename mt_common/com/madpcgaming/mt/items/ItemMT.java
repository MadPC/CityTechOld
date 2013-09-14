package com.madpcgaming.mt.items;

import net.minecraft.item.Item;

import com.madpcgaming.mt.MadTech;

public class ItemMT extends Item
{

	public ItemMT(int par1)
	{
		super(par1);
		this.setCreativeTab(MadTech.tabsMT);
	}
	
	protected void afterInit()
	{
		
	}
}
