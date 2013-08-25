package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class PlatinumIngot extends ItemMT
{
	
	public PlatinumIngot(int id)
	{
		super(id);
		this.setUnlocalizedName(Strings.PLATINUM_INGOT_NAME);
		maxStackSize = 64;
		afterInit();
	}
	
}
