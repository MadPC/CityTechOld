package com.madpcgaming.mt;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

// import com.madpcgaming.mt.lib.ItemIds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	public int getTabIconItemIndex()
	{
		
		return Item.ingotIron.itemID;
	}
	
}
