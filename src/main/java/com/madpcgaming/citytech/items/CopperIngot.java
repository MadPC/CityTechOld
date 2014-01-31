package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class CopperIngot extends ItemMT
{
	
	public CopperIngot(int id)
	{
		super();
		this.setUnlocalizedName(Strings.COPPER_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("madtech:ingotCopper");
	}
	
}
