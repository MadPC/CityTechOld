package com.madpcgaming.madtech.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.madtech.energy.EnergyHandler;
import com.madpcgaming.madtech.energy.interfaces.IEnergyConductor;

public class DrainTE extends TileEntity implements IEnergyConductor
{
	
	private EnergyHandler	energy;
	
	public DrainTE()
	{
		super();
		energy = new EnergyHandler(this.field_145851_c, this.field_145848_d, this.field_145849_e);
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
	
	private byte	tickCount	= 0;
	
	@Override
	public void func_145845_h()
	{
		if (!this.field_145850_b.isRemote)
		{
			energy.update(this.field_145850_b);
			tickCount++;
			if (tickCount == 6)
			{
				tickCount = 0;
				for (int i = 0; i < 6; i++)
				{
					ForgeDirection d = ForgeDirection.getOrientation(i);
					int X = this.field_145851_c + d.offsetX;
					int Y = this.field_145848_d + d.offsetY;
					int Z = this.field_145849_e + d.offsetZ;
					TileEntity t = this.field_145850_b.func_147438_o(X, Y, Z);
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
		super.func_145839_a(par1NBTTagCompound);
	}
}
