package com.madpcgaming.citytech.piping.power;

import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.power.IInternalPowerReceptor;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.power.ITeslaBat;
import com.madpcgaming.citytech.util.DyeColor;

public interface IPowerPiping extends IInternalPowerReceptor, IPiping
{
	public static final String ICON_KEY = null;
	public static final String ICON_KEY_INPUT = null;
	public static final String ICON_KEY_OUTPUT = null;
	public static final String ICON_CORE_KEY = null;
	public static final String ICON_TRANSMISSION_KEY = null;
	public static final String COLOR_CONTROLLER_ID = null;
	
	IPowerInterface getExternalPowerReceptor(ForgeDirection direction);
	
	ITeslaBat getTeslaBat();
	
	float getMaxEnergyExtracted(ForgeDirection dir);
	
	float getMaxEnergyReceived(ForgeDirection dir);
	
	void setRedstoneMode(RedstoneControlMode mode, ForgeDirection dir);
	
	RedstoneControlMode getRedstoneMode(ForgeDirection dir);
	
	void setSignalColor(ForgeDirection dir, DyeColor col);
	
	DyeColor getSignalColor(ForgeDirection dir);
	
	IIcon getTextureForInputMode();
	
	IIcon getTextureForOutputMode();
	
	void onTick();
	
	float getEnergyStored();
	
	void setEnergyStored(float give);
}
