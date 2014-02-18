package com.madpcgaming.citytech.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemDye;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.util.DyeColor;

public class ColorButton extends IconButtonCT
{
	private int colorIndex = 0;
	private String toolTipPrefix = "";
	
	public ColorButton(IGuiScreen gui, int id, int x, int y)
	{
		super(gui, id, x, y, null);
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int par2, int par3)
	{
		boolean result = super.mousePressed(mc, par2, par3);
		if(result)
		{
			nextColor();
		}
		return result;
	}
	
	public String getToolTipPrefix()
	{
		return toolTipPrefix;
	}
	
	public void setToolTipHeading(String toolTipPrefix)
	{
		if(toolTipPrefix == null)
		{
			this.toolTipPrefix = "";
		} else {
			this.toolTipPrefix = toolTipPrefix;
		}
	}
	
	private void nextColor()
	{
		colorIndex++;
		if(colorIndex >= ItemDye.field_150922_c.length)
		{
			colorIndex = 0;
		}
		setColorIndex(colorIndex);
	}
	
	public int getColorIndex()
	{
		return colorIndex;
	}
	
	public void setColorIndex(int colorIndex)
	{
		this.colorIndex = MathHelper.clamp_int(colorIndex, 0, ItemDye.field_150922_c.length - 1);
		String colStr = DyeColor.DYE_ORE_LOCAL_NAMES[colorIndex];
		if(toolTipPrefix != null && toolTipPrefix.length() > 0)
		{
			setToolTip(toolTipPrefix, colStr);
		} else {
			setToolTip(colStr);
		}
	}
	
	@Override
	  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
	    super.drawButton(mc, mouseX, mouseY);
	    if(field_146123_n) {
	      Tessellator tes = Tessellator.instance;
	      tes.startDrawingQuads();

	      int x = xPosition + 2;
	      int y = yPosition + 2;

	      GL11.glDisable(GL11.GL_TEXTURE_2D);

	      int col = ItemDye.field_150922_c[colorIndex];
	      tes.setColorOpaque_I(col);
	      tes.addVertex(x, y + height - 4, zLevel);
	      tes.addVertex(x + width - 4, y + height - 4, zLevel);
	      tes.addVertex(x + width - 4, y + 0, zLevel);
	      tes.addVertex(x, y + 0, zLevel);

	      tes.draw();

	      GL11.glEnable(GL11.GL_TEXTURE_2D);

	    }
	  }
}
