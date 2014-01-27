package com.madpcgaming.madtech.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.madpcgaming.madtech.energy.IEnergyConductor;

public class EnergyReader extends ItemMT
{
	
	public EnergyReader(int par1)
	{
		super();
		this.maxStackSize = 1;
	}
	
	/**
	 * This is called when the item is used, before the block is activated.
	 * 
	 * @param stack
	 *            The Item Stack
	 * @param player
	 *            The Player that used the item
	 * @param world
	 *            The Current World
	 * @param x
	 *            Target X Position
	 * @param y
	 *            Target Y Position
	 * @param z
	 *            Target Z Position
	 * @param side
	 *            The side of the target hit
	 * @return Return true to prevent any further processing.
	 */
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ)
	{
		if (world.isRemote)
			return false;
		TileEntity t = world.func_147438_o(x, y, z);
		if (t != null && t instanceof IEnergyConductor)
		{
			IEnergyConductor en = (IEnergyConductor) t;
			player.func_145747_a(new ChatComponentText(String.format("This Device currently holds %.2f %s", en.getEnergyLevel(),("general.Energy.name"))));
			return true;
		}
		return false;
	}
}
