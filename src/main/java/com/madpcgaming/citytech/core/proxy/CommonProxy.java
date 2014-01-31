package com.madpcgaming.citytech.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.client.gui.GuiIndustrialFurnace;
import com.madpcgaming.citytech.client.gui.GuiSimpleEFurnace;
import com.madpcgaming.citytech.client.gui.TableGui;
import com.madpcgaming.citytech.helpers.LogHelper;
import com.madpcgaming.citytech.inventory.ContainerIndustrialFurnace;
import com.madpcgaming.citytech.inventory.ContainerSimpleEFurnace;
import com.madpcgaming.citytech.inventory.ContainerTable;
import com.madpcgaming.citytech.lib.GuiIds;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.citytech.tileentitys.TileSimpleEFurnace;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy implements IGuiHandler
{
	
	public void registerRenderings()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(CityTech.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		LogHelper.info("Getting Server GUI!");
		world.func_147438_o(x, y, z);
		/*if (t instanceof TileElectrolyser)
		{
			return new ElectrolyserContainer(player.inventory, (TileElectrolyser) t);
		}*/
		if (ID == GuiIds.FURNACE_CORE)
		{
			TileEntityIndustrialFurnaceCore tileIndustrialFurnaceCore = (TileEntityIndustrialFurnaceCore) world.func_147438_o(x, y, z);
			return new ContainerIndustrialFurnace(player.inventory, tileIndustrialFurnaceCore);
		}
		else if (ID == GuiIds.FURNACE_SIMPLE)
		{
			TileSimpleEFurnace tileEntity = (TileSimpleEFurnace) world.func_147438_o(x, y, z);
			return new ContainerSimpleEFurnace(player.inventory, tileEntity);
		}
		else if (ID == GuiIds.TABLE)
		{
			return new ContainerTable(player.inventory, world, x, y, z);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		LogHelper.info("Getting Client GUI!");
		world.func_147438_o(x, y, z);
		/*if (t instanceof TileElectrolyser)
		{
			return new ElectrolyserGUI(new ElectrolyserContainer(player.inventory, (TileElectrolyser) t), (TileElectrolyser) t);
		}*/
		if (ID == GuiIds.FURNACE_CORE)
		{
			//WHY DO I DECLARE T IF YOU DONT USE IT?
			TileEntityIndustrialFurnaceCore tileIndustrialFurnaceCore = (TileEntityIndustrialFurnaceCore) world.func_147438_o(x, y, z);
			return new GuiIndustrialFurnace(player.inventory, tileIndustrialFurnaceCore);
		}
		else if (ID == GuiIds.FURNACE_SIMPLE)
		{
			TileSimpleEFurnace tileEntity = (TileSimpleEFurnace) world.func_147438_o(x, y, z);
			return new GuiSimpleEFurnace(player.inventory, tileEntity);
		}
		else if (ID == GuiIds.TABLE)
			return new TableGui(player.inventory, world, x, y, z);
		return null;
	}
}
