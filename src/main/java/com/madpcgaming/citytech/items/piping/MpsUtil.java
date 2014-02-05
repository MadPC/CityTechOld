package com.madpcgaming.citytech.items.piping;

import java.lang.reflect.Method;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class MpsUtil {

	  public static final MpsUtil instance = new MpsUtil();

	  private static String WRENCH_MODUL_NAME = "Prototype OmniWrench";

	  private boolean isMpsInstalled;

	  private Class<?> powerFistClass;

	  private Method itemActiveMethod;

	  private MpsUtil() {
	    isMpsInstalled = false;
	    try {
	      powerFistClass = Class.forName("net.machinemuse.powersuits.item.ItemPowerFist");

	      Class<?> moduleManagerClass = Class.forName("net.machinemuse.api.ModuleManager");
	      itemActiveMethod = moduleManagerClass.getMethod("itemHasActiveModule", ItemStack.class, String.class);

	      isMpsInstalled = true;
	    } catch (Exception e) {

	    }

	  }

	  public boolean isOmniToolActive(ItemStack equipped) {
	    if(!isMpsInstalled || equipped == null || itemActiveMethod == null) {
	      return false;
	    }
	    try {
	      Object res = itemActiveMethod.invoke(null, equipped, WRENCH_MODUL_NAME);
	      if(res instanceof Boolean) {
	        return ((Boolean) res).booleanValue();
	      }
	    } catch (Exception e) {
	    }
	    return false;
	  }

	}