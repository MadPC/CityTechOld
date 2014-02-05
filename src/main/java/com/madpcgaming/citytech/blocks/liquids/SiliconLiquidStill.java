package com.madpcgaming.citytech.blocks.liquids;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class SiliconLiquidStill extends BlockStaticLiquid
{
	
	public SiliconLiquidStill(int id)
	{
		super(Material.field_151586_h);
		func_149711_c(100F);
		this.func_149713_g(3);
		this.func_149663_c(Strings.SILICON_LIQUID_STILL);
		this.func_149647_a(CityTech.tabsCT);
		this.func_149649_H();
	}
	
}
