package com.madpcgaming.citytech.items.piping;

import com.madpcgaming.citytech.lib.Lang;

public class ItemPipingSubtype {

	  public final String unlocalisedName;

	  public final String uiName;

	  public final String iconKey;

	  public ItemPipingSubtype(String unlocalisedName, String iconKey) {
	    this.unlocalisedName = unlocalisedName;
	    this.uiName = Lang.localize(unlocalisedName);
	    this.iconKey = iconKey;
	  }

	}
