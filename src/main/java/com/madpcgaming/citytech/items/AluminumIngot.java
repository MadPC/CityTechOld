package com.madpcgaming.citytech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.lib.Strings;

public class AluminumIngot extends ItemMT
{
	
	public AluminumIngot(int id)
	{
		
		super();
		this.setUnlocalizedName(Strings.ALUMINUM_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("madtech:ingotAluminum");
	}
}
