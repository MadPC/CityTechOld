package com.madpcgaming.mt.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

import com.madpcgaming.mt.MadTech;

public class BlockMT extends Block
{
	
	public BlockMT(int par1, Material par2Material)
	{
		super(par1, par2Material);
		// LogHelper.info("&&Calling BlockMT constructor with id %d and Material %s",
		// par1, par2Material);
		this.setCreativeTab(MadTech.tabsMT);
		this.blockHardness = 3.0f;
		this.blockResistance = 1.0f;
		
		// Sets effectiveness.
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 1);
	}
	
	protected void afterInit()
	{
		
	}
	
}
