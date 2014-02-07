package com.madpcgaming.citytech.piping.liquid;

import net.minecraftforge.fluids.FluidStack;

import com.madpcgaming.citytech.piping.AbstractPipingNetwork;

public class AbstractTankPipingNetwork<T extends AbstractTankPiping> extends AbstractPipingNetwork<ILiquidPiping, T>
{
	protected FluidStack liquidType;
	protected boolean fluidTypeLocked = false;
	
	protected AbstractTankPipingNetwork(Class<T> cl)
	{
		super(cl);
	}
	
	public FluidStack getFluidType()
	{
		return liquidType;
	}
	
	@Override
	public Class<ILiquidPiping> getBasePipingType()
	{
		return ILiquidPiping.class;
	}
	
	@Override
	public void addPiping(T pipe)
	{
		super.addPiping(pipe);
		pipe.setFluidType(liquidType);
		if(pipe.fluidTypeLocked && !fluidTypeLocked)
		{
			setFluidTypeLocked(true);
		}
	}
	
	public boolean setFluidType(FluidStack newType)
	{
		if(liquidType != null && liquidType.isFluidEqual(newType))
		{
			return false;
		}
		if(newType != null)
		{
			liquidType = newType.copy();
			liquidType.amount = 0;
		} else {
			liquidType = null;
		}
		for(AbstractTankPiping piping: pipes)
		{
			piping.setFluidType(liquidType);
		}
		return true;
	}
	
	public void setFluidTypeLocked(boolean fluidTypeLocked)
	{
		if(this.fluidTypeLocked == fluidTypeLocked)
		{
			return;
		}
		this.fluidTypeLocked = fluidTypeLocked;
		for(AbstractTankPiping piping : pipes)
		{
			piping.setFluidTypeLocked(fluidTypeLocked);
		}
	}
	
	public boolean canAcceptLiquid(FluidStack acceptable) {
	    return areFluidsCompatable(liquidType, acceptable);
	  }
	public static boolean areFluidsCompatable(FluidStack a, FluidStack b) {
	    if(a == null || b == null) {
	      return true;
	    }
	    return a.isFluidEqual(b);
	  }

	  public int getTotalVolume() {
	    int totalVolume = 0;
	    for (T pipe : pipes) {
	      totalVolume += pipe.getTank().getFluidAmount();
	    }
	    return totalVolume;
	  }

}
