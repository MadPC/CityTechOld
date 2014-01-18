package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;

import com.madpcgaming.mt.lib.Strings;

public class AluminumIngot extends ItemMT
{
	
	public AluminumIngot(int id)
	{
		
		super(id);
		this.setUnlocalizedName(Strings.ALUMINUM_INGOT_NAME);
		maxStackSize = 64;
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:ingotAluminum");
	}
}
