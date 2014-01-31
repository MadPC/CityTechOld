package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;

public class TinOre extends BlockMT
{
	
	public TinOre(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149663_c(Strings.TIN_ORE_NAME);
		this.func_149647_a(CityTech.tabsMT);
	}
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.TinIngot;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:oreTin");
	}
	
}
