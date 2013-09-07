package com.madpcgaming.mt.handlers;

import cpw.mods.fml.common.Loader;

public class UniversalElectricityHandler
{
	public static final UniversalElectricityHandler instance = new UniversalElectricityHandler();
	
	private static boolean isUELoaded = false;
	private static float CONVERT_TO = 1.0f;

	public static boolean isUELoaded()
	{
		return isUELoaded;
	}
	
	public void checkLoaded()
	{
		isUELoaded = Loader.isModLoaded("UE");
	}
	
	public float convertToUE(float f)
	{
		return f * CONVERT_TO;
	}
}
