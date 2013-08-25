package com.madpcgaming.mt.blocks.liquids;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;

import net.minecraft.block.BlockFlowing;
import net.minecraft.block.material.Material;

public class SiliconLiquidFlowing extends BlockFlowing
{
	
	public SiliconLiquidFlowing(int id)
	{
		super(id, Material.water);
		blockHardness = 100F;
		this.setLightOpacity(3);
		this.setCreativeTab(MadTech.tabsMT);
		this.setUnlocalizedName(Strings.SILICON_LIQUID_FLOWING);
		
	}
	
}
