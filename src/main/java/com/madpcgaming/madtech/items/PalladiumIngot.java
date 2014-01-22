package com.madpcgaming.madtech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.lib.Strings;

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
		itemIcon = iconRegister.registerIcon("mt:ingotPalladium");
	}
	
}
