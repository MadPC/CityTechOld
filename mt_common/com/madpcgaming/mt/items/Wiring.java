package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class Wiring extends ItemMT
{
	
	public Wiring(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.WIRING_ITEM_NAME);
		maxStackSize = 64;
	}
}
