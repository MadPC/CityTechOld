package com.madpcgaming.citytech.power;

import buildcraft.api.power.IPowerReceptor;
import cofh.api.energy.IEnergyHandler;

public class PowerHandlerUtil
{
	public static IPowerInterface create(Object o)
	{
		if(o instanceof IEnergyHandler)
		{
			return new PowerInterfaceRF((IEnergyHandler) o);			
		} 
		else  if (o instanceof IPowerReceptor)
		{
			return new PowerInterfaceBC((IPowerReceptor) o);
		}
	}
}
