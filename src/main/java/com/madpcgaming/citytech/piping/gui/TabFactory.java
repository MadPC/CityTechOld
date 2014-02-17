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
			//TODO: CREATE SETTINGS, REMOVE NULL
			return null;//new PowerSetting(gui, pipe);
		} 
		else if(baseType.isAssignableFrom(ILiquidPiping.class))
		{
			//TODO: CREATE SETTINGS, REMOVE NULL
			return null;//new LiquidSetting(gui, pipe);
		}
		else if(baseType.isAssignableFrom(IItemPiping.class))
		{
			//TODO: CREATE SETTINGS, REMOVE NULL
			return null;//new ItemSetting(gui, pipe);
		}
		else if (baseType.isAssignableFrom(IRedstonePiping.class))
		{
			//TODO: CREATE SETTINGS, REMOVE NULL
			return null;//new RedstoneSetting(gui, pipe);
		}
		return null;
	}
}
