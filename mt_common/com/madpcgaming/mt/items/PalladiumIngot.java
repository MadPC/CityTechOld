package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;

import com.madpcgaming.mt.lib.Strings;

public class PalladiumIngot extends ItemMT
{
	
	public PalladiumIngot(int par1)
	{
		super(par1);
		this.setUnlocalizedName(Strings.PALLADIUM_INGOT_NAME);
		maxStackSize = 64;
		afterInit();
	}
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:ingotPalladium");
	}
	
}
