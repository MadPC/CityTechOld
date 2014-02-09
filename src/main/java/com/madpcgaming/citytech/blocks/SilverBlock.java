package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;

public class SilverBlock extends BlockCT
{
	
	public SilverBlock(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(CityTech.tabsCT);
		this.setResistance(5.0F);
		this.setBlockName(Strings.SILVER_BLOCK_NAME);
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.SILVER_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("citytech:oreSilver");
	}
	
}