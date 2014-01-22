package com.madpcgaming.madtech.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.madtech.energy.EnergyHandler;
import com.madpcgaming.madtech.energy.interfaces.IEnergyConductor;
import com.madpcgaming.madtech.helpers.OrientationHelper;

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
		energy = new EnergyHandler(this.field_145851_c, this.field_145848_d, this.field_145849_e);
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
			TileEntity ent = this.field_145850_b.func_147438_o(this.field_145851_c + ofX, this.field_145848_d + ofY, this.field_145849_e + ofZ);
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
			int workX = this.field_145851_c + d.offsetX;
			int workY = this.field_145848_d + d.offsetY;
			int workZ = this.field_145849_e + d.offsetZ;
			TileEntity t = this.field_145850_b.func_147438_o(workX, workY, workZ);
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
	public void func_145845_h()
	{
		this.update();
		if (!this.field_145850_b.isRemote)
			energy.update(this.field_145850_b);
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
		super.func_145839_a (par1);
		this.energy.updatePosition(this.field_145851_c, this.field_145848_d, this.field_145849_e);
		this.energy.readFromNBT(par1);
	}
	
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1)
    {
    	super.func_145841_b(par1);
    	this.energy.writeToNBT(par1);
    }
}
