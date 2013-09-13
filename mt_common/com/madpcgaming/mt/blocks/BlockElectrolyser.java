package com.madpcgaming.mt.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.madpcgaming.mt.MadTech;
import com.madpcgaming.mt.lib.Reference;
import com.madpcgaming.mt.lib.Strings;
import com.madpcgaming.mt.lib.Utils;
import com.madpcgaming.mt.tileentitys.TileElectrolyser;

public class BlockElectrolyser extends BlockContainer
{
	
	protected BlockElectrolyser(int id)
	{
		super(id, Material.iron);
		
		this.setHardness(0.5F);
		this.setCreativeTab(MadTech.tabsMT);
		this.setUnlocalizedName(Strings.ELECTROLYSER_NAME);
		this.field_111026_f = this.getUnlocalizedName();
		this.func_111022_d(Reference.MOD_ID + ":" + this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileElectrolyser();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
	{
		
		super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
	}
	
	public boolean onBlockActived(World world, int i, int j, int k, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
	{
		if (entityPlayer.isSneaking())
		{
			return false;
		}
		
		ItemStack current = entityPlayer.inventory.getCurrentItem();
		if (current != null)
		{
			
			FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);
			
			TileElectrolyser tank = (TileElectrolyser) world.getBlockTileEntity(i, j, k);
			
			if (fluid != null)
			{
				int qty = tank.fill(ForgeDirection.UNKNOWN, fluid, true);
				
				if (qty != 0 && !entityPlayer.capabilities.isCreativeMode)
				{
					entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, Utils.consumeItem(current));
				}
				
				return true;
			} else
			{
				
				FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
				if (available != null)
				{
					ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
					
					fluid = FluidContainerRegistry.getFluidForFilledItem(filled);
					
					if (fluid != null)
					{
						if (!entityPlayer.capabilities.isCreativeMode)
						{
							if (current.stackSize > 1)
							{
								if (!entityPlayer.inventory.addItemStackToInventory(filled))
									return false;
								else
								{
									entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem,
											Utils.consumeItem(current));
								}
							} else
							{
								entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem,
										Utils.consumeItem(current));
								entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, filled);
							}
						}
						tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
						return true;
					} else
					{
						this.openGUI(entityPlayer, world, i, j, k);
						return true;
					}
				} else
				{
					// Stupid way to make guis work ...
					this.openGUI(entityPlayer, world, i, j, k);
					return true;
				}
			}
		} else
		{
			this.openGUI(entityPlayer, world, i, j, k);
			return true;
		}
	}
	
	private void openGUI(EntityPlayer entityPlayer, World w, int x, int y, int z)
	{
		entityPlayer.openGui(MadTech.instance, 0, w, x, y, z);
	}
	
}
