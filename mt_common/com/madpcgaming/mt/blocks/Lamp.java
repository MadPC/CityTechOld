package com.madpcgaming.mt.blocks;

import java.util.Random;

import com.madpcgaming.mt.MadTech;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

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
		this.setCreativeTab(MadTech.tabsMT);
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
	
	  private void checkPowerState(World world, int i, int j, int k)
	  {
	    if (!this.powered) {
	      int md = world.getBlockMetadata(i, j, k);
	      world.setBlockMetadataWithNotify(i, j, k, this.onID, md);
	      world.markBlockForUpdate(i, j, k);
	    } else if (this.powered){
	      int md = world.getBlockMetadata(i, j, k);
	      world.setBlockMetadataWithNotify(i, j, k, this.offID, md);
	      world.markBlockForUpdate(i, j, k);
	    }
	  }
	  public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	  {
	    checkPowerState(world, i, j, k);
	  }

	  public void onBlockAdded(World world, int i, int j, int k) {
	    checkPowerState(world, i, j, k);
	  }

}
