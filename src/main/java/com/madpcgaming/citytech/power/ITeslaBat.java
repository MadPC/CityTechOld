package com.madpcgaming.citytech.power;

public interface ITeslaBat
{
	int getMinEnergyReceived();

	int getMaxEnergyReceived();

	int getMaxEnergyStored();

	int getMinActivationEnergy();

	int getPowerLoss();

	int getPowerLossRegularity();

	int getMaxEnergyExtracted();
}
