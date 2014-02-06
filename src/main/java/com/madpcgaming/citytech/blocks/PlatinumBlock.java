package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;

public class PlatinumBlock extends BlockMT
{

	public PlatinumBlock(int id)
	{
		super(id, Material.rock);
		this.setResistance(5.0F);
		this.setBlockName(Strings.PLATINUM_BLOCK_NAME);
		this.setCreativeTab(CityTech.tabsCT);
	}

	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.PLATINUM_BLOCK;
	}

	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("madtech:blockPlatinum");
	}

}