package com.madpcgaming.citytech.gui;

import com.madpcgaming.citytech.lib.Textures;
import com.madpcgaming.citytech.render.RenderUtil;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public final class IconCT
{
	//TODO: MAKE TEXTURE FILE
	public static final IconCT	ACTIVE_TAB	= new IconCT(0,0);
	public static final IconCT	INACTIVE_TAB	= new IconCT(0,0);
	public static final IconCT	BUTTON_DISABLED	= new IconCT(0,0);
	public static final IconCT	BUTTON_HIGHLIGHT = new IconCT(0,0);
	public static final IconCT	BUTTON	= new IconCT(0,0);
	public static final IconCT	LEFT_ARROW	= new IconCT(0,0);
	public static final IconCT  RIGHT_ARROW = new IconCT(0,0);
	public static final IconCT	REDSTONE_MODE_ALWAYS = new IconCT(0,0);
	public static final IconCT	REDSTONE_MODE_NEVER	= new IconCT(0,0);
	public static final IconCT	REDSTONE_MODE_WITH_SIGNAL = new IconCT(0,0);
	public static final IconCT	REDSTONE_MODE_WITHOUT_SIGNAL = new IconCT(0,0);
	public static final IconCT	WRENCH_OVERLAY_POWER = new IconCT(0,0);
	public static final IconCT	WRENCH_OVERLAY_ITEM	= new IconCT(0,0);
	public static final IconCT	WRENCH_OVERLAY_FLUID = new IconCT(0,0);
	public static final IconCT	WRENCH_OVERLAY_REDSTONE	= new IconCT(0,0);
	public static final IconCT	BUTTON_DOWN_HIGHLIGHT = new IconCT(0,0);
	public static final IconCT	BUTTON_DOWN	= new IconCT(0,0);
	public static final IconCT	FILTER_WHITELIST = new IconCT(0,0);
	public static final IconCT	FILTER_META_OFF	= new IconCT(0,0);
	public static final IconCT	FILTER_META	= new IconCT(0,0);
	public static final IconCT	FILTER_STICKY_OFF = new IconCT(0,0);
	public static final IconCT	FILTER_STICKY = new IconCT(0,0);
	public static final IconCT	FILTER_NBT_OFF = new IconCT(0,0);
	public static final IconCT	FILTER_NBT	= new IconCT(0,0);
	public static final IconCT	FILTER_ORE_DICT_OFF	= new IconCT(0,0);
	public static final IconCT	FILTER_ORE_DICT = new IconCT(0,0);
	public static final IconCT	LOOP_OFF = new IconCT(0,0);
	public static final IconCT	LOOP = new IconCT(0,0);	
	public static final IconCT	FILTER_BLACKLIST = new IconCT(0,0);
	
	private static final int TEX_SIZE = 256;
	private static final double PIX_SIZE = 1d / TEX_SIZE;
	

	
	public final double minU;
	public final double maxU;
	public final double minV;
	public final double maxV;
	public final double width;
	public final double height;
	
	public static final ResourceLocation TEXTURE = Textures.GUI_WIDGET;
	
	


	public IconCT(int x, int y)
	{
		this(x, y, 16, 16);
	}
	
	public IconCT(int x, int y, int width, int height)
	{
		this(width, height, (float) (PIX_SIZE * x), (float) (PIX_SIZE * (x + width)), (float) (PIX_SIZE * y), (float) (PIX_SIZE * (y + height)));
	}
	
	public IconCT(double width, double height, double minU, double maxU, double minV, double maxV)
	{
		this.width = width;
		this.height = height;
		this.minU = minU;
		this.maxU = maxU;
		this.minV = minV;
		this.maxV = maxV;
	}
	
	public void renderIcon(double x, double y)
	{
		renderIcon(x,y, width, height, 0, false);
	}
	
	public void renderIcon(double x, double y, boolean doDraw)
	{
		renderIcon(x, y, width, height, 0, doDraw);
	}
	
	public void renderIcon(double x, double y, double width, double height, double zLevel, boolean doDraw)
	{
		Tessellator tessellator = Tessellator.instance;
		if(doDraw)
		{
			RenderUtil.bindTexture(TEXTURE);
			tessellator.startDrawingQuads();
		}
		tessellator.addVertexWithUV(x, y + height, zLevel, minU, maxV);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
	    tessellator.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
	    tessellator.addVertexWithUV(x, y + 0, zLevel, minU, minV);
	    if(doDraw)
	    {
	    	tessellator.draw();
	    }
	}
}
