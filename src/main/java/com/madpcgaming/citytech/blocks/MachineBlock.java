package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;

public class MachineBlock extends Block {

	protected MachineBlock(int id)
	{
		super(Material.iron);
		
		setHardness(0.5F);
		setCreativeTab(CityTech.tabsCT);
		setBlockName(Strings.MACHINEBLOCK_NAME);
		this.textureName = this.getUnlocalizedName();
		
	}
}
