package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;

import com.madpcgaming.mt.lib.Strings;

public class ReinforcedGlass extends BlockMT {

	public ReinforcedGlass(int id) {
		super(id, Material.glass);
		this.setUnlocalizedName(Strings.REINFORCED_GLASS_NAME);
		this.afterInit();
	}

	
}
