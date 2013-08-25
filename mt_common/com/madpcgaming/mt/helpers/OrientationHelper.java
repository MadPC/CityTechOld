package com.madpcgaming.mt.helpers;

import com.madpcgaming.mt.lib.Reference;

import net.minecraftforge.common.ForgeDirection;

public class OrientationHelper
{
	public static byte getMaskFromDirection(ForgeDirection d)
	{
		switch (d)
		{
			case UP:
				return Reference.TOP;
			case DOWN:
				return Reference.BOTTOM;
			case NORTH:
				return Reference.FRONT;
			case SOUTH:
				return Reference.BACK;
			case EAST:
				return Reference.RIGHT;
			case WEST:
				return Reference.LEFT;
			default:
				return 0;
		}
	}
	
	public static byte getMaskFromOrdinal(int i)
	{
		return getMaskFromDirection(ForgeDirection.getOrientation(i));
	}
}
