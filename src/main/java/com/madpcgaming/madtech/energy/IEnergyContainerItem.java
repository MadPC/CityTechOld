package com.madpcgaming.madtech.energy;

import net.minecraft.item.ItemStack;

/**
 * Implement this interface on Item classes that support external manipulation of their internal energy storages.
 * 
 * A example: {@link ItemEnergyContainer}
 * 
 * @author advtech92
 *
 */
public interface IEnergyContainerItem
{
	/**
	 * Adds energy to container item. Returns the quantity of energy that was accepted. This should always return 0 if the item cannot be externally charged.
	 * @param container
	 * @param maxReceive
	 * @param test
	 * @return
	 */
	int receiveEnergy(ItemStack container, int maxReceive, boolean test);
	/**
	 * Removes energy from a container item. Returns the quantity of energy that was removed. This should always return 0 if the item cannot be externally
	 * discharged.
	 * @param container
	 * 			ItemStack to be discharged.
	 * @param maxExtract
	 * 			Maximum amount of energy to be extracted from the item.
	 * @param test
	 * 			if True, the discharge was only tested.
	 * @return Amount of energy that was (or would have been, if tested) extracted from the item.
	 */
	int extractEnergy(ItemStack container, int maxExtract, boolean test);
	/**
	 * Get the amount of energy currently stored in the container Item.
	 */
	int getEnergyStored(ItemStack container);
	/**
	 *  Get the max amount of energy that can be stored in the container item
	 */
	int getMaxEnergyStored(ItemStack container);
}
