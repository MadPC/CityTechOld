package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;

public class AluminiumOre extends BlockCT
{
	
	public AluminiumOre(int id)
	{
		//rock = rock
		super(id, Material.rock);
		//setResistance = setResistence
		this.setResistance(5.0F);
		//setBlockName  = setUnlocalizedName
		this.setBlockName (Strings.ALUMINIUM_ORE_NAME);
		//setCreativeTab - setcreativeTab
		this.setCreativeTab(CityTech.tabsCT);
	}
	
	public Item idDropped(int par1, Random random, int par3)
	{
		return ModItems.AluminiumIngot;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	//registerBlockIcons - registerBlockIcons
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//blockIcon - blockIcon
		blockIcon = iconRegister.registerIcon("citytech:oreAluminium");
	}
	
}
