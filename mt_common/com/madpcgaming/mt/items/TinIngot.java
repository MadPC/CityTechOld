package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class TinIngot extends ItemMT
{
	
	public TinIngot(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.TIN_INGOT_NAME);
		maxStackSize = 64;
		afterInit();
	}
	
}
