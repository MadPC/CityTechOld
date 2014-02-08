package com.madpcgaming.citytech.piping.power;

import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.power.IInternalPowerReceptor;
import com.madpcgaming.citytech.power.IPowerInterface;

public interface IPowerPiping extends IInternalPowerReceptor, IPiping
{
	public static final String ICON_KEY = null;
	public static final String ICON_KEY_INPUT = null;
	public static final String ICON_KEY_OUTPUT = null;
	public static final String ICON_CORE_KEY = null;
	public static final String ICON_TRANSMISSION_KEY = null;
	public static final String COLOR_CONTROLLER_ID = null;
	
	IPowerInterface getExternalPowerReceptor(ForgeDirection direction);
	
}
