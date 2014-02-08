package com.madpcgaming.citytech.piping.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.util.DyeColor;

public class ItemPiping extends AbstractPiping implements IItemPiping
{
	@Override
	public Class<? extends IPiping> getBasePipingType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack createItem()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractPipingNetwork<?, ?> getNetwork()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setNetwork(AbstractPipingNetwork<?, ?> network)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IIcon getTextureForState(CollidableComponent component)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTransmitionTextureForState(CollidableComponent component)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack insertItem(ForgeDirection from, ItemStack item)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTextureForInputMode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTextureForOutputMode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTextureForInOutMode(boolean inputComponent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getTextureForInOutBackground()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIcon getCityTechIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInventory getExternalInventory(ForgeDirection direction)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaximumExtracted()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getTickTimePerItem()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void itemsExtacted(int numInserted, int slot)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputFilter(ForgeDirection dir, ItemFilter filter)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutputFilter(ForgeDirection dir, ItemFilter filter)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ItemFilter getInputFilter(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemFilter getOutputFilter(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMetaData()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setExtractionRedstoneMode(RedstoneControlMode mode,
			ForgeDirection dir)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public RedstoneControlMode getExtractionRedstoneMode(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExtractionSignalColor(ForgeDirection dir, DyeColor col)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public DyeColor getExtractionSignalColor(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExtractionRedstoneConditionMet(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelfFeedEnabled(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSelfFeedEnabled(ForgeDirection dir, boolean enabled)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public DyeColor getInputColor(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DyeColor getOutputColor(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInputColor(ForgeDirection dir, DyeColor col)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutputColor(ForgeDirection dir, DyeColor col)
	{
		// TODO Auto-generated method stub

	}

}
