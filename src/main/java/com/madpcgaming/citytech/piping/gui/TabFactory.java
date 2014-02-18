package com.madpcgaming.citytech.piping.gui;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.item.IItemPiping;
import com.madpcgaming.citytech.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.piping.power.IPowerPiping;
import com.madpcgaming.citytech.piping.redstone.IRedstonePiping;

public class TabFactory
{
	public static final TabFactory instance = new TabFactory();
	
	private TabFactory()
	{
		
	}
	
	public ISettingPanel createPanelForPiping(GuiExternalConnection gui, IPiping pipe)
	{
		Class<? extends IPiping> baseType = pipe.getBasePipingType();
		if(baseType.isAssignableFrom(IPowerPiping.class))
		{
			return new PowerSettings(gui, pipe);
		} 
		else if(baseType.isAssignableFrom(ILiquidPiping.class))
		{
			return new LiquidSettings(gui, pipe);
		}
		else if(baseType.isAssignableFrom(IItemPiping.class))
		{
			return new ItemSettings(gui, pipe);
		}
		else if (baseType.isAssignableFrom(IRedstonePiping.class))
		{
			return new RedstoneSettings(gui, pipe);
		}
		return null;
	}
}
