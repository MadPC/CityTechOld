package com.madpcgaming.mt.blocks;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ReinforcedStone extends Block
{

	public ReinforcedStone(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(MadTech.tabsMT);
		this.setHardness(1.0F);
		this.setResistance(2000.0F);
		this.setUnlocalizedName(Strings.REINFORCED_STONE_NAME);
	}

}
