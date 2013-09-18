package com.madpcgaming.mt.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

import java.util.Random;

import com.madpcgaming.mt.lib.BlockIds;
import com.madpcgaming.mt.lib.Strings;

public class CopperBlock extends BlockMT
{
	
	public CopperBlock(int id)
	{
		super(id, Material.rock);
		this.setResistance(5.0F);
		this.setUnlocalizedName(Strings.COPPER_BLOCK_NAME);
		this.afterInit();
	}
	
	public int idDropped(int par1, Random random, int par3)
	{
		return BlockIds.COPPER_BLOCK;
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("mt:blockCopper");
	}
	
}