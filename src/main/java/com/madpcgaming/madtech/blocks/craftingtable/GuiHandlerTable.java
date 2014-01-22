package com.madpcgaming.madtech.blocks.craftingtable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.madpcgaming.madtech.blocks.ModBlocks;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerTable implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile_entity = world.func_147438_o (x, y, z);
		switch (id) {
		case 0:
			return id == 0 && world.func_147439_a(x, y, z) == ModBlocks.CraftingTable ? new ContainerTable(
					player.inventory, world, x, y, z) : null;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile_entity = world.func_147438_o (x, y, z);
		switch (id) {
		case 0:
			return id == 0
					&& world.func_147439_a(x, y, z) == ModBlocks.CraftingTable ? new TableGui(
					player.inventory, world, x, y, z) : null;
		}
		return null;
	}
}