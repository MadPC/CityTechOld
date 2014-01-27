package com.madpcgaming.madtech.energy;

/**
 * An energy storage is the unit of interaction with Energy Inventories.
 * 
 * Example: {@link EnergyStorage}
 * 
 * @author Advtech92
 *
 */
public interface IEnergyStorage
{
	/**
	 * 
	 * @param maxRecieve
	 *  Max Amount of energy to be inserted
	 * @param test
	 * if true, insertion will be tested only
	 * @return Amount of Energy that was (or would have been, if tested) accepted by the storage.
	 */
	int recieveEnergy(int maxRecieve, boolean test);
	/**
	 * 
	 * @param maxExtract
	 * Max amount to be removed
	 * @param test
	 * Tests the Storage
	 * @return
	 * Amount of Energy that was (or would have been, if tested) removed from the storage.
	 */
	int extractEnergy(int maxExtract, boolean test);
	/**
	 * 
	 * @return amount of energy currently in the unit.
	 */
	int getEnergyStored();
	/**
	 * 
	 * @return max amount that can be stored.
	 */
	int getMaxEnergyStored();
}
