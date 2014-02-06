package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceDummy;

public class BlockIndustrialFurnaceDummy extends BlockContainer {

	public BlockIndustrialFurnaceDummy(int id) {
		super(Material.rock);
		setBlockName(Strings.FURNACEDUMMY_NAME);
		setStepSound(Block.soundTypeMetal);
		setHardness(3.5f);
		setCreativeTab(CityTech.tabsCT);
	}
	
	@Override
	public Item getItem(World world, int par1, int par2, int par3)
	{
		return Item.getItemFromBlock(ModBlocks.IndustrialFurnaceDummy);
	}
	

	@Override
	public TileEntity createNewTileEntity(World world, int var1) {
		return new TileEntityIndustrialFurnaceDummy();
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//Johulk or Krystian texture please
		
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.getTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiBlock();
		
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;
		
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.getTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityIndustrialFurnaceCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}
		return true;
	}

}
