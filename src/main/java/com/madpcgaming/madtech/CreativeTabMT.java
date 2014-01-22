package com.madpcgaming.madtech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
// import com.madpcgaming.mt.lib.ItemIds;

public class CreativeTabMT extends CreativeTabs
{
	
	public CreativeTabMT(int par1, String par2Str)
	{
		
		super(par1, par2Str);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * the itemID for the item to be displayed on the tab
	 */
	public Item getTabIconItem()
	{
		
		return Items.iron_ingot;
	}
	
}
