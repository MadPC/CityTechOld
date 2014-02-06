package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class ReinforcedGlass extends Block {

	public ReinforcedGlass(int id) {
		super(Material.glass);
		this.setBlockName(Strings.REINFORCED_GLASS_NAME);
		this.setResistance(2000.0F);
		this.setHardness(1.0F);
		this.setCreativeTab(CityTech.tabsCT);
		
	}

	
}
