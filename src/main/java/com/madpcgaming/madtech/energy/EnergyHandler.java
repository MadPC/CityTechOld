package com.madpcgaming.madtech.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.madtech.helpers.LogHelper;

public class EnergyHandler
{
	private static final float	MAX_NORM_ENERGY	= 100.0f;
	
	private float				internal_request 	=	0;
	private float[]				Requested		= 	new float[6];
	private float				Energy;
	private int					x, y, z;
	
	public void updatePosition(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public EnergyHandler(int x, int y, int z)
	{
		// LogHelper.info("&&X: %d Y: %d Z: %d", x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		Energy = 0;
	}
	
	public float placeEnegry(ForgeDirection d, float amount)
	{
		// This should only happen after checking and only be called from the
		// Tile on itself!
		if (d == ForgeDirection.UNKNOWN && amount < 0)
		{
			float leftover = this.Energy + amount;
			if (leftover >= 0)
			{
				this.Energy += amount;
			} else
			{
				LogHelper.severe("&& Problem at %d %d %d %f", x, y, z, Energy);
				throw new RuntimeException("A TileEntity tried to remove more Energy than possible with a Diretion of Unknown!");
			}
		} else
		{
			if (this.internal_request != 0)
			{
				this.internal_request -= amount;
				if (this.internal_request < 0)
					this.internal_request = 0;
			}
			
			float temp = this.Energy + amount;
			if (temp > MAX_NORM_ENERGY)
			{
				float space = MAX_NORM_ENERGY - this.Energy;
				this.Energy = MAX_NORM_ENERGY;
				return amount - space;
			} else
			{
				this.Energy += amount;
				return 0.0f;
			}
		}
		return 0.0f;
	}
	
	/**
	 * use this to tell another object YOU need energy
	 * @param d
	 * @param amount
	 * @return if this Handler has enough energy
	 */
	public boolean requestEnergy(ForgeDirection d, float amount)
	{
		if (this.Energy >= amount)
		{
			this.Requested[d.ordinal()] = 0;
			return true;
		}
		this.Requested[d.ordinal()] = amount;
		return false;
	}
	
	/**
	 * Use this to tell an EnergyHandler you need energy.
	 * @param amount
	 */
	public boolean requestEnergy(float amount)
	{
		if (amount >= 0)
			internal_request = amount;
		return false;
	}
	
	public float getEnergyLevel()
	{
		return this.Energy;
	}
	
	/**
	 * Gives the EnegryHandler the chance to fullfill requests
	 */
	public void update(World world)
	{
		for (int i = 0; i < 6; i++)
		{
			float request = this.Requested[i];
			if (request > 0)
			{
				if (this.Energy >= request)
				{
					TileEntity te = world.func_147438_o(x, y, z);
					if (te != null && te instanceof IEnergyConductor)
						((IEnergyConductor) te).requestFrom(ForgeDirection.getOrientation(i), request);
					else
						LogHelper.severe("&&Where did %s go? It requested %f Flux!", ForgeDirection.getOrientation(i), request);
				} else
				{
					for (int j = 0; j < 6; j++)
					{
						ForgeDirection d = ForgeDirection.getOrientation(j);
						TileEntity t = world.func_147438_o(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
						if (i != j && t != null && t instanceof IEnergyConductor)
						{
							((IEnergyConductor) t).requestFrom(ForgeDirection.getOrientation(j).getOpposite(), request);
						}
					}
				}
			}
		}
		
		if (internal_request != 0)
		{
			//LogHelper.info("&&Requesting %f Flux from nearby Conductors!", internal_request);
			for (int j = 0; j < 6; j++)
			{
				ForgeDirection d = ForgeDirection.getOrientation(j);
				TileEntity t = world.func_147438_o(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
				if (t != null && t instanceof IEnergyConductor)
				{
					//LogHelper.info("&&Now Requesting from %s", t);
					((IEnergyConductor) t).requestFrom(ForgeDirection.getOrientation(j).getOpposite(), internal_request);
				}
			}
		}
	}
	
	public float getOverallRequest()
	{
		float ret = 0.0f;
		for (float f : this.Requested)
		{
			ret += f;
		}
		return ret;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setFloat("Energy", this.Energy);
		this.x = par1NBTTagCompound.getInteger("x");
        this.y = par1NBTTagCompound.getInteger("y");
        this.z = par1NBTTagCompound.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound par1)
	{
		this.Energy = par1.getFloat("Energy");
	}
}
