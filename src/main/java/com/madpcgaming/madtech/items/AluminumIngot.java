package com.madpcgaming.madtech.items;

import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.lib.Strings;

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
