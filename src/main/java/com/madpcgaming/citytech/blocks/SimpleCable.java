package com.madpcgaming.citytech.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.helpers.LogHelper;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.power.IEnergyConductor;
import com.madpcgaming.citytech.tileentitys.CableTE;

public class SimpleCable extends BlockContainer
{
	
	protected SimpleCable()
	{
		super(Material.iron);
		this.setCreativeTab(CityTech.tabsCT);
		this.setHardness(1.0f);
		this.setBlockName(Strings.SIMPLE_CABLE_NAME);
		this.setResistance(0.5f);
	}
	
	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int var1)
	{
		return new CableTE();
	}
	
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9)
	{
		if (!par1World.isRemote)
		{
			LogHelper.info("&&%s %s %s %s %s %s %s %s %s Block rightcliked with Item %s in hand!", par1World, par2, par3, par4,
					par5EntityPlayer, par6, par7, par8, par9, par5EntityPlayer.getCurrentEquippedItem());
			ItemStack i = par5EntityPlayer.getCurrentEquippedItem();
			TileEntity t = par1World.getTileEntity(par2, par3, par4);
			if (i != null)
			{
				if (i.getItem() == ModItems.wireTester)
				{
					IEnergyConductor l = (IEnergyConductor) t;
					l.transferTo(ForgeDirection.UNKNOWN, 5.0f);
				}
			} else
			{
				CableTE t2 = (CableTE) t;
				LogHelper.info("&&Got %f power and I need %f power.", t2.getEnergyLevel(), t2.getRequested());
			}
		}
		return false;
	}
}
