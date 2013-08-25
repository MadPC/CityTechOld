package com.madpcgaming.mt.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.madpcgaming.mt.lib.Reference;

import cpw.mods.fml.common.FMLLog;

public class LogHelper
{
	private static final Logger	MTLog	= Logger.getLogger(Reference.MOD_ID);
	
	static
	{
		MTLog.setParent(FMLLog.getLogger());
	}
	
	public static void log(Level l, String s)
	{
		log(l, s, (Object) null);
	}
	
	public static void log(Level l, String s, Object... args)
	{
		if (!s.startsWith("&&"))
			MTLog.log(l, s);
		else
		{
			String msg = String.format(s.substring(2), args);
			MTLog.log(l, msg);
		}
	}
	
	public static void info(String s)
	{
		log(Level.INFO, s, (Object) null);
	}
	
	public static void info(String s, Object... args)
	{
		log(Level.INFO, s, args);
	}
	
	public static void severe(String string, Object... args)
	{
		log(Level.SEVERE, string, args);
	}
	
	public static void severe(String string)
	{
		log(Level.SEVERE, string, (Object) null);
	}
}
