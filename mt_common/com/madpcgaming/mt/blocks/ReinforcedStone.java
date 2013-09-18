package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import com.madpcgaming.mt.lib.Strings;

public class ReinforcedStone extends BlockMT {

	public ReinforcedStone(int id) {
		super(id, Material.rock);
		this.setUnlocalizedName(Strings.REINFORCED_STONE_NAME);
		this.afterInit();
	}

	
}
