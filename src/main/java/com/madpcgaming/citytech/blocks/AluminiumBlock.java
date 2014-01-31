package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;

public class AluminiumBlock extends BlockMT
{
	
	public AluminiumBlock(int id)
	{
		//field_151573_f = iron
		super(id, Material.field_151573_f);
		//func_149752_b = setResistence
		this.func_149752_b(5.0F);
		//func_149647_a = setCreativeTab
		this.func_149647_a(CityTech.tabsMT);
		//func_149663_c = setUnlocalizedName
		this.func_149663_c(Strings.ALUMINIUM_BLOCK_NAME);
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.ALUMINUM_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	//func_149651_a = registerBlockIcons
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		//field_149761_L = blockIcon
		field_149761_L = iconRegister.registerIcon("madtech:blockAluminium");
	}
	
}