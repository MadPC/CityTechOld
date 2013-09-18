package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;

import com.madpcgaming.mt.lib.Strings;

public class Silicon extends ItemMT
{
	
	public Silicon(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.SILICON_ITEM_NAME);
		maxStackSize = 64;
		afterInit();
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:Silicon");
	}
}
