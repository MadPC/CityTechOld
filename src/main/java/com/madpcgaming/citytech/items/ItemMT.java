package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.madpcgaming.citytech.CityTech;

public class ItemMT extends Item
{
	private String unlocName;
	
	public ItemMT(String unlocName)
	{
		super();
		this.setCreativeTab(CityTech.tabsCT);
		setUnlocalizedName(this.unlocName = unlocName);
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon("citytech:" + unlocName);
	}
}
