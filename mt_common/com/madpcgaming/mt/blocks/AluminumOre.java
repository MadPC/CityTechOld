package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import java.util.Random;

import com.madpcgaming.mt.items.ModItems;
import com.madpcgaming.mt.lib.Strings;

public class AluminumOre extends BlockMT
{
	
	public AluminumOre(int id)
	{
		super(id, Material.rock);
		this.setResistance(5.0F);
		this.setUnlocalizedName(Strings.ALUMINUM_ORE_NAME);
		this.afterInit();
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return ModItems.AluminumIngot.itemID;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
}
