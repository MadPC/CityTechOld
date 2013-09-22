package com.madpcgaming.mt.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;

public class ReinforcedGlass extends Block {

	public ReinforcedGlass(int id) {
		super(id, Material.glass);
		this.setUnlocalizedName(Strings.REINFORCED_GLASS_NAME);
		this.setResistance(2000.0F);
		this.setHardness(1.0F);
		this.setCreativeTab(MadTech.tabsMT);
		
	}

	
}
