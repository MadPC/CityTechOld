package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class ReinforcedGlass extends Block {

	public ReinforcedGlass(int id) {
		super(Material.field_151592_s);
		this.func_149663_c(Strings.REINFORCED_GLASS_NAME);
		this.func_149752_b(2000.0F);
		this.func_149711_c(1.0F);
		this.func_149647_a(CityTech.tabsCT);
		
	}

	
}
