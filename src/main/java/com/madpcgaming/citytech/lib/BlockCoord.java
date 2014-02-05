package com.madpcgaming.citytech.lib;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public final class BlockCoord
{
	public final int x;
	public final int y;
	public final int z;
	
	public BlockCoord(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockCoord(TileEntity tile)
	{
		//TODO:   xCoord				yCoord				zCoord
		this(tile.field_145851_c, tile.field_145848_d, tile.field_145849_e);
	}
	
	public BlockCoord getLocation(ForgeDirection dir)
	{
		return new BlockCoord(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
	}
	
	public int distanceSquared(BlockCoord squared)
	{
		int dx, dy, dz;
		dx = x - squared.x;
		dy = y - squared.y;
		dz = z - squared.z;
		return(dx * dx + dy * dy + dz * dz);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		BlockCoord other = (BlockCoord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "BlockCoord [X =" + x + ", Y=" + y + ", Z=" + z +"]";
	}
	
	public boolean equals(int xCoord, int yCoord, int zCoord)
	{
		return x == xCoord && y == yCoord && z == zCoord;
	}
}
