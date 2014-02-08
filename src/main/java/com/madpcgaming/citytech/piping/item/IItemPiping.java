package com.madpcgaming.citytech.piping.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.transport.IItemConduit;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.util.DyeColor;

public interface IItemPiping extends IPiping, IItemConduit
{
	IIcon getTextureForInputMode();
	
	IIcon getTextureForOutputMode();
	
	IIcon getTextureForInOutMode(boolean inputComponent);
	
	IIcon getTextureForInOutBackground();
	
	IIcon getCityTechIcon();
	
	IInventory getExternalInventory(ForgeDirection direction);
	
	int getMaximumExtracted();
	
	float getTickTimePerItem();
	
	void itemsExtacted(int numInserted, int slot);
	
	void setInputFilter(ForgeDirection dir, ItemFilter filter);
	
	void setOutputFilter(ForgeDirection dir, ItemFilter filter);
	
	ItemFilter getInputFilter(ForgeDirection dir);
	
	ItemFilter getOutputFilter(ForgeDirection dir);
	
	int getMetaData();
	
	void setExtractionRedstoneMode(RedstoneControlMode mode, ForgeDirection dir);
	
	RedstoneControlMode getExtractionRedstoneMode(ForgeDirection dir);
	
	void setExtractionSignalColor(ForgeDirection dir, DyeColor col);
	
	DyeColor getExtractionSignalColor(ForgeDirection dir);
	
	boolean isExtractionRedstoneConditionMet(ForgeDirection dir);
	
	boolean isSelfFeedEnabled(ForgeDirection dir);
	
	void setSelfFeedEnabled(ForgeDirection dir, boolean enabled);
	
	DyeColor getInputColor(ForgeDirection dir);
	
	DyeColor getOutputColor(ForgeDirection dir);
	
	void setInputColor(ForgeDirection dir , DyeColor col);
	
	void setOutputColor(ForgeDirection dir, DyeColor col);
}
