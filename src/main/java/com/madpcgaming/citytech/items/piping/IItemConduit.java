package com.madpcgaming.citytech.items.piping;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IItemConduit
{
	public ItemStack insertItem(ForgeDirection from, ItemStack item);
}
