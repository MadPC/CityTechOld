package com.madpcgaming.mt.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Lamp extends Block
{
	public boolean lit;
	public boolean powered;
	public int onID;
	public int offID;

	public Lamp(int id, boolean l, boolean p)
	{
		super(id, Material.glass);
		this.setHardness(0.5F);
		this.lit = l;
		this.powered = p;
	}
	
	public boolean canRenderInPass(int n)
	{
		return true;
	}
	
	public boolean isOpaqueCube()
	{
		return true;
	}
	
	public boolean renderAsNormalBlock()
	{
		return true;
	}
	
	public boolean isACube()
	{
		return true;
	}
	
	public boolean canProvidePower()
	{
		return true;
	}
	
	public int getRenderBlockPass()
	{
		return 1;
	}
	
	public int damagedDropped(int i)
	{
		return i;
	}
	
	public int idDropped(int i, Random random, int j)
	{
		return this.offID;
	}

}
