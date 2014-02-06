package com.madpcgaming.citytech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.inventory.ContainerTable;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.lib.Textures;

public class TableGui extends GuiContainer {


	public TableGui(InventoryPlayer inventoryplayer, World world, int i,
			int j, int k) {
		super(new ContainerTable(inventoryplayer, world, i, j, k));
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal(Strings.GUI_WORKBENCH_NAME),28, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 - 14, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(Textures.GUI_WORKBENCH);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		
	}
}