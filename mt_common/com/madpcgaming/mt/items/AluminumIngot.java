package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class AluminumIngot extends ItemMT
{
	
	public AluminumIngot(int id)
	{
		
		super(id);
		this.setUnlocalizedName(Strings.ALUMINUM_INGOT_NAME);
		maxStackSize = 64;
		afterInit();
	}
}
