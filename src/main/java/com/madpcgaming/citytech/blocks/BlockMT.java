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
		this.func_149647_a(CityTech.tabsCT);
		this.func_149711_c(3.0f);
		this.func_149752_b(1.0f);
		
		// Sets effectiveness.
	}
}
