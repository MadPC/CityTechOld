package com.madpcgaming.cofh.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IItemConduit
{
	public ItemStack insertItem(ForgeDirection from, ItemStack item);
}
