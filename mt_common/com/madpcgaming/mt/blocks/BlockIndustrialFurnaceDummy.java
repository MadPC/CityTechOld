package com.madpcgaming.mt.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Strings;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceDummy;

public class BlockIndustrialFurnaceDummy extends BlockContainer {

	public BlockIndustrialFurnaceDummy(int id) {
		super(id, Material.rock);
		setUnlocalizedName(Strings.FURNACEDUMMY_NAME);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(MadTech.tabsMT);
	}
	
	@Override
	public int idDropped(int id, Random random, int par3)
	{
		return ModBlocks.IndustrialFurnaceDummy.blockID;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileEntityIndustrialFurnaceDummy();
	}
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		//Johulk or Krystian texture please
		
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiBlock();
		
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;
		
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityIndustrialFurnaceCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}
		return true;
	}

}
