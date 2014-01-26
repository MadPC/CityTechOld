package com.madpcgaming.madtech.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.madtech.inventory.ContainerTable;
import com.madpcgaming.madtech.lib.Strings;
import com.madpcgaming.madtech.lib.Textures;

public class TableGui extends GuiContainer {


	public TableGui(InventoryPlayer inventoryplayer, World world, int i,
			int j, int k) {
		super(new ContainerTable(inventoryplayer, world, i, j, k));
		field_147000_g = 166;
	}

	@Override
	protected void func_146979_b(int par1, int par2) {
		this.field_146289_q.drawString(StatCollector.translateToLocal(Strings.GUI_WORKBENCH_NAME),28, 6, 4210752);
		this.field_146289_q.drawString(StatCollector.translateToLocal("container.inventory"), 8, field_147000_g - 96 - 14, 4210752);
	}

	@Override
	protected void func_146976_a(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_146297_k.getTextureManager().bindTexture(Textures.GUI_WORKBENCH);
		int l = (field_146294_l - field_146999_f) / 2;
		int i1 = (field_146295_m - field_147000_g) / 2;
		drawTexturedModalRect(l, i1, 0, 0, field_146999_f, field_147000_g);
		
	}
}