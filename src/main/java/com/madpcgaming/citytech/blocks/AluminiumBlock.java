package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;

public class AluminiumBlock extends BlockCT
{
	
	public AluminiumBlock(int id)
	{
		//iron = iron
		super(id, Material.iron);
		//setResistance = setResistence
		this.setResistance(5.0F);
		//setCreativeTab = setCreativeTab
		this.setCreativeTab(CityTech.tabsCT);
		//setBlockName = setUnlocalizedName
		this.setBlockName(Strings.ALUMINIUM_BLOCK_NAME);
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.ALUMINUM_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	//registerBlockIcons = registerBlockIcons
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//blockIcon = blockIcon
		blockIcon = iconRegister.registerIcon("citytech:blockAluminium");
	}
	
}