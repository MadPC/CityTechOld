package com.madpcgaming.madtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.lib.Strings;

public class ReinforcedStone extends Block
{

	public ReinforcedStone(int id)
	{
		super(Material.field_151576_e);
		this.func_149647_a(MadTech.tabsMT);
		this.func_149711_c(1.0F);
		this.func_149752_b(2000.0F);
		this.func_149663_c(Strings.REINFORCED_STONE_NAME);
	}

}
