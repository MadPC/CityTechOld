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
		field_147000_g = 166;
	}
	//func_146979_b = drawGuiContainerForegroundLayer
	@Override
	protected void func_146979_b(int x, int y)
	{
		//field_146289_q = fontRendererObj
        String invName = tileEntity.func_145818_k_() ? tileEntity.func_145825_b() : StatCollector.translateToLocal(tileEntity.func_145825_b());
        field_146289_q.drawString(invName, field_146999_f / 2 - field_146289_q.getStringWidth(invName) / 2, 6, 4210752);
		//field_146289_q.drawString(StatCollector.translateToLocal(Strings.GUI_INDUSTRIAL_FURNACE),28, 6, 4210752);
		field_146289_q.drawString(StatCollector.translateToLocal("container.inventory"), 6, field_147000_g - 96 + 2, 4210752);
	}
	
	//func_146976_a = drawGuiContainerBackgroundLayer
	@Override
	protected void func_146976_a(float f, int par1, int par2)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);
		//field_146297_k = MC
		this.field_146297_k.getTextureManager().bindTexture(Textures.GUI_INDUSTRIAL_FURNACE);
		
		//field_146294_l = width
		//field_146295_m = height
		//field_146999_f = xSize
		//field_147000_g = ySize
		//field_147003_i = guiLeft
		//field_147009_r = guiTop
		int x = (field_146294_l - field_146999_f) / 2;
		int y = (field_146295_m - field_147000_g) / 2;
		drawTexturedModalRect(x, y, 0, 0, field_146999_f, field_147000_g);
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
