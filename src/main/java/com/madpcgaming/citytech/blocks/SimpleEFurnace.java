package com.madpcgaming.citytech.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.GuiIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.TileSimpleEFurnace;

public class SimpleEFurnace extends BlockContainer
{
	
	public static final byte MASK_DIR = 0x07;
	public static final byte META_DIR_NORTH = 0x01;
	public static final byte META_DIR_SOUTH = 0x02;
	public static final byte META_DIR_EAST = 0x03;
	public static final byte META_DIR_WEST = 0x00;
	public static final byte META_IS_ACTIVE = 0x08;
	
	private IIcon faceIconUnlit;
	private IIcon faceIconLit;
	
	public SimpleEFurnace(int id)
	{
		super(Material.field_151573_f);
		this.func_149711_c(1.0f);
		this.func_149647_a(CityTech.tabsMT);
		func_149663_c(Strings.SIMPLE_SE_FURNACE_NAME);
	}
	
	//Display
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		field_149761_L = iconRegister.registerIcon("madtech:SEFurnace_Side");
		faceIconUnlit = iconRegister.registerIcon("madtech:SEFurnace_Front_Unlit");
		faceIconLit  = iconRegister.registerIcon("madtech:SEFurnace_Front_Lit");
	}

	
	public void func_149689_a(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack)
	{
		int metadata = 0;
		int facing = META_DIR_WEST;
		
		int dir = MathHelper.floor_double((double)(entity.rotationYaw * 4f / 360f) + 0.5) & 3;
		if(dir == 0)
			facing = META_DIR_NORTH;
		if(dir == 1)
			facing = META_DIR_EAST;
		if(dir == 2)
			facing = META_DIR_SOUTH;
		if(dir == 3)
			facing = META_DIR_WEST;
		
		metadata |= facing;
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
	}
	
	@Override
	public IIcon func_149691_a(int side, int metadata)
	{
		boolean isActive = ((metadata >> 3) == 1);
		int facing = (metadata & MASK_DIR);
		
		return (side == getSideFromFacing(facing) ? (isActive ? faceIconLit: faceIconUnlit): field_149761_L);
	}
	
	public int func_149745_a(Random random)
	{
		return 1;
	}
	
	/**
	 * Called upon block activation (right click on the block.)
	 */
	
	@Override
	public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
																							//func_147438_o - getTileEntity
		TileSimpleEFurnace tileEntity = (TileSimpleEFurnace) world.func_147438_o(x, y, z);
		
		if(tileEntity != null)
		{
				player.openGui(CityTech.instance, GuiIds.FURNACE_SIMPLE, world, x, y, z);
		}
		return true;
	}
	
	@Override
	public void func_149734_b(World world, int x, int y, int z, Random random)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		if((metadata & META_IS_ACTIVE) == 0)
			return;
		
		int facing = metadata & MASK_DIR;
		
		double yMod = (0.3 * random.nextDouble());
		double xMod = -0.02;
		double zMod = (0.75 - (0.5 * random.nextDouble()));
		double temp = 0.0;
		
		switch(facing)
		{
		case META_DIR_EAST:
			xMod += 1.04;
			break;
		case META_DIR_NORTH:
			temp = xMod;
			xMod = zMod;
			zMod = temp;
			break;
		case META_DIR_SOUTH:
			temp = xMod;
			xMod = zMod;
			zMod = temp + 1.04;
			break;
		default:
			break;
		}
		
		world.spawnParticle("smoke", x + xMod, y + yMod, z + zMod, 0, 0, 0);
		world.spawnParticle("flame", x + xMod, y + yMod, z + zMod, 0, 0, 0);
	}
	
	@Override
	public void func_149749_a(World world, int x, int y, int z, Block par5, int par6)
	{
		dropItems(world, x,y,z);
		super.func_149749_a(world, x, y, z, par5, par6);
	}
	
	private static int getSideFromFacing(int facing)
	{
		switch(facing)
		{
		case META_DIR_WEST:
			return 4;
		case META_DIR_EAST:
			return 5;
		case META_DIR_NORTH:
			return 2;
		case META_DIR_SOUTH:
			return 3;
		default:
			return 4;
		}
	}
	
	private void dropItems(World world, int x, int y, int z)
	{
		Random random = new Random();
		
		TileSimpleEFurnace tileEntity = (TileSimpleEFurnace)world.func_147438_o(x, y, z);
		if(tileEntity == null)
			return;
		
		for (int slot = 0; slot < tileEntity.getSizeInventory(); slot++)
		{
			ItemStack item = tileEntity.getStackInSlot(slot);
			
			if(item != null && item.stackSize > 0)
			{
				float rx = random.nextFloat() * 0.8f + 0.1f;
				float ry = random.nextFloat() * 0.8f + 0.1f;
				float rz = random.nextFloat() * 0.8f + 0.1f;
				
				EntityItem entityItem = new EntityItem(world, x +rx, y + ry, z + rz, item.copy());
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	//Tile Entity
	@Override
	public TileEntity func_149915_a(World world, int var1)
	{
		return new TileSimpleEFurnace();
	}
}
