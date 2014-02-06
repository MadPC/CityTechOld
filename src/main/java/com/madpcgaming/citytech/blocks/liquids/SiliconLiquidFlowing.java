package com.madpcgaming.citytech.blocks.liquids;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class SiliconLiquidFlowing extends BlockLiquid
{
	
	public SiliconLiquidFlowing(int id)
	{
		super(Material.water);
		setHardness(100F);
		this.setLightOpacity(3);
		this.setCreativeTab(CityTech.tabsCT);
		this.setBlockName(Strings.SILICON_LIQUID_FLOWING);
		
	}
	
}
