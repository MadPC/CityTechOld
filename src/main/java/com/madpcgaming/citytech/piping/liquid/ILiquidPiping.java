package com.madpcgaming.citytech.piping.liquid;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.util.DyeColor;

public interface ILiquidPiping extends IPiping, IFluidHandler
{
	boolean canOutputToDir(ForgeDirection dir);
	
	boolean isExtractingFromDir(ForgeDirection dir);
	
	void setExtractionRedstoneMode(RedstoneControlMode mode, ForgeDirection dir);
	
	RedstoneControlMode getExtractionRedstoneMode(ForgeDirection dir);
	
	void setExtractionSignalColor(ForgeDirection dir, DyeColor col);
	
	DyeColor getExtractionSignalColor(ForgeDirection dir);
}
