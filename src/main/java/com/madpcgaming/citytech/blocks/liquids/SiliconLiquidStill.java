package com.madpcgaming.citytech.blocks.liquids;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class SiliconLiquidStill extends BlockStaticLiquid
{
	
	public SiliconLiquidStill(int id)
	{
		super(Material.water);
		setHardness(100F);
		this.setLightOpacity(3);
		this.setBlockName(Strings.SILICON_LIQUID_STILL);
		this.setCreativeTab(CityTech.tabsCT);
		this.disableStats();
	}
	
}
