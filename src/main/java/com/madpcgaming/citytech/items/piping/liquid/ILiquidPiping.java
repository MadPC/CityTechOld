package com.madpcgaming.citytech.items.piping.liquid;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import com.madpcgaming.citytech.items.piping.IPiping;
import com.madpcgaming.citytech.lib.DyeColor;

public interface ILiquidPiping extends IPiping, IFluidHandler {

	  boolean canOutputToDir(ForgeDirection dir);

	  boolean isExtractingFromDir(ForgeDirection dir);

	  void setExtractionSignalColor(ForgeDirection dir, DyeColor col);

	  DyeColor getExtractionSignalColor(ForgeDirection dir);

	}
