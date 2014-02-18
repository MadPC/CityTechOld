package com.madpcgaming.citytech.piping.gui;

import net.minecraft.client.gui.GuiButton;

import com.madpcgaming.citytech.gui.ColorButton;
import com.madpcgaming.citytech.gui.IconCT;
import com.madpcgaming.citytech.gui.RedstoneModeButton;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.machine.IRedstoneModeControlable;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.power.IPowerPiping;

public class PowerSetting extends BaseSettingsPanel
{
	private static final int ID_REDSTONE_BUTTON = 796;
	private static final int ID_COLOR_BUTTON = 797;
	private IPowerPiping pipes;
	private RedstoneModeButton rsB;
	private ColorButton colorB;
	
	protected PowerSetting(final GuiExternalConnection gui, IPiping pipe)
	{
		super(IconCT.WRENCH_OVERLAY_POWER, Strings.POWER_PIPING_NAME, gui, pipe);
		pipes = (IPowerPiping) pipe;
		
		int x = 38;
		int y = customTop;
		
		rsB = new RedstoneModeButton(gui, ID_REDSTONE_BUTTON, x, y, new IRedstoneModeControlable() {

		      @Override
		      public void setRedstoneControlMode(RedstoneControlMode mode) {
		        RedstoneControlMode curMode = getRedstoneControlMode();
		        pipes.setRedstoneMode(mode, gui.dir);
		        if(curMode != mode) {
		        	//TODO: Needed?
		          //Packet pkt = ConduitPacketHandler.createExtractionModePacket(conduit, gui.dir, mode);
		          //PacketDispatcher.sendPacketToServer(pkt);
		        }

		      }

		      @Override
		      public RedstoneControlMode getRedstoneControlMode() {
		        return pipes.getRedstoneMode(gui.dir);
		      }
		    });

		    x += rsB.getWidth() + gap;
		    colorB = new ColorButton(gui, ID_COLOR_BUTTON, x, y);
		    colorB.setToolTipHeading(Lang.localize("gui.conduit.redstone.signalColor"));
		    colorB.setColorIndex(pipes.getSignalColor(gui.dir).ordinal());

		  }

		  @Override
		  public void actionPerformed(GuiButton guiButton) {
		    super.actionPerformed(guiButton);
		    if(guiButton.id == ID_COLOR_BUTTON) {
		    	//TODO: Needed?
		      //Packet pkt = ConduitPacketHandler.createSignalColorPacket(conduit, gui.dir, DyeColor.values()[colorB.getColorIndex()]);
		      //PacketDispatcher.sendPacketToServer(pkt);
		    }
		  }

		  @Override
		  protected void initCustomOptions() {
		    super.initCustomOptions();
		    rsB.onGuiInit();
		    colorB.onGuiInit();
		  }

		  @Override
		  public void deactivate() {
		    super.deactivate();
		    rsB.detach();
		    colorB.detach();
		  }
		}
