package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.MadTech;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;

public class TinBlock extends BlockMT
{
	
	public TinBlock(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149752_b(5.0F);
		this.func_149663_c(Strings.TIN_BLOCK_NAME);
		this.func_149647_a(MadTech.tabsMT);
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.TIN_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:blockTin");
	}
	
}