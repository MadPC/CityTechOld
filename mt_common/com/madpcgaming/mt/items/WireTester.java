package com.madpcgaming.mt.items;

import com.madpcgaming.mt.MadTech;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class WireTester extends ItemMT
{
	
	public WireTester(int par1)
	{
		super(par1);
		this.setCreativeTab(MadTech.tabsMT);
		this.maxStackSize = 1;
		LanguageRegistry.addName(this, "Wire tester DEV");
		afterInit();
	}
	
}
