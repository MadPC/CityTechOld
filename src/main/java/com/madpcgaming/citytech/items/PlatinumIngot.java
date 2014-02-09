package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class PlatinumIngot extends ItemMT
{
	
	public PlatinumIngot(int id)
	{
		super();
		this.setUnlocalizedName(Strings.PLATINUM_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("citytech:ingotPlatinum");
	}
	
	
}
