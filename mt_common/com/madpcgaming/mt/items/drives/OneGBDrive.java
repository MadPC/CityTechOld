package com.madpcgaming.mt.items.drives;

import com.madpcgaming.mt.items.ItemMT;
import com.madpcgaming.mt.lib.Strings;

public class OneGBDrive extends ItemMT
{
	
	public OneGBDrive(int id)
	{
		
		super(id);
		this.setUnlocalizedName(Strings.ALUMINUM_BLOCK_NAME);
		maxStackSize = 1;
		afterInit();
	}
}
