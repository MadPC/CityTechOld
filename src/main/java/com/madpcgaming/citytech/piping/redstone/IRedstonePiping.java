package com.madpcgaming.citytech.piping.redstone;

import java.util.Set;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.util.DyeColor;

public interface IRedstonePiping extends IPiping
{
	public static final String KEY_PIPING_ICON = "CityTech:redstonePiping";
	public static final String KEY_TRANSMISSION_ICON = "CityTech:redstonePipingTransmission";
	public static final String KEY_CORE_OFF_ICON = "CityTech:redstonePipingCoreOff";
	public static final String KEY_CORE_ON_ICON = "CityTech:redstonePipingCoreOn";
	
	int isProvidingStrongPower(ForgeDirection toDirection);
	
	int isProvidingWeakPower(ForgeDirection toDirection);
	
	Set<Signal> getNetworkInputs();
	
	Set<Signal> getNetworkInputs(ForgeDirection side);
	
	Set<Signal> getNetworkOutputs(ForgeDirection side);
	
	DyeColor getSignalColor(ForgeDirection dir);
	
	void updateNetwork();
	
	int[] getOutputValues(World world, int x, int y, int z, ForgeDirection side);
	
	int getOutputValue(World world, int x, int y, int z, ForgeDirection side, int subnet);
}
