package com.madpcgaming.citytech.piping;

import com.madpcgaming.citytech.lib.Lang;

public enum ConnectionMode
{
	IN_OUT("gui.piping.ioMode.inOut"),
	INPUT("gui.piping.ioMode.input"),
	OUTPUT("gui.piping.ioMode.output"),
	DISABLED("gui.piping.ioMode.disabled"),
	NOT_SET("gui.piping.ioMode.notSet");
	
	private final String unlocalizedName;
	
	ConnectionMode(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
	}
	
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public static ConnectionMode getNext(ConnectionMode mode)
	{
		int ord = mode.ordinal() + 1;
		if(ord >= ConnectionMode.values().length)
		{
			ord = 0;
		}
		return ConnectionMode.values()[ord];
	}
	
	public static ConnectionMode getPrevious(ConnectionMode mode)
	{
		int ord = mode.ordinal() - 1;
		if(ord < 0)
		{
			ord = ConnectionMode.values().length - 1;
		}
		return ConnectionMode.values()[ord];
	}
	
	public boolean acceptsInput()
	{
		return this == IN_OUT || this == INPUT;
	}
	
	public boolean acceptsOutput()
	{
		return this == IN_OUT || this == OUTPUT;
	}
	
	public String getLocalizedName()
	{
		return  Lang.localize(unlocalizedName);
	}
}
