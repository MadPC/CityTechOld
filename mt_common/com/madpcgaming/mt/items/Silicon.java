package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class Silicon extends ItemMT
{
	
	public Silicon(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.SILICON_ITEM_NAME);
		maxStackSize = 64;
		afterInit();
	}
}
