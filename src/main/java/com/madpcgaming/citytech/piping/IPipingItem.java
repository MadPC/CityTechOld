package com.madpcgaming.citytech.piping;

import net.minecraft.item.ItemStack;

public interface IPipingItem
{
	Class<? extends IPiping> getBasePipingType();
	
	IPiping createPiping(ItemStack item);
}
