package com.madpcgaming.mt.blocks.liquids;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;

public class SiliconLiquidStill extends BlockStationary
{
	
	public SiliconLiquidStill(int id)
	{
		super(id, Material.water);
		blockHardness = (100F);
		this.setLightOpacity(3);
		this.setUnlocalizedName(Strings.SILICON_LIQUID_STILL);
		this.setCreativeTab(MadTech.tabsMT);
		this.disableStats();
	}
	
}
