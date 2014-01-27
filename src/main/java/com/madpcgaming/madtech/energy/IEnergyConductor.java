package com.madpcgaming.madtech.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyConductor
{
	/**
	 * Transfers power <b>to</b> this Cable.
	 * 
	 * @param d
	 *            Direction of power coming in
	 * @param power
	 *            The power to be transfered
	 * @return Leftover power that can't be stored anymore.
	 */
	public float transferTo(ForgeDirection d, float power);
	
	/**
	 * Requests power <b>from</b> this Cable.
	 * 
	 * @param d
	 *            The Direction the Request came from
	 * @param power
	 *            The amountRequested.
	 */
	public void requestFrom(ForgeDirection d, float power);
	
	public float getEnergyLevel();
}
