package com.madpcgaming.madtech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.lib.BlockIds;
import com.madpcgaming.madtech.lib.Strings;

public class SilverBlock extends BlockMT
{
	
	public SilverBlock(int id)
	{
		super(id, Material.field_151576_e);
		this.func_149647_a(MadTech.tabsMT);
		this.func_149752_b(5.0F);
		this.func_149663_c(Strings.SILVER_BLOCK_NAME);
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.SILVER_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("mt:oreSilver");
	}
	
}