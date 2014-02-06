package com.madpcgaming.citytech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.inventory.ContainerIndustrialFurnace;
import com.madpcgaming.citytech.lib.Textures;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;


public class GuiIndustrialFurnace extends GuiContainer
{

	public TileEntityIndustrialFurnaceCore	tileEntity;

	public GuiIndustrialFurnace(InventoryPlayer playerInventory, TileEntityIndustrialFurnaceCore tileEntity)
	{
		super(new ContainerIndustrialFurnace(playerInventory, tileEntity));
		this.tileEntity = tileEntity;
		ySize = 166;
	}
	//drawGuiContainerForegroundLayer = drawGuiContainerForegroundLayer
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		//fontRendererObj = fontRendererObj
        String invName = tileEntity.hasCustomInventoryName() ? tileEntity.getInventoryName() : StatCollector.translateToLocal(tileEntity.getInventoryName());
        fontRendererObj.drawString(invName, xSize / 2 - fontRendererObj.getStringWidth(invName) / 2, 6, 4210752);
		//fontRendererObj.drawString(StatCollector.translateToLocal(Strings.GUI_INDUSTRIAL_FURNACE),28, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 + 2, 4210752);
	}
	
	//drawGuiContainerBackgroundLayer = drawGuiContainerBackgroundLayer
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int par1, int par2)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);
		//mc = MC
		this.mc.getTextureManager().bindTexture(Textures.GUI_INDUSTRIAL_FURNACE);
		
		//width = width
		//height = height
		//xSize = xSize
		//ySize = ySize
		//field_147003_i = guiLeft
		//field_147009_r = guiTop
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int i1;
		
		if(tileEntity.isBurning())
		{
			i1 = tileEntity.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(x + 57, y + 36 + 23 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		
		i1 = tileEntity.getCookProgressScaled(24);
		drawTexturedModalRect(x + 83, y + 34, 176, 14, i1 + 1, 16);
	}

}
