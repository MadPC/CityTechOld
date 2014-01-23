package com.madpcgaming.madtech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.madtech.inventory.ContainerSimpleEFurnace;
import com.madpcgaming.madtech.lib.Strings;
import com.madpcgaming.madtech.lib.Textures;
import com.madpcgaming.madtech.tileentitys.TileSimpleEFurnace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSimpleEFurnace extends GuiContainer
{
	
	private TileSimpleEFurnace tile;
	
	public GuiSimpleEFurnace(InventoryPlayer playerinv, TileSimpleEFurnace tile)
	{
		super(new ContainerSimpleEFurnace(playerinv, tile));
		this.tile = tile;
		field_147000_g = 166;
	}
	
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	final String invTitle = Strings.GUI_SIMPLE_E_FURNACE;
        this.field_146289_q.drawString(invTitle, field_146999_f / 2 - this.field_146289_q.getStringWidth(invTitle) / 2, 6, 4210752);
        this.field_146289_q.drawString(I18n.getStringParams("container.inventory"), 8, field_147000_g - 96 + 2, 4210752);
    }
	
	@Override
	protected void func_146976_a(float f, int par1, int par2)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_146297_k.renderEngine.bindTexture(Textures.GUI_SIMPLE_EFURNACE);
        int k = (this.field_146294_l - field_146999_f) / 2;
        int l = (this.field_146295_m - field_147000_g) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, field_146999_f, field_147000_g);
        int i1;

        if (this.tile.isBurning())
        {
            i1 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.tile.getCookProgressScaled(12);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}
	
}
