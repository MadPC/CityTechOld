package com.madpcgaming.mt.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.madpcgaming.mt.client.gui.GuiIndustrialFurnace;
import com.madpcgaming.mt.lib.GuiIds;
import com.madpcgaming.mt.tileentitys.CableTE;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.mt.tileentitys.renderers.CableRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{

	public void registerRenderings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(CableTE.class,
				new CableRenderer());
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntityIndustrialFurnaceCore tileEntity = (TileEntityIndustrialFurnaceCore)world.getBlockTileEntity(x, y, z);
		if(tileEntity != null && id == GuiIds.FURNACE_CORE)
			return new GuiIndustrialFurnace(player.inventory, tileEntity);
		return null;
	}
	
}
