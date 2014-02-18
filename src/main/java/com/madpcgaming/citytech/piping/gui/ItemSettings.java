package com.madpcgaming.citytech.piping.gui;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.network.Packet;
import cofh.api.transport.IItemConduit;

import com.madpcgaming.citytech.gui.ColorButton;
import com.madpcgaming.citytech.gui.IconButtonCT;
import com.madpcgaming.citytech.gui.IconCT;
import com.madpcgaming.citytech.gui.RedstoneModeButton;
import com.madpcgaming.citytech.gui.ToggleButtonCT;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.machine.IRedstoneModeControlable;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.item.IItemPiping;
import com.madpcgaming.citytech.piping.item.ItemFilter;
import com.madpcgaming.citytech.render.ColorUtil;
import com.madpcgaming.citytech.render.RenderUtil;
import com.madpcgaming.citytech.util.DyeColor;

public class ItemSettings extends BaseSettingsPanel
{

	private static final int NEXT_FILTER_ID	= 98932;

	private static final int ID_REDSTONE_BUTTON = 12614;

	private static final int ID_COLOR_BUTTON = 179816;

	private static final int ID_WHITELIST = 17;
	private static final int ID_NBT	= 18;
	private static final int ID_META = 19;
	private static final int ID_ORE_DICT = 20;
	private static final int ID_STICKY = 21;
	private static final int ID_LOOP = 22;
	private static final int ID_CHANNEL = 23;

	private IItemPiping itemPiping;

	private String inputHeading = "Extraction Filter";
	private String outputHeading = "Insertion Filter";

	private IconButtonCT nextFilterB;

	private ToggleButtonCT useMetaB;
	private ToggleButtonCT useNbtB;
	private IconButtonCT whiteListB;
	private ToggleButtonCT useOreDictB;
	private ToggleButtonCT stickyB;
	private ColorButton channelB;

	private ToggleButtonCT loopB;

	private RedstoneModeButton	rsB;
	private ColorButton colorB;

	boolean inOutShowIn = false;

	boolean isAdvanced;

	private ItemFilter activeFilter;

	protected ItemSettings(final GuiExternalConnection gui, IPiping pipe)
	{
		super(IconCT.WRENCH_OVERLAY_ITEM, Strings.ITEM_PIPING_NAME, gui, pipe);
		itemPiping = (IItemPiping) pipe;
		isAdvanced = itemPiping.getMetaData() == 1;

		int x = 98;
		int y = customTop;

		nextFilterB = new IconButtonCT(gui, NEXT_FILTER_ID, x, y,
				IconCT.RIGHT_ARROW);
		nextFilterB.setSize(8, 16);

		x = 112;
		rsB = new RedstoneModeButton(gui, ID_REDSTONE_BUTTON, x, y,
				new IRedstoneModeControlable()
				{

					@Override
					public void setRedstoneControlMode(RedstoneControlMode mode)
					{
						RedstoneControlMode curMode = getRedstoneControlMode();
						itemPiping.setExtractionRedstoneMode(mode, gui.dir);
						if (curMode != mode) {
							// Packet pkt = ConduitPacketHandler.createExtractionModePacket(itemPiping, gui.dir, mode);
							// PacketDispatcher.sendPacketToServer(pkt);
						}

					}

					@Override
					public RedstoneControlMode getRedstoneControlMode()
					{
						return itemPiping.getExtractionRedstoneMode(gui.dir);
					}
				});

		x += rsB.getWidth() + gap;
		colorB = new ColorButton(gui, ID_COLOR_BUTTON, x, y);
		colorB.setColorIndex(itemPiping.getExtractionSignalColor(gui.dir)
				.ordinal());
		colorB.setToolTipHeading(Lang.localize("gui.conduit.item.sigCol"));

		x = 112;
		y = 66;
		whiteListB = new IconButtonCT(gui, ID_WHITELIST, x, y, IconCT.FILTER_WHITELIST);
		whiteListB.setToolTip(Lang.localize("gui.conduit.item.whitelist"));

		x += 20;
		useMetaB = new ToggleButtonCT(gui, ID_META, x, y,IconCT.FILTER_META_OFF, IconCT.FILTER_META);
		useMetaB.setSelectedToolTip(Lang.localize("gui.conduit.item.matchMetaData"));
		useMetaB.setUnselectedToolTip(Lang.localize("gui.conduit.item.ignoreMetaData"));
		useMetaB.setPaintSelectedBorder(false);

		x += 20;
		stickyB = new ToggleButtonCT(gui, ID_STICKY, x, y,	IconCT.FILTER_STICKY_OFF, IconCT.FILTER_STICKY);
		String[] lines = Lang.localizeList("gui.conduit.item.stickyEnabled");
		stickyB.setSelectedToolTip(lines);
		stickyB.setUnselectedToolTip(Lang.localize("gui.conduit.item.stickyDisbaled"));
		stickyB.setPaintSelectedBorder(false);

		y += 20;
		x = 112;

		channelB = new ColorButton(gui, ID_CHANNEL, x, y);
		channelB.setColorIndex(0);
		channelB.setToolTipHeading(Lang.localize("gui.conduit.item.channel"));

		x += 20;
		useNbtB = new ToggleButtonCT(gui, ID_NBT, x, y,IconCT.FILTER_NBT_OFF, IconCT.FILTER_NBT);
		useNbtB.setSelectedToolTip(Lang.localize("gui.conduit.item.matchNBT"));
		useNbtB.setUnselectedToolTip(Lang.localize("gui.conduit.item.ignoreNBT"));
		useNbtB.setPaintSelectedBorder(false);

		x += 20;
		useOreDictB = new ToggleButtonCT(gui, ID_ORE_DICT, x, y, IconCT.FILTER_ORE_DICT_OFF, IconCT.FILTER_ORE_DICT);
		useOreDictB.setSelectedToolTip(Lang.localize("gui.conduit.item.oreDicEnabled"));
		useOreDictB.setUnselectedToolTip(Lang.localize("gui.conduit.item.oreDicDisabled"));
		useOreDictB.setPaintSelectedBorder(false);

		// x += 20;
		y = customTop;
		loopB = new ToggleButtonCT(gui, ID_LOOP, x, y, IconCT.LOOP_OFF, IconCT.LOOP);
		loopB.setSelectedToolTip(Lang.localize("gui.conduit.item.selfFeedEnabled"));
		loopB.setUnselectedToolTip(Lang.localize("gui.conduit.item.selfFeedDisabled"));
		loopB.setPaintSelectedBorder(false);

	}

	private String getHeading()
	{
		ConnectionMode mode = pipe.getConnectionMode(gui.dir);
		if (mode == ConnectionMode.DISABLED) {
			return "";
		}
		if (mode == ConnectionMode.OUTPUT) {
			return outputHeading;
		}
		if (mode == ConnectionMode.INPUT || inOutShowIn) {
			return inputHeading;
		}
		return outputHeading;
	}

	@Override
	protected void initCustomOptions()
	{
		updateGuiVisibility();
	}

	private void updateGuiVisibility()
	{

		deactivate();

		boolean showInput = false;
		boolean showOutput = false;

		ConnectionMode mode = pipe.getConnectionMode(gui.dir);
		if (mode == ConnectionMode.INPUT) {
			showInput = true;
			gui.container.setInputSlotsVisible(true);
			gui.container.setOutputSlotsVisible(false);
			gui.container.setInventorySlotsVisible(true);
		}
		else if (mode == ConnectionMode.OUTPUT) {
			showOutput = true;
			gui.container.setInputSlotsVisible(false);
			gui.container.setOutputSlotsVisible(true);
			gui.container.setInventorySlotsVisible(true);
		}
		else if (mode == ConnectionMode.IN_OUT) {

			if (nextFilterB != null) {
				nextFilterB.onGuiInit();
			}
			showInput = inOutShowIn;
			showOutput = !inOutShowIn;

		}

		if (!showInput && !showOutput) {
			activeFilter = null;
		}
		else {
			gui.container.setInventorySlotsVisible(true);
			if (showInput) {
				activeFilter = itemPiping.getInputFilter(gui.dir);
				gui.container.setInputSlotsVisible(true);
				gui.container.setOutputSlotsVisible(false);
			}
			else {
				activeFilter = itemPiping.getOutputFilter(gui.dir);
				gui.container.setInputSlotsVisible(false);
				gui.container.setOutputSlotsVisible(true);
			}
		}

		updateButtons();

	}

	private void updateButtons()
	{
		if (activeFilter == null || useNbtB == null) {
			return;
		}

		ConnectionMode mode = pipe.getConnectionMode(gui.dir);
		if (mode == ConnectionMode.DISABLED) {
			return;
		}
		boolean outputActive = (mode == ConnectionMode.IN_OUT && !inOutShowIn)
				|| (mode == ConnectionMode.OUTPUT);
		int chanCol;
		if (outputActive) {
			stickyB.onGuiInit();
			stickyB.setSelected(activeFilter.isSticky());

			chanCol = itemPiping.getOutputColor(gui.dir).ordinal();
		}
		else {

			rsB.onGuiInit();
			rsB.setMode(itemPiping.getExtractionRedstoneMode(gui.dir));

			colorB.onGuiInit();
			colorB.setColorIndex(itemPiping.getExtractionSignalColor(gui.dir)
					.ordinal());

			chanCol = itemPiping.getInputColor(gui.dir).ordinal();
		}

		channelB.onGuiInit();
		channelB.setColorIndex(chanCol);

		if (isAdvanced) {
			useNbtB.onGuiInit();
			useNbtB.setSelected(activeFilter.isMatchNBT());

			useOreDictB.onGuiInit();
			useOreDictB.setSelected(activeFilter.isUseOreDict());
		}

		useMetaB.onGuiInit();
		useMetaB.setSelected(activeFilter.isMatchMeta());

		whiteListB.onGuiInit();
		if (activeFilter.isBlacklist()) {
			whiteListB.setIcon(IconCT.FILTER_BLACKLIST);
			whiteListB.setToolTip(Lang.localize("gui.conduit.item.blacklist"));
		}
		else {
			whiteListB.setIcon(IconCT.FILTER_WHITELIST);
			whiteListB.setToolTip(Lang.localize("gui.conduit.item.whitelist"));
		}

		if (mode == ConnectionMode.IN_OUT) {
			loopB.onGuiInit();
			loopB.setSelected(itemPiping.isSelfFeedEnabled(gui.dir));
		}

	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		super.actionPerformed(guiButton);
		if (guiButton.id == NEXT_FILTER_ID) {
			inOutShowIn = !inOutShowIn;
			updateGuiVisibility();
		}

		if (activeFilter == null) {
			return;
		}
		if (guiButton.id == ID_META) {
			activeFilter.setMatchMeta(useMetaB.isSelected());
			sendFilterChange();
		}
		else if (guiButton.id == ID_NBT) {
			activeFilter.setMatchNBT(useNbtB.isSelected());
			sendFilterChange();
		}
		else if (guiButton.id == ID_STICKY) {
			activeFilter.setSticky(stickyB.isSelected());
			sendFilterChange();
		}
		else if (guiButton.id == ID_ORE_DICT) {
			activeFilter.setUseOreDict(useOreDictB.isSelected());
			sendFilterChange();
		}
		else if (guiButton.id == ID_WHITELIST) {
			activeFilter.setBlacklist(!activeFilter.isBlacklist());
			sendFilterChange();
		}
		else if (guiButton.id == ID_COLOR_BUTTON) {
			//Packet pkt = ConduitPacketHandler.createSignalColorPacket(itemPiping, gui.dir,DyeColor.values()[colorB.getColorIndex()]);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
		else if (guiButton.id == ID_LOOP) {
			itemPiping.setSelfFeedEnabled(gui.dir, !itemPiping.isSelfFeedEnabled(gui.dir));
			//Packet pkt = ConduitPacketHandler.createItemLoopPacket(itemPiping, gui.dir);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
		else if (guiButton.id == ID_CHANNEL) {

			ConnectionMode mode = pipe.getConnectionMode(gui.dir);
			if (mode == ConnectionMode.IN_OUT) {
				mode = inOutShowIn ? ConnectionMode.INPUT: ConnectionMode.OUTPUT;
			}

			DyeColor col = DyeColor.values()[channelB.getColorIndex()];
			boolean input;
			if (mode == ConnectionMode.INPUT) {
				col = DyeColor.values()[channelB.getColorIndex()];
				itemPiping.setInputColor(gui.dir, col);
				input = true;
			}
			else if (mode == ConnectionMode.OUTPUT) {
				itemPiping.setOutputColor(gui.dir,
						DyeColor.values()[channelB.getColorIndex()]);
				input = false;
			}
			else {
				return;
			}
			//Packet pkt = ConduitPacketHandler.createItemChannelPacket(itemPiping, gui.dir, col, input);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
	}

	private void sendFilterChange()
	{
		updateButtons();

		if (activeFilter != null) {
			ConnectionMode mode = pipe.getConnectionMode(gui.dir);
			boolean inputActive = (mode == ConnectionMode.IN_OUT && inOutShowIn) || (mode == ConnectionMode.INPUT);
			//Packet pkt = ConduitPacketHandler.createItemFilterPacket(itemPiping, gui.dir, inputActive, activeFilter);
			//PacketDispatcher.sendPacketToServer(pkt);
		}
	}

	@Override
	protected void connectionModeChanged(ConnectionMode conectionMode)
	{
		super.connectionModeChanged(conectionMode);
		updateGuiVisibility();
	}

	@Override
	protected void renderCustomOptions(int top, float par1, int par2, int par3)
	{
		ConnectionMode mode = pipe.getConnectionMode(gui.dir);
		if (mode != ConnectionMode.DISABLED) 
		{
			if (itemPiping.getMetaData() == 0) 
			{
				RenderUtil.bindTexture("enderio:textures/gui/itemFilter.png");
			}else {
				RenderUtil.bindTexture("enderio:textures/gui/itemFilterAdvanced.png");
			}
			gui.drawTexturedModalRect(gui.getGuiLeft(), gui.getGuiTop() + 55, 0, 55, gui.getXSize(), 145);

			FontRenderer fr = gui.getFontRenderer();
			String heading = getHeading();
			int headingWidth = fr.getStringWidth(heading);
			int x = 0;
			int rgb = ColorUtil.getRGB(Color.darkGray);
			fr.drawString(heading, left + x, top, rgb);
		}

	}

	@Override
	public void deactivate()
	{
		gui.container.setInventorySlotsVisible(false);
		gui.container.setInputSlotsVisible(false);
		gui.container.setOutputSlotsVisible(false);
		rsB.detach();
		colorB.detach();
		channelB.detach();
		useNbtB.detach();
		useMetaB.detach();
		useOreDictB.detach();
		whiteListB.detach();
		stickyB.detach();
		loopB.detach();
		nextFilterB.detach();
	}

}