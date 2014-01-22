package com.madpcgaming.madtech.items;

import net.minecraft.client.renderer.texture.IIconRegister;



public class Rubber extends ItemMT
{
	
	public Rubber(int id)
	{
		super();
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:Rubber");
	}
	
}
