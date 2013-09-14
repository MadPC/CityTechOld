package com.madpcgaming.mt.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;

public class MachineBlock extends Block {

	protected MachineBlock(int id)
	{
		super(id, Material.iron);
		
		setHardness(0.5F);
		setCreativeTab(MadTech.tabsMT);
		setUnlocalizedName(Strings.MACHINEBLOCK_NAME);
		this.textureName = this.getUnlocalizedName();
		
	}}
