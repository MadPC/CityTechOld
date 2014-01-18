package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;

import com.madpcgaming.mt.lib.Strings;

public class SilverIngot extends ItemMT
{
	
	public SilverIngot(int par1)
	{
		super(par1);
		this.setUnlocalizedName(Strings.SILVER_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:ingotSilver");
	}
	
}
