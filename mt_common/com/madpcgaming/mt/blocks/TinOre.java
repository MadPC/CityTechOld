package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import java.util.Random;

import com.madpcgaming.mt.lib.BlockIds;
import com.madpcgaming.mt.lib.Strings;

public class TinOre extends BlockMT
{
	
	public TinOre(int id)
	{
		super(id, Material.rock);
		this.setUnlocalizedName(Strings.TIN_ORE_NAME);
		this.afterInit();
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.TIN_ORE;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
}
