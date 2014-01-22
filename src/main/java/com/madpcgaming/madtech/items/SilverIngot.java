package com.madpcgaming.madtech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.lib.Strings;

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
		itemIcon = iconRegister.registerIcon("mt:ingotSilver");
	}
	
}