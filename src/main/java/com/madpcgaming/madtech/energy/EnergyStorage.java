package com.madpcgaming.madtech.energy;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage
{
	protected int	energy;
	protected int	capacity;
	protected int	maxReceive;
	protected int	maxExtract;

	public EnergyStorage(int capacity)
	{
		this(capacity, capacity, capacity);
	}

	public EnergyStorage(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public EnergyStorage func_145839_a(NBTTagCompound nbt)
	{
		this.energy = nbt.getInteger("Energy");

		if (energy > capacity) {
			energy = capacity;
		}
		return this;
	}

	public NBTTagCompound func_145841_b(NBTTagCompound nbt)
	{
		if (energy < 0) {
			energy = 0;
		}
		nbt.setInteger("Energy", energy);
		return nbt;
	}

	public void setCapacity(int capacity)
	{
		this.capacity = capacity;

		if (energy > capacity) {
			energy = capacity;
		}
	}

	public void setMaxTransfer(int maxTransfer)
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
	}

	public int getMaxReceive()
	{
		return maxReceive;
	}

	public int getMaxExtract()
	{
		return maxExtract;
	}

	/**
	 * Allows for server to client sync. Do not call externally to the
	 * containing TileEntity. Not all IEnergyHandlers will have it.
	 * 
	 * @param energy
	 */
	public void setEnergyStored(int energy)
	{
		this.energy = energy;
		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}
	
	public void modifyEnergyStored(int energy)
	{
		this.energy += energy;
		
		if(this.energy > capacity)
		{
			this.energy = capacity;
		}
		else if(this.energy < 0)
		{
			this.energy = 0;
		}
	}

	/* IEnergyStorage*/
	@Override
	public int recieveEnergy(int maxRecieve, boolean test)
	{
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
		if(!test){
			energy += energyReceived;
		}
		return energyReceived;
	}

	/* IEnergyStorage*/
	@Override
	public int extractEnergy(int maxExtract, boolean test)
	{
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		
		if(!test)
		{
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	/* IEnergyStorage*/
	@Override
	public int getEnergyStored()
	{
		return energy;
	}

	/* IEnergyStorage*/
	@Override
	public int getMaxEnergyStored()
	{
		return capacity;
	}

}
