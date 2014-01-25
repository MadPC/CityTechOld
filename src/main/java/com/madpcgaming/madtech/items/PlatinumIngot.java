package com.madpcgaming.madtech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.lib.Strings;

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
		itemIcon = iconRegister.registerIcon("madtech:ingotPlatinum");
	}
	
	
}
