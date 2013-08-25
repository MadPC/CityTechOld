package com.madpcgaming.mt.items;

import com.madpcgaming.mt.lib.Strings;

public class PalladiumIngot extends ItemMT
{
	
	public PalladiumIngot(int par1)
	{
		super(par1);
		this.setUnlocalizedName(Strings.PALLADIUM_INGOT_NAME);
		maxStackSize = 64;
		afterInit();
	}
	
}
