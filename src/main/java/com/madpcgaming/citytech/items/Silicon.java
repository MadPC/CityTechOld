package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class Silicon extends ItemMT
{
	
	public Silicon(int id)
	{
		super();
		this.setUnlocalizedName(Strings.SILICON_ITEM_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("madtech:Silicon");
	}
}
