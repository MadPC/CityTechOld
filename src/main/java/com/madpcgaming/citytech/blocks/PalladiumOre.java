package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;

public class PalladiumOre extends BlockMT
{
	
	public PalladiumOre(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149663_c(Strings.PALLADIUM_ORE_NAME);
		this.func_149647_a(CityTech.tabsCT);
	}
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.PalladiumIngot;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:orePalladium");
	}
}