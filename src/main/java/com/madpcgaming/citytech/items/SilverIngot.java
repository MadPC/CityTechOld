package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class SilverIngot extends ItemMT
{
	
	public SilverIngot(int par1)
	{
		super();
		this.setUnlocalizedName(Strings.SILVER_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("citytech:ingotSilver");
	}
	
}
