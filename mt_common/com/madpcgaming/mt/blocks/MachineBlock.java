package com.madpcgaming.mt.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Reference;
import com.madpcgaming.mt.lib.Strings;

public class MachineBlock extends Block {

	protected MachineBlock(int id)
	{
		super(id, Material.iron);
		
		this.setHardness(0.5F);
		this.setCreativeTab(MadTech.tabsMT);
		this.setUnlocalizedName(Strings.MACHINEBLOCK_NAME);
		this.field_111026_f = this.getUnlocalizedName();
		this.func_111022_d(Reference.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
	}}
