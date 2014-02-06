package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;

public class CopperOre extends BlockMT
{
	
	public CopperOre(int id)
	{
		super(id, Material.rock);
		this.setBlockName (Strings.COPPER_ORE_NAME);
		this.setCreativeTab(CityTech.tabsCT);
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
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("madtech:oreCopper");
	}
}
