package com.madpcgaming.citytech.gui;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.render.RenderUtil;

public class IconButtonCT extends GuiButton
{
	public static final int DEFAULT_WIDTH = 16;
	public static final int DEFAULT_HEIGHT = 16;
	
	protected IconCT icon;
	protected ResourceLocation texture;
	private int xOrigin;
	private int yOrigin;
	
	protected IGuiScreen gui;
	protected GuiToolTip toolTip;
	private int marginX = 0;
	private int marginY = 0;
	
	public IconButtonCT(IGuiScreen gui, int id, int x, int y, IconCT icon)
	{
		super(id, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
		this.gui = gui;
		this.icon = icon;
		texture = IconCT.TEXTURE;
		this.xOrigin = x;
		this.yOrigin = y;
	}
	
	public void setToolTip(String... toolTipText)
	{
		if(toolTip == null)
		{
			toolTip = new GuiToolTip(new Rectangle(xOrigin, yOrigin, width, height), toolTipText);
			gui.addToolTip(toolTip);
			toolTip.setBounds(new Rectangle(xPosition, yPosition, width, height));
		} else {
			toolTip.setToolTipText(toolTipText);
		}
	}
	
	public void onGuiInit()
	{
		gui.addButton(this);
		if(toolTip != null)
		{
			gui.addToolTip(toolTip);
		}
		xPosition = xOrigin + gui.getGuiLeft();
		yPosition = yOrigin + gui.getGuiTop();
	}
	
	public void detach()
	{
		gui.removeToolTip(toolTip);
		gui.removeButton(this);
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		if(toolTip != null)
		{
			toolTip.setBounds(new Rectangle(xPosition, yPosition, width, height));
		}
	}
	
	public void setIconMargin(int x, int y)
	{
		marginX = x;
		marginY = y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public IconCT getIcon()
	{
		return icon;
	}
	
	public void setIcon(IconCT icon)
	{
		this.icon = icon;
	}
	
	@Override
	 public void drawButton(Minecraft mc, int mouseX, int mouseY) {
	    if(field_146123_n) {

	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	      this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + width
	          && mouseY < this.yPosition + height;
	      int hoverState = getHoverState(this.field_146123_n);
	      mouseDragged(mc, mouseX, mouseY);

	      IconCT background = getIconForHoverState(hoverState);

	      RenderUtil.bindTexture(texture);
	      GL11.glColor3f(1, 1, 1);

	      Tessellator tes = Tessellator.instance;
	      tes.startDrawingQuads();

	      int x = xPosition;
	      int y = yPosition;

	      GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	      GL11.glEnable(GL11.GL_BLEND);
	      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	      background.renderIcon(x, y, width, height, 0, false);
	      if(icon != null) {
	        icon.renderIcon(x + marginX, y + marginY, width - (2 * marginX), height - (2 * marginY), 0, false);
	      }

	      tes.draw();

	      GL11.glPopAttrib();

	    }
	  }
	
	 protected IconCT getIconForHoverState(int hoverState) {
		    if(hoverState == 0) {
		      return IconCT.BUTTON_DISABLED;
		    }
		    if(hoverState == 2) {
		      return IconCT.BUTTON_HIGHLIGHT;
		    }
		    return IconCT.BUTTON;
		  }

}
