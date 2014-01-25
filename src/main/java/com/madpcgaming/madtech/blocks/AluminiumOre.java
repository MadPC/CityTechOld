package com.madpcgaming.madtech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.items.ModItems;
import com.madpcgaming.madtech.lib.Strings;

public class AluminiumOre extends BlockMT
{
	
	public AluminiumOre()
	{
		//field_151576_e = rock
		super(Material.field_151576_e);
		//func_149752_b = setResistence
		this.func_149752_b(5.0F);
		//func_149663_c  = setUnlocalizedName
		this.func_149663_c (Strings.ALUMINIUM_ORE_NAME);
		//func_149647_a - setcreativeTab
		this.func_149647_a(MadTech.tabsMT);
	}
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.AluminiumIngot;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	//func_149651_a - registerBlockIcons
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		//field_149761_L - blockIcon
		field_149761_L = iconRegister.registerIcon("madtech:blockAluminium");
	}
	
}
