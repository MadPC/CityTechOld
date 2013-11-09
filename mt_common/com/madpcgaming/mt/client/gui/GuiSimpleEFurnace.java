package com.madpcgaming.mt.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.mt.helpers.LocalizationHelper;
import com.madpcgaming.mt.inventory.ContainerSimpleEFurnace;
import com.madpcgaming.mt.lib.Textures;
import com.madpcgaming.mt.tileentitys.TileSimpleEFurnace;

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
	}
	
	/**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = this.tile.isInvNameLocalized() ? this.tile.getInvName() : LocalizationHelper.getLocalizedString(this.tile.getInvName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Textures.GUI_SIMPLE_EFURNACE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (this.tile.isBurning())
        {
            i1 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.tile.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}
	
}
