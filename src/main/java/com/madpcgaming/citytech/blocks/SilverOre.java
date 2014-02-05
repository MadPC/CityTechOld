package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;

public class SilverOre extends BlockMT
{
	
	public SilverOre(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149663_c(Strings.SILVER_ORE_NAME);
		this.func_149647_a(CityTech.tabsCT);
	}
	
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.SilverIngot;
	}
	
	@Override
	public int func_149745_a(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:oreSilver");
	}
}
