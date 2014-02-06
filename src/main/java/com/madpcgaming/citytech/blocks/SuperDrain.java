package com.madpcgaming.citytech.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.DrainTE;

public class SuperDrain extends BlockContainer
{
	
	protected SuperDrain(int par1)
	{
		super(Material.iron);
		this.setHardness(1.0f);
		this.setCreativeTab(CityTech.tabsCT);
		setBlockName(Strings.SUPER_DRAIN_NAME);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("madtech:BlockDrain");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int var1)
	{
		return new DrainTE();
	}
	
}
