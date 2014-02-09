package com.madpcgaming.citytech.blocks.multiblocks;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.lib.GuiIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;

public class IndustrialFurnaceCore extends BlockContainer {
	public static final int META_ISACTIVE = 0x00000008;
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;
	
	private IIcon faceIconUnlit;
	private IIcon faceIconLit;

	public IndustrialFurnaceCore(int id) {
		super(Material.rock);
		setBlockName(Strings.FURNACECORE_NAME);
		setHardness(3.5f);
		setCreativeTab(CityTech.tabsCT);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return ((world.getBlockMetadata(x, y, z) >> 3) == 0 ? 0 :15);
	}
	
	public void registerIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("citytech:brick");
		faceIconUnlit = iconRegister.registerIcon("citytech:IndustrialFurance_Front_Unlit");
		faceIconLit = iconRegister.registerIcon("citytech:IndustrialFurnace_Front_Lit");
	}
	

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack){
		int metadata = 0;
		int facing = META_DIR_WEST;
		
		int dir = MathHelper.floor_double((double)(entity.rotationYaw * 4f /360f) + 0.5) & 3;
		if(dir == 0)
			facing = META_DIR_NORTH;
		if(dir == 1)
			facing = META_DIR_EAST;
		if(dir == 2)
			facing = META_DIR_SOUTH;
		if (dir == 3)
			facing = META_DIR_WEST;
		
		metadata |=  facing;
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
	}
	
	public IIcon getIcon(int side, int metadata)
	{
		boolean isActive = ((metadata >> 3) == 1);
		int facing = (metadata & MASK_DIR);
		
		return (side == getSideFromFacing(facing) ? (isActive ? faceIconLit : faceIconUnlit) : blockIcon);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;
		
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore)world.getTileEntity(x, y, z);
		
		if(tileEntity != null)
		{
			if(!tileEntity.getIsValid())
			{
				if(tileEntity.checkIfProperlyFormed())
				{
					tileEntity.convertDummies();
					if(world.isRemote)
						player.addChatMessage(new ChatComponentText("Industrial Furnace Complete!"));
				}
			}
			
			if(tileEntity.getIsValid())
				player.openGui(CityTech.instance, GuiIds.FURNACE_CORE, world, x, y, z);
		}
		
		return true;
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random prng)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		if((metadata & META_ISACTIVE) == 0)
			return;
		
		int facing = metadata & MASK_DIR;
		
		double yMod = (0.3 * prng.nextDouble());
		double xMod = -0.02;
		double zMod = (0.75 - (0.5 * prng.nextDouble()));
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
	public TileEntity createNewTileEntity(World world, int var1) {
		return new TileEntityIndustrialFurnaceCore();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore)world.getTileEntity(x, y, z);
		
		if(tileEntity != null)
			tileEntity.invalidateMultiBlock();
		
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
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
		
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore)world.getTileEntity(x, y, z);
		if(tileEntity == null)
			return;
		
		for(int slot = 0; slot < tileEntity.getSizeInventory(); slot++)
		{
			ItemStack item = tileEntity.getStackInSlot(slot);
			
			if(item != null && item.stackSize > 0)
			{
				float rx = random.nextFloat() * 0.8f + 0.1f;
				float ry = random.nextFloat() * 0.8f + 0.1f;
				float rz = random.nextFloat() * 0.8f + 0.1f;
				
				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, item.copy());
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

}
