package com.madpcgaming.mt.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import com.madpcgaming.mt.tileentitys.TileElectrolyser;

public class ElectrolyserGUI extends GuiContainer
{
	
	TileElectrolyser	tile;
	
	public ElectrolyserGUI(Container par1Container, TileElectrolyser t)
	{
		super(par1Container);
		tile = t;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		
	}
	
}
