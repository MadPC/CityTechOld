package com.madpcgaming.citytech.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPowerInterface 
{

  Object getDelegate();

  boolean canPipingConnect(ForgeDirection direction);

  float getEnergyStored(ForgeDirection dir);

  float getMaxEnergyStored(ForgeDirection dir);

  float getPowerRequest(ForgeDirection dir);

  float getMinEnergyReceived(ForgeDirection dir);

  float receiveEnergy(ForgeDirection opposite, float canOffer);

}