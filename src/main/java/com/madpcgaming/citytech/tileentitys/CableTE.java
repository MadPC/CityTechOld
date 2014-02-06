package com.madpcgaming.citytech.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.energy.EnergyHandler;
import com.madpcgaming.citytech.energy.IEnergyConductor;
import com.madpcgaming.citytech.helpers.OrientationHelper;

public class CableTE extends TileEntity implements IEnergyConductor
{
	
	private EnergyHandler	energy;
	private byte			sideConnections;
	
	public byte getSideConnections()
	{
		return sideConnections;
	}
	
	public CableTE()
	{
		super();
		energy = new EnergyHandler(this.xCoord, this.yCoord, this.zCoord);
		sideConnections = 0;
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
			// ForgeDirection opposite = d.getOpposite();
			ForgeDirection opposite = d;
			
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
	
	public void update()
	{
		for (int i = 0; i < 6; i++)
		{
			ForgeDirection d = ForgeDirection.getOrientation(i);
			int workX = this.xCoord + d.offsetX;
			int workY = this.yCoord + d.offsetY;
			int workZ = this.zCoord + d.offsetZ;
			TileEntity t = this.worldObj.getTileEntity(workX, workY, workZ);
			byte b = OrientationHelper.getMaskFromDirection(d);
			if (t != null && t instanceof IEnergyConductor)
			{
				sideConnections |= b;
			} else
			{
				sideConnections &= ~b;
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		this.update();
		if (!this.worldObj.isRemote)
			energy.update(this.worldObj);
	}
	
	@Override
	public float getEnergyLevel()
	{
		return energy.getEnergyLevel();
	}
	
	public float getRequested()
	{
		return this.energy.getOverallRequest();
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT (par1);
		this.energy.updatePosition(this.xCoord, this.yCoord, this.zCoord);
		this.energy.readFromNBT(par1);
	}
	
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1)
    {
    	super.writeToNBT(par1);
    	this.energy.writeToNBT(par1);
    }
}
