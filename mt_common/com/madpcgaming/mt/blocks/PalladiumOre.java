package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import java.util.Random;

import com.madpcgaming.mt.items.ModItems;
import com.madpcgaming.mt.lib.Strings;

public class PalladiumOre extends BlockMT
{
	
	public PalladiumOre(int id)
	{
		super(id, Material.rock);
		this.setUnlocalizedName(Strings.PALLADIUM_ORE_NAME);
		this.afterInit();
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return ModItems.PalladiumIngot.itemID;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
}