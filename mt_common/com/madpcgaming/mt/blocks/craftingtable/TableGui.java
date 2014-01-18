package com.madpcgaming.mt.blocks.craftingtable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class TableGui extends GuiContainer {

	public void registerIcons(IconRegister iconRegister)
	{
		Icon blockIcon = iconRegister.registerIcon("mt:Table");
	}

	public TableGui(InventoryPlayer inventoryplayer, World world, int i,
			int j, int k) {
		super(new ContainerTable(inventoryplayer, world, i, j, k));
	}

	private static TextureManager textureManager = func_110434_K(); //get the TextureManager instance

	public void onGuiClosed() {
		super.onGuiClosed();
	}

	private static TextureManager func_110434_K() {
	
		return null;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(
				StatCollector.translateToLocal("\u00a76Better"), 120, 5,
				0x404040);
		this.fontRenderer.drawString(
				StatCollector.translateToLocal("\u00a76Crafting"), 116, 20,
				0x404040);
		// this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
		// 8, this.ySize - 96 - 14, 0x404040);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
}