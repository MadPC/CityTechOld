package com.madpcgaming.madtech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.items.ModItems;
import com.madpcgaming.madtech.lib.Strings;

public class CopperOre extends BlockMT
{
	
	public CopperOre(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149663_c (Strings.COPPER_ORE_NAME);
		this.func_149647_a(MadTech.tabsMT);
	}
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.CopperIngot;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:oreCopper");
	}
}
