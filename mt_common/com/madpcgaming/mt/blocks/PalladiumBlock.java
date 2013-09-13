package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import java.util.Random;

import com.madpcgaming.mt.lib.BlockIds;
import com.madpcgaming.mt.lib.Strings;

public class PalladiumBlock extends BlockMT
{
	
	public PalladiumBlock(int id)
	{
		super(id, Material.rock);
		this.setResistance(5.0F);
		this.setUnlocalizedName(Strings.PALLADIUM_BLOCK_NAME);
		this.afterInit();
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.PALLADIUM_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
}