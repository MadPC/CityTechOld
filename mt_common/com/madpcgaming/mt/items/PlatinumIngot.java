package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;

import com.madpcgaming.mt.lib.Strings;

public class PlatinumIngot extends ItemMT
{
	
	public PlatinumIngot(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.PLATINUM_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:ingotPlatinum");
	}
	
	
}
