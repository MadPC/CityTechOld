package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class ReinforcedStone extends Block
{

	public ReinforcedStone(int id)
	{
		super(Material.rock);
		this.setCreativeTab(CityTech.tabsCT);
		this.setHardness(1.0F);
		this.setResistance(2000.0F);
		this.setBlockName(Strings.REINFORCED_STONE_NAME);
	}

}
