package com.madpcgaming.citytech.blocks.multiblocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceDummy;

public class IndustrialFurnaceDummy extends BlockContainer
{

	public IndustrialFurnaceDummy(int id)
	{
		super(Material.field_151576_e);
		func_149663_c(Strings.FURNACEDUMMY_NAME);
		func_149672_a(Block.field_149769_e);
		func_149711_c(3.5f);
		func_149647_a(CityTech.tabsCT);
	}
	
	public Item idDropped(int par1, Random par2Random, int par3)
	{
		return Item.func_150898_a(Blocks.brick_block);
	}

	@Override
	public TileEntity func_149915_a(World world, int var1)
	{
		return new TileEntityIndustrialFurnaceDummy();
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:brick");
	}
	
	@Override
	public void func_149749_a(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.func_147438_o(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiBlock();
		
		super.func_149749_a(world, x, y, z, par5, par6);
	}
	
	@Override
	public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;
		
		TileEntityIndustrialFurnaceDummy dummy = (TileEntityIndustrialFurnaceDummy)world.func_147438_o(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityIndustrialFurnaceCore core = dummy.getCore();
			return core.func_145838_q().func_149727_a(world, core.field_145851_c, core.field_145848_d, core.field_145849_e, player , par6, par7, par8, par9);
		}
		
		return true;
	}

}
