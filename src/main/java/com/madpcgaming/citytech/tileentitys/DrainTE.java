package com.madpcgaming.citytech.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.energy.EnergyHandler;
import com.madpcgaming.citytech.energy.IEnergyConductor;

public class DrainTE extends TileEntity implements IEnergyConductor
{
	
	private EnergyHandler	energy;
	
	public DrainTE()
	{
		super();
		energy = new EnergyHandler(this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public float transferTo(ForgeDirection d, float power)
	{
		// Just redirection, but could be fancier
		return energy.placeEnegry(d, power);
	}
	
	@Override
	public void requestFrom(ForgeDirection d, float power)
	{
		boolean now = energy.requestEnergy(d, power);
		
		// if transfer directly
		if (now)
		{
			ForgeDirection opposite = d.getOpposite();
			// calculate the offset to call transferTo
			int ofX = opposite.offsetX;
			int ofY = opposite.offsetY;
			int ofZ = opposite.offsetZ;
			// get the TileEntity
			TileEntity ent = this.worldObj.getTileEntity(this.xCoord + ofX, this.yCoord + ofY, this.zCoord + ofZ);
			// if it exists and is a Cable (or other object)
			if (ent != null && ent instanceof IEnergyConductor)
			{
				IEnergyConductor c = (IEnergyConductor) ent;
				// Transfer the power
				float leftover = c.transferTo(opposite, power);
				// and use this style to remove power from yourself
				energy.placeEnegry(ForgeDirection.UNKNOWN, -(power - leftover));
			}
		}
	}
	
	private byte	tickCount	= 0;
	
	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			energy.update(this.worldObj);
			tickCount++;
			if (tickCount == 6)
			{
				tickCount = 0;
				for (int i = 0; i < 6; i++)
				{
					ForgeDirection d = ForgeDirection.getOrientation(i);
					int X = this.xCoord + d.offsetX;
					int Y = this.yCoord + d.offsetY;
					int Z = this.zCoord + d.offsetZ;
					TileEntity t = this.worldObj.getTileEntity(X, Y, Z);
					if (t != null && t instanceof IEnergyConductor)
					{
						((IEnergyConductor) t).requestFrom(d.getOpposite(), 5.0f);
					}
				}
			}
		}
	}
	
	@Override
	public float getEnergyLevel()
	{
		return energy.getEnergyLevel();
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
	}
}
