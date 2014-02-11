package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.citytech.CityTech;

public class BlockCT extends Block
{
	private String unlocName;
	
	public BlockCT(String unlocName, Material par2Material, float hardness, float resistance)
	{
		super(par2Material);
		this.setCreativeTab(CityTech.tabsCT);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.unlocName = unlocName;
	}
	
	public BlockCT(String unlocName, Material par2Material, float resistance)
	{
		this(unlocName, par2Material, 3.0F, resistance);
	}
	
	public BlockCT(String unlocName, float hardness, Material par2Material)
	{
		this(unlocName, par2Material, hardness, 1.0F);
	}
	
	public BlockCT(String unlocName, Material par2Material)
	{
		this(unlocName, par2Material, 3.0F, 1.0F);
	}
	
	public BlockCT(String unlocName)
	{
		this(unlocName, Material.rock);
	}
	
	public String getUnlocalizedName() 
	{
		return "tile." + unlocName;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		this.blockIcon = register.registerIcon("citytech:" + unlocName);
	}
}
