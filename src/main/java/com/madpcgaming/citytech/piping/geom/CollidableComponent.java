package com.madpcgaming.citytech.piping.geom;

import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.render.BoundingBox;

public class CollidableComponent
{
	public final Class<? extends IPiping> pipingType;
	public final BoundingBox bound;
	public final ForgeDirection dir;
	public final Object data;
	
	public CollidableComponent(Class<? extends IPiping> pipingType, BoundingBox bound, ForgeDirection id, Object data)
	{
		this.pipingType = pipingType;
		this.bound = bound;
		this.dir = id;
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		return "CollidableComponent [pipingType=" + pipingType + ", bound=" + bound + ", id=" + dir + "]";
	}
}
