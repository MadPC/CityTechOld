package com.madpcgaming.buildcraft.api.transport;

import net.minecraft.tileentity.TileEntity;

public interface IPipe
{
	enum DrawingState
	{
		DrawingPipe, DrawingRedWire, DrawingBlueWire, DrawingGreenWire, DrawingYellowWire, DrawingGate
	}
	
	enum WireColor
	{
		Red, Blue, Green, Yellow;
		
		public WireColor reverse()
		{
			switch(this)
			{
				case Red:
					return Yellow;
				case Blue:
					return Green;
				case Green:
					return Blue;
				case Yellow:
					return Red;
				default:
					return Red;
						
			}
		}
	}
	
	public boolean isWired(WireColor color);
	
	public boolean hasGate();
	
	public TileEntity getContainer();
	
	public boolean isWireConnectedTo(TileEntity tile, WireColor color);
}
