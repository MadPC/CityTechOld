package com.madpcgaming.mt.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;

import com.madpcgaming.mt.items.ModItems;
import com.madpcgaming.mt.lib.Strings;

public class SilverOre extends BlockMT
{
	
	public SilverOre(int id)
	{
		super(id, Material.rock);
		this.setUnlocalizedName(Strings.SILVER_ORE_NAME);
		this.afterInit();
	}
	
	@Override
	public int idDropped(int par1, Random random, int par3)
	{
		return ModItems.SilverIngot.itemID;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
}
