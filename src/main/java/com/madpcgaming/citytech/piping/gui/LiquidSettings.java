package com.madpcgaming.citytech.piping.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;

import com.madpcgaming.citytech.gui.ColorButton;
import com.madpcgaming.citytech.gui.IconCT;
import com.madpcgaming.citytech.gui.RedstoneModeButton;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.machine.IRedstoneModeControlable;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.render.ColorUtil;

public class LiquidSettings extends BaseSettingsPanel
{

	static final int ID_REDSTONE_BUTTON	= 16;

	private static final int ID_COLOR_BUTTON = 17;

	private RedstoneModeButton rsB;

	private ColorButton colorB;

	private String autoExtractStr = Lang.localize("gui.conduit.fluid.autoExtract");

	private ILiquidPiping pipe;

	protected LiquidSettings(final GuiExternalConnection gui, IPiping con)
	{
		super(IconCT.WRENCH_OVERLAY_FLUID, Strings.LIQUID_PIPING_NAME, 	gui, con);

		pipe = (ILiquidPiping) con;

		int x = gap + gui.getFontRenderer().getStringWidth(autoExtractStr) + gap * 2;
		int y = customTop;

		rsB = new RedstoneModeButton(gui, ID_REDSTONE_BUTTON, x, y,	new IRedstoneModeControlable()
		{

			@Override
			public void setRedstoneControlMode(RedstoneControlMode mode)
			{
				RedstoneControlMode curMode = getRedstoneControlMode();
				pipe.setExtractionRedstoneMode(mode, gui.dir);
				if (curMode != mode) 
				{
					//TODO: needed? if so update.
					//Packet pkt = ConduitPacketHandler.createExtractionModePacket(pipe,gui.dir, mode);
					//PacketDispatcher.sendPacketToServer(pkt);
				}

			}

			@Override
			public RedstoneControlMode getRedstoneControlMode()
			{
				return pipe.getExtractionRedstoneMode(gui.dir);
			}
		});

		x += rsB.getWidth() + gap;
		colorB = new ColorButton(gui, ID_COLOR_BUTTON, x, y);
		colorB.setToolTipHeading(Lang
				.localize("gui.conduit.redstone.signalColor"));
		colorB.setColorIndex(pipe.getExtractionSignalColor(gui.dir)
				.ordinal());
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		super.actionPerformed(guiButton);
		if (guiButton.id == ID_COLOR_BUTTON) 
		{
			//Packet pkt = ConduitPacketHandler.createSignalColorPacket(conduit, gui.dir, DyeColor.values()[colorB.getColorIndex()]);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
	}

	@Override
	protected void connectionModeChanged(ConnectionMode conectionMode)
	{
		super.connectionModeChanged(conectionMode);
		if (conectionMode == ConnectionMode.INPUT) {
			rsB.onGuiInit();
			colorB.onGuiInit();
		}
		else {
			gui.removeButton(rsB);
			gui.removeButton(colorB);
		}

	}

	@Override
	public void deactivate()
	{
		super.deactivate();
		rsB.setToolTip((String[]) null);
		colorB.setToolTip((String[]) null);
	}

	@Override
	protected void renderCustomOptions(int top, float par1, int par2, int par3)
	{
		if (pipe.getConnectionMode(gui.dir) == ConnectionMode.INPUT) {
			int x = gui.getGuiLeft() + gap
					+ gui.getFontRenderer().getStringWidth(autoExtractStr)
					+ gap + 2;
			int y = customTop;
			gui.getFontRenderer().drawString(autoExtractStr, left, top,	ColorUtil.getRGB(Color.DARK_GRAY));
		}
	}

}