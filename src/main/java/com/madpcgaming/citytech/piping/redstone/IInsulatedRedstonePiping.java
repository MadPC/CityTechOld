package com.madpcgaming.citytech.piping.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.util.DyeColor;

public interface IInsulatedRedstonePiping extends IRedstonePiping
{
	static final String	KEY_INS_CONDUIT_ICON = null;
	static final String	KEY_INS_CORE_OFF_ICON = null;
	static final String	KEY_INS_CORE_ON_ICON = null;

	public static final String	COLOR_CONTROLLER_ID		= "ColorController";

	public void onInputsChanged(ForgeDirection side, int[] inputValues);

	public void onInputChanged(ForgeDirection side, int inputValue);

	public void forceConnectionMode(ForgeDirection dir, ConnectionMode mode);

	void setSignalColor(ForgeDirection dir, DyeColor col);
}
