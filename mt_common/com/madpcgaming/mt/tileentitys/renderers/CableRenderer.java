package com.madpcgaming.mt.tileentitys.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.mt.lib.Reference;
import com.madpcgaming.mt.models.ModelbasicCable;
import com.madpcgaming.mt.tileentitys.CableTE;

import cpw.mods.fml.client.FMLClientHandler;

public class CableRenderer extends TileEntitySpecialRenderer
{
	private ModelbasicCable	m;
	
	public CableRenderer()
	{
		m = new ModelbasicCable();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		this.renderModelAt((CableTE) tileentity, d0, d1, d2, f);
	}
	
	public void renderModelAt(CableTE t, double d, double d1, double d2, float f)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
		GL11.glRotatef(180F, 0F, 0F, 1F);
		
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "/textures/gui/test.png"));
		GL11.glPushMatrix();
		m.renderModel(t, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
