package com.madpcgaming.madtech.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.tileentitys.DrainTE;

public class SuperDrain extends BlockContainer
{
	
	protected SuperDrain(int par1)
	{
		super(Material.field_151573_f);
		this.func_149711_c(1.0f);
		this.func_149647_a(MadTech.tabsMT);
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("mt:BlockDrain");
	}
	
	@Override
	public TileEntity func_149915_a(World world, int var1)
	{
		return new DrainTE();
	}
	
}
