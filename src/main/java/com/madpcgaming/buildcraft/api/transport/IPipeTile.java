package com.madpcgaming.buildcraft.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public interface IPipeTile extends IFluidHandler, ISolidSideTile
{
	public enum PipeType
	{
		ITEM, FLUID, POWER, STRUCTURE;
	}
	
	PipeType getPipeType();
	
	int injectItem(ItemStack stack, boolean doAdd, ForgeDirection from);
	
	boolean isPipeConnected(ForgeDirection with);
}
