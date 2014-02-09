package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class PalladiumIngot extends ItemMT
{
	
	public PalladiumIngot(int par1)
	{
		super();
		this.setUnlocalizedName(Strings.PALLADIUM_INGOT_NAME);
		maxStackSize = 64;
	}
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("citytech:ingotPalladium");
	}
	
}
