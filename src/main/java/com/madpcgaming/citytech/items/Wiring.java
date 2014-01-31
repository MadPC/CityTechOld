package com.madpcgaming.citytech.items;

import com.madpcgaming.citytech.lib.Strings;

public class Wiring extends ItemMT
{
	
	public Wiring(int id)
	{
		super();
		this.setUnlocalizedName(Strings.WIRING_ITEM_NAME);
		maxStackSize = 64;
	}
}
