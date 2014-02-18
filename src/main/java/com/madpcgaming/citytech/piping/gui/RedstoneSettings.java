package com.madpcgaming.citytech.piping.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;

import com.madpcgaming.citytech.gui.ColorButton;
import com.madpcgaming.citytech.gui.IconCT;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.redstone.IInsulatedRedstonePiping;
import com.madpcgaming.citytech.render.ColorUtil;
import com.madpcgaming.citytech.util.DyeColor;

public class RedstoneSettings extends BaseSettingsPanel
{

	private static final int ID_COLOR_BUTTON	= 163;
	private ColorButton	pb;

	private String signalColorStr	= Lang.localize("gui.conduit.redstone.color");
	private IInsulatedRedstonePiping insCon;

	public RedstoneSettings(GuiExternalConnection gui, IPiping con)
	{
		super(IconCT.WRENCH_OVERLAY_REDSTONE, Strings.REDSTONE_PIPING_NAME, gui, con);

		int x = gap + gui.getFontRenderer().getStringWidth(signalColorStr) + gap + 2;
		int y = customTop;
		pb = new ColorButton(gui, ID_COLOR_BUTTON, x, y);
		pb.setToolTipHeading(Lang.localize("gui.piping.redstone.signalColor"));
		if (con instanceof IInsulatedRedstonePiping) 
		{
			insCon = (IInsulatedRedstonePiping) con;
			DyeColor sigCol = insCon.getSignalColor(gui.dir);
			pb.setColorIndex(sigCol.ordinal());
		}
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		super.actionPerformed(guiButton);
		if (guiButton.id == ID_COLOR_BUTTON)
		{
			//Packet pkt = ConduitPacketHandler.createSignalColorPacket(insCon, gui.dir, DyeColor.values()[pb.getColorIndex()]);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
	}

	@Override
	protected void initCustomOptions()
	{
		if (insCon != null) 
		{
			pb.setColorIndex(pb.getColorIndex());
			pb.onGuiInit();
		}
	}

	@Override
	public void deactivate()
	{
		super.deactivate();
		pb.setToolTip((String[]) null);
	}

	@Override
	protected void renderCustomOptions(int top, float par1, int par2, int par3)
	{
		if (insCon != null) 
		{
			gui.getFontRenderer().drawString(signalColorStr, left, top, ColorUtil.getRGB(Color.darkGray));
		}
	}

}