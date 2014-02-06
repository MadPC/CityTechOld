package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;

public class BlockMT extends Block
{
	
	public BlockMT(int par1, Material par2Material)
	{
		super(par2Material);
		// LogHelper.info("&&Calling BlockMT constructor with id %d and Material %s",
		// par1, par2Material);
		this.setCreativeTab(CityTech.tabsCT);
		this.setHardness(3.0f);
		this.setResistance(1.0f);
		
		// Sets effectiveness.
	}
}
