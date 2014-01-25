package com.madpcgaming.madtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.madtech.MadTech;

public class BlockMT extends Block
{
	
	public BlockMT(Material par2Material)
	{
		super(par2Material);
		// LogHelper.info("&&Calling BlockMT constructor with id %d and Material %s",
		// par1, par2Material);
		this.func_149647_a(MadTech.tabsMT);
		this.func_149711_c(3.0f);
		this.func_149752_b(1.0f);
		
		// Sets effectiveness.
	}
}
