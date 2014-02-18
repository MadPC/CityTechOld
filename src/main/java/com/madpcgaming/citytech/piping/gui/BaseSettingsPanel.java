package com.madpcgaming.citytech.piping.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.gui.IconButtonCT;
import com.madpcgaming.citytech.gui.IconCT;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.render.ColorUtil;

public class BaseSettingsPanel implements ISettingPanel
{
	
	static final int PREV_MODE_B = 327;
	static final int NEXT_MODE_B = 328;
	
	protected final IconCT icon;
	protected final GuiExternalConnection gui;
	protected final IPiping pipe;
	protected final String typeName;
	
	protected IconButtonCT leftArrow;
	protected IconButtonCT rightArrow;
	protected String modeLabel;
	
	protected int left = 0;
	protected int top = 0;
	protected int width = 0;
	protected int height = 0;
	
	protected int gap = 5;
	
	protected int customTop = 0;
	
	protected BaseSettingsPanel(IconCT icon, String typeName, GuiExternalConnection gui, IPiping pipe)
	{
		this.icon = icon;
		this.typeName = typeName;
		this.gui = gui;
		this.pipe = pipe;
		
		modeLabel = Lang.localize("gui.piping.ioMode");
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int x = gap * 3 + fr.getStringWidth(modeLabel);
		int y = gap * 3 + fr.FONT_HEIGHT;
		
		leftArrow = new IconButtonCT(gui, PREV_MODE_B, x, y, IconCT.LEFT_ARROW);
		leftArrow.setSize(8, 16);
		x += leftArrow.getWidth() + gap + getLongestModeStringWidth() + gap;
		
		rightArrow = new IconButtonCT(gui, NEXT_MODE_B, x, y, IconCT.RIGHT_ARROW);
		rightArrow.setSize(8, 16);
		customTop = top + gap * 5 + fr.FONT_HEIGHT * 2;
	}

	@Override
	public void onGuiInit(int x, int y, int width, int height)
	{
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		
		leftArrow.onGuiInit();
		rightArrow.onGuiInit();
		
		FontRenderer fr = gui.getFontRenderer();
		connectionModeChanged(pipe.getConnectionMode(gui.dir));
		
		initCustomOptions();
	}
	
	protected void initCustomOptions()
	{
		
	}
	

	@Override
	public void deactivate()
	{
		
	}

	@Override
	public IconCT getIcon()
	{
		return icon;
	}
	
	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if(guiButton.id == PREV_MODE_B)
		{
			pipe.setConnectionMode(gui.dir, pipe.getPreviousConnectionMode(gui.dir));
			//TODO: Does the code below need removed or updated?
			//Packet pkt = PipingPacketHandler.createConnectionModePack(gui.bundle, pipe, gui.dir);
			//PacketDispatcher.sendPacketToServer(pkt);
			connectionModeChanged(pipe.getConnectionMode(gui.dir));
		} 
		else if(guiButton.id == NEXT_MODE_B)
		{
			pipe.setConnectionMode(gui.dir, pipe.getNextConnectionMode(gui.dir));
			//TODO: Does the code below need removed or updated?
			//Packet pkt = PipingPacketHandler.createConnectionModePack(gui.bundle, pipe, gui.dir);
			//PacketDispatcher.sendPacketToServer(pkt);
			connectionModeChanged(pipe.getConnectionMode(gui.dir));
		}
	}
	
	protected void connectionModeChanged(ConnectionMode connectionMode)
	{
		
	}
	
	@Override
	public void render(float par1, int par2, int par3) {
	    FontRenderer fr = gui.getFontRenderer();

	    int rgb = ColorUtil.getRGB(Color.darkGray);
	    int x = left + (width - fr.getStringWidth(getTypeName())) / 2;

	    fr.drawString(getTypeName(), x, top, rgb);

	    x = left;
	    int y = top + gap + fr.FONT_HEIGHT + gap;
	    gui.getFontRenderer().drawString(modeLabel, x, y, rgb);

	    String modeString = pipe.getConnectionMode(gui.dir).getLocalizedName();
	    x += gap + leftArrow.getWidth() + fr.getStringWidth(modeLabel) + gap;

	    GL11.glColor3f(1, 1, 1);
	    IconCT icon = new IconCT(10, 60, 64, 20);
	    icon.renderIcon(x - gap, y - (fr.FONT_HEIGHT / 2) - 1, getLongestModeStringWidth() + gap * 2, leftArrow.getHeight(), 0, true);

	    int move = (getLongestModeStringWidth() - fr.getStringWidth(modeString)) / 2;
	    x += move;
	    rgb = ColorUtil.getRGB(Color.white);
	    gui.getFontRenderer().drawString(modeString, x, y, rgb);

	    renderCustomOptions(y + gap + fr.FONT_HEIGHT + gap, par1, par2, par3);
	}
	
	 protected void renderCustomOptions(int top, float par1, int par2, int par3) {

	  }

	  private int getLongestModeStringWidth() {
	    int maxWidth = 0;
	    for (ConnectionMode mode : ConnectionMode.values()) {
	      int width = gui.getFontRenderer().getStringWidth(mode.getLocalizedName());
	      if(width > maxWidth) {
	        maxWidth = width;
	      }
	    }
	    return maxWidth;
	  }

	  protected String getTypeName() {
	    return typeName;
	  }
}
