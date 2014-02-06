package com.madpcgaming.citytech.lib;

import net.minecraft.util.StatCollector;

public class Lang
{
	public static String localize(String s) {
	    s = "cityech." + s;
	    return StatCollector.translateToLocal(s);
	  }

	  public static String[] localizeList(String string) {
	    String s = localize(string);
	    return s.split("\\|");
	  }

}
