package com.madpcgaming.citytech.energy;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implement this interface on TileEntites which should handle energy, generally storing it in one or more ways internal {@link IEnergyStorage} Objects.
 * 
 *  Example here: {@link TileEnergyHandler}
 * 
 * @author Advtech92
 *
 */
public interface IEnergyHandler
{
	/**
	 * Add energy to an IEnergyHandler, internal networking is left entirely to the IEnergyHandler
	 * @param from
	 *             Orientation the energy is received from.
	 * @param maxReceive
	 *             Maximum amount of energy to receive.
	 * @param test
	 *             if True, the change will only be tested.
	 * @return Amount of energy that was (or would have been, if tested) received.
	 */
	int receiveEnergy(ForgeDirection from, int maxReceive, boolean test);
	/**
	 * Remove energy from an IEnergyHandler, internal networking is left entirely to the IEnergyHandler
	 * @param from
	 *             Orientation the energy is extracted from 
	 * @param maxExtract
	 *             Maximum amount of energy to be extracted
	 * @param test
	 *             if True, the extraction will only be tested.
	 * @return Amount of energy that was (or woud have been, if tested) extracted.
	 */
	int extractEnergy(ForgeDirection from, int maxExtract, boolean test);
	/**
	 * Returns true if the Handler functions on a given side - if a Tile Entity can receive or send energy on a given side, this should return true.
	 */
	boolean canInterface(ForgeDirection from);
	/**
	 * Returns the amount of energy currently stored
	 */
	int getEnergyStored(ForgeDirection from);
	/**
	 * Returns the maximum amount of energy that can be stored
	 */
	int getMaxEnergyStored(ForgeDirection from);
}
