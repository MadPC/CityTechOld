package com.madpcgaming.citytech.items.piping.item;

import javax.swing.Icon;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.items.piping.IItemConduit;
import com.madpcgaming.citytech.items.piping.IPiping;
import com.madpcgaming.citytech.lib.DyeColor;

public interface IItemPiping extends IPiping, IItemConduit
{
	Icon getTextureForInputMode();

	Icon getTextureForOutputMode();

	Icon getTextureForInOutMode(boolean inputComponent);

	Icon getTextureForInOutBackground();

	Icon getEnderIcon();

	IInventory getExternalInventory(ForgeDirection direction);

	int getMaximumExtracted();

	float getTickTimePerItem();

	void itemsExtracted(int numInserted, int slot);

	void setInputFilter(ForgeDirection dir, ItemFilter filter);

	void setOutputFilter(ForgeDirection dir, ItemFilter filter);

	ItemFilter getInputFilter(ForgeDirection dir);

	ItemFilter getOutputFilter(ForgeDirection dir);

	int getMetaData();

	void setExtractionSignalColor(ForgeDirection dir, DyeColor col);

	DyeColor getExtractionSignalColor(ForgeDirection dir);

	boolean isExtractionRedstoneConditionMet(ForgeDirection dir);

	boolean isSelfFeedEnabled(ForgeDirection dir);

	void setSelfFeedEnabled(ForgeDirection dir, boolean enabled);

	DyeColor getInputColor(ForgeDirection dir);

	DyeColor getOutputColor(ForgeDirection dir);

	void setInputColor(ForgeDirection dir, DyeColor col);

	void setOutputColor(ForgeDirection dir, DyeColor col);

}