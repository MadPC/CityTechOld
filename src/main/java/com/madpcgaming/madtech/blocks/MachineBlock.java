package com.madpcgaming.madtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.lib.Strings;

public class MachineBlock extends Block {

	protected MachineBlock(int id)
	{
		super(Material.field_151573_f);
		
		func_149711_c(0.5F);
		func_149647_a(MadTech.tabsMT);
		func_149663_c(Strings.MACHINEBLOCK_NAME);
		this.field_149768_d = this.func_149739_a();
		
	}
}
