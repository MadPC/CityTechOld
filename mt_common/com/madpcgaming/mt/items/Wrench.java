package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class Wrench extends ItemMT
{
	
	public Wrench(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.WRENCH_ITEM_NAME);
		maxStackSize = 1;
	}
	
}
