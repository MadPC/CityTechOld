package com.madpcgaming.mt.items;

import net.minecraft.client.renderer.texture.IconRegister;


public class Rubber extends ItemMT
{
	
	public Rubber(int id)
	{
		super(id);
		afterInit();
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("mt:Rubber");
	}
	
}
