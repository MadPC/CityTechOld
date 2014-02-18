package com.madpcgaming.citytech.gui;

import net.minecraft.client.Minecraft;

import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.machine.IRedstoneModeControlable;
import com.madpcgaming.citytech.machine.RedstoneControlMode;

public class RedstoneModeButton extends IconButtonCT
{
	private static IconCT[] ICONS = new IconCT[] 
	{
		IconCT.REDSTONE_MODE_ALWAYS, IconCT.REDSTONE_MODE_WITH_SIGNAL, IconCT.REDSTONE_MODE_WITHOUT_SIGNAL, IconCT.REDSTONE_MODE_NEVER
	};
	
	private static final String TOOLTIP_HEADING = Lang.localize("gui.tooltip.redstoneControlMode");
	
	IRedstoneModeControlable model;
	RedstoneControlMode curMode;
	
	public RedstoneModeButton(IGuiScreen gui, int id, int x, int y, IRedstoneModeControlable model)
	{
		super(gui, id, x, y, ICONS[model.getRedstoneControlMode().ordinal()]);
		this.model = model;
		curMode = model.getRedstoneControlMode();
		setToolTip(TOOLTIP_HEADING, curMode.tooltip);
		setIcon(ICONS[curMode.ordinal()]);
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int par2, int par3)
	{
		boolean result = super.mousePressed(mc, par2, par3);
		if(result)
		{
			nextMode();
		}
		return result;
	}
	
	private void nextMode()
	{
		if(curMode == null)
		{
			curMode = RedstoneControlMode.ON;
		}
		setMode(curMode.next());
	}
	
	public void setMode(RedstoneControlMode mode)
	{
		if(mode == curMode)
		{
			return;
		}
		curMode = mode;
		setToolTip(TOOLTIP_HEADING, mode.tooltip);
		setIcon(ICONS[mode.ordinal()]);
		model.setRedstoneControlMode(mode);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		super.drawButton(mc, mouseX, mouseY);
	}
}
