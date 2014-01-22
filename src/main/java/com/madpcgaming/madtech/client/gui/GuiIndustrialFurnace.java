package com.madpcgaming.madtech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import com.madpcgaming.madtech.inventory.ContainerIndustrialFurnace;
import com.madpcgaming.madtech.lib.Strings;
import com.madpcgaming.madtech.lib.Textures;
import com.madpcgaming.madtech.tileentitys.TileEntityIndustrialFurnaceCore;


public class GuiIndustrialFurnace extends GuiContainer
{

	public TileEntityIndustrialFurnaceCore	tileEntity;

	public GuiIndustrialFurnace(InventoryPlayer playerInventory, TileEntityIndustrialFurnaceCore tileEntity)
	{
		super(new ContainerIndustrialFurnace(playerInventory, tileEntity));
		this.tileEntity = tileEntity;
	}
	
	@Override
	protected void func_146979_b(int x, int y)
	{
		field_146289_q.drawString(StatCollector.translateToLocal(Strings.GUI_INDUSTRIAL_FURNACE),28, 6, 4210752);
		field_146289_q.drawString(StatCollector.translateToLocal("container.inventory"), 8, 176 - 96 + 2, 4210752);
	}

	@Override
	protected void func_146976_a(float f, int par1, int par2)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);

		this.field_146297_k.getTextureManager().bindTexture(Textures.GUI_INDUSTRIAL_FURNACE);

		int x = (field_146294_l - par1) / 2;
		int y = (field_146295_m - par2) / 2;
		drawTexturedModalRect(x, y, 0, 0, par1, par2);
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
