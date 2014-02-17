package com.madpcgaming.citytech.piping.gui;

import com.madpcgaming.citytech.gui.IconCT;

import net.minecraft.client.gui.GuiButton;

public interface ISettingPanel
{
	void onGuiInit(int x, int y, int width, int height);
	
	void deactivate();
	
	IconCT getIcon();
	
	void render(float par1, int par2, int par3);
	
	void actionPerformed(GuiButton button);
}
