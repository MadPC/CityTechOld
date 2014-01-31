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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.GuiIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;

public class BlockIndustrialFurnaceCore extends BlockContainer {
	
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;
	public static final int META_ISACTIVE = 0x00000008;
	
	private IIcon faceIconUnlit;
	private IIcon faceIconLit;


	public BlockIndustrialFurnaceCore(int id) {
		//field_151576_e - rock
		super(Material.field_151576_e);
		//func_149663_c - setUnlocalizedName
		func_149663_c(Strings.FURNACECORE_NAME);
		//func_149711_c - setHardness
		func_149711_c (3.5F);
		//func_149647_a - setcreativeTab
		func_149647_a(CityTech.tabsMT);
		
	}
	
	//func_149651_a - registerBlockIcons
	@Override
	public void func_149651_a(IIconRegister iconRegister)
	{
		//field_149761_L - blockIcon
		field_149761_L = iconRegister.registerIcon("madtech:brick");
		faceIconUnlit = iconRegister.registerIcon("madtech:IndustrialFurnace_Front_Unlit");
		faceIconLit  = iconRegister.registerIcon("madtech:IndustrialFurnace_Front_Lit");
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack)
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
	
	//func_149691_a - getIcon
	@Override
	public IIcon func_149691_a(int side, int metadata)
	{
		boolean isActive = ((metadata >> 3) == 1);
		int facing = (metadata & MASK_DIR);
		
		return (side == getSideFromFacing(facing) ? (isActive ? faceIconLit: faceIconUnlit): field_149761_L);
	}
	
	//func_149727_a - onBlockActivated
	@Override
	public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
																							//func_147438_o - getTileEntity
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore) world.func_147438_o(x, y, z);
		
		if(tileEntity != null)
		{
			if(!tileEntity.getIsValid())
			{
				if(tileEntity.checkIfProperlyFormed()){
					tileEntity.convertDummies();
					if(world.isRemote)
						//func_145747_a - addChatMessage
						player.func_145747_a(new ChatComponentText("Industrial Furnace Completed"));
				}
			}
			if(tileEntity.getIsValid())
				player.openGui(CityTech.instance, GuiIds.FURNACE_CORE, world, x, y, z);
		}
		return true;
	}
	
	//func_149734_b - randomDisplayTick
	@Override
	public void func_149734_b(World world, int x, int y, int z, Random random)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		if((metadata & META_ISACTIVE) == 0)
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

	//func_149915_a - createNewTileEntity
	@Override
	public TileEntity func_149915_a(World world, int var1) {
		
		return new TileEntityIndustrialFurnaceCore();
	}
	
	//func_149749_a - breakBlock
	@Override
	public void func_149749_a(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore) world.func_147438_o(x, y, z);
		if(tileEntity != null)
			tileEntity.invalidateMultiBlock();
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
		
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore)world.func_147438_o(x, y, z);
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
	
	

}
