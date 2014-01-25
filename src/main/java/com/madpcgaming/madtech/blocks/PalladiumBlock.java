package com.madpcgaming.madtech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.lib.Strings;

public class PalladiumBlock extends BlockMT
{
	
	public PalladiumBlock()
	{
		super(Material.field_151576_e);
		this.func_149752_b(5.0F);
		this.func_149663_c(Strings.PALLADIUM_BLOCK_NAME);
		this.func_149647_a(MadTech.tabsMT);
	}
	
	public int quantityDropped(Random random)
	{
		return 1;
	}
	
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:orePalladium");
	}
	
}