package com.madpcgaming.mt.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.tileentitys.DrainTE;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class SuperDrain extends BlockContainer
{
	
	protected SuperDrain(int par1)
	{
		super(par1, Material.iron);
		this.blockHardness = 1.0f;
		this.setCreativeTab(MadTech.tabsMT);
		LanguageRegistry.addName(this, "A Quantum Drain. Drains TONS of Power.");
	}
	
	public void registerIcons(IconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("mt:BlockDrain");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new DrainTE();
	}
	
}
