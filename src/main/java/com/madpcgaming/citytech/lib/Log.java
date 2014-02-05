package com.madpcgaming.citytech.lib;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public final class Log {

	  public static final String CHANNEL = "EnderIO";

	  public static void warn(String msg) {
	    FMLLog.log(CHANNEL, Level.WARN, msg);
	  }

	  public static void error(String msg) {
	    FMLLog.log(CHANNEL, Level.FATAL, msg);
	  }

	  public static void info(String msg) {
	    FMLLog.log(CHANNEL, Level.INFO, msg);
	  }

	  public static void debug(String msg) {
	    FMLLog.log(CHANNEL, Level.TRACE, msg);
	  }

	  private Log() {
	  }

	}