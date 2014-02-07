package com.madpcgaming.citytech.util;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.citytech.lib.Lang;

public enum DyeColor {

	  BLACK,
	  RED,
	  GREEN,
	  BROWN,
	  BLUE,
	  PURPLE,
	  CYAN,
	  SILVER,
	  GRAY,
	  PINK,
	  LIME,
	  YELLOW,
	  LIGHT_BLUE,
	  MAGENTA,
	  ORANGE,
	  WHITE;

	  public static final String[] DYE_ORE_NAMES = {
	      "dyeBlack",
	      "dyeRed",
	      "dyeGreen",
	      "dyeBrown",
	      "dyeBlue",
	      "dyePurple",
	      "dyeCyan",
	      "dyeLightGray",
	      "dyeGray",
	      "dyePink",
	      "dyeLime",
	      "dyeYellow",
	      "dyeLightBlue",
	      "dyeMagenta",
	      "dyeOrange",
	      "dyeWhite"
	  };

	  public static final String[] DYE_ORE_LOCAL_NAMES = {
	      Lang.localize("color.black"),
	      Lang.localize("color.red"),
	      Lang.localize("color.green"),
	      Lang.localize("color.brown"),
	      Lang.localize("color.blue"),
	      Lang.localize("color.purple"),
	      Lang.localize("color.cyan"),
	      Lang.localize("color.lightGray"),
	      Lang.localize("color.gray"),
	      Lang.localize("color.pink"),
	      Lang.localize("color.lime"),
	      Lang.localize("color.yellow"),
	      Lang.localize("color.lightBlue"),
	      Lang.localize("color.magenta"),
	      Lang.localize("color.orange"),
	      Lang.localize("color.white")
	  };

	  public static DyeColor getNext(DyeColor col) {
	    int ord = col.ordinal() + 1;
	    if(ord >= DyeColor.values().length) {
	      ord = 0;
	    }
	    return DyeColor.values()[ord];
	  }

	  public static DyeColor getColorFromDye(ItemStack dye) {
	    if(dye == null) {
	      return null;
	    }
	    int oreId = OreDictionary.getOreID(dye);
	    if(oreId < 0) {
	      return null;
	    }
	    for (int i = 0; i < DYE_ORE_NAMES.length; i++) {
	      String dyeName = DYE_ORE_NAMES[i];
	      if(OreDictionary.getOreID(dyeName) == oreId) {
	        return DyeColor.values()[i];
	      }
	    }
	    return null;
	  }

	  public static DyeColor fromIndex(int index) {
	    return DyeColor.values()[index];
	  }

	  private DyeColor() {
	  }

	  public int getColor() {
	    return ItemDye.field_150922_c[ordinal()];
	  }

	  public String getName() {
	    return ItemDye.field_150921_b[ordinal()];
	  }

	  public String getLocalisedName() {
	    return DYE_ORE_LOCAL_NAMES[ordinal()];
	  }

	  @Override
	  public String toString() {
	    return getName();
	  }

	}