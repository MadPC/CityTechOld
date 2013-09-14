package com.madpcgaming.mt.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.mt.lib.Strings;
import com.madpcgaming.mt.tileentitys.ContainerIndustrialFurnace;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceCore;

public class GuiIndustrialFurnace extends GuiContainer
{

	private TileEntityIndustrialFurnaceCore tileEntity;
	
	
	public GuiIndustrialFurnace(InventoryPlayer playerInventory, TileEntityIndustrialFurnaceCore tileEntity)
	{
		super(new ContainerIndustrialFurnace(playerInventory, tileEntity));
		
		this.tileEntity = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		final String invTitle = Strings.GUI_INDUSTRIAL_FURNACE;
		fontRenderer.drawString(invTitle, xSize / 2 - fontRenderer.getStringWidth(invTitle)/ 2, 6, 4210752);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);
		//func_110577_a = bindTexture?
		
		//this.mc.getTextureManager().bindTexture();
	}

}
