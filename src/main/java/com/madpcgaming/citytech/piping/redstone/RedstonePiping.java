package com.madpcgaming.citytech.piping.redstone;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.util.DyeColor;

public class RedstonePiping extends AbstractPiping implements IRedstonePiping
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
	public int isProvidingStrongPower(ForgeDirection toDirection)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int isProvidingWeakPower(ForgeDirection toDirection)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<Signal> getNetworkInputs()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Signal> getNetworkInputs(ForgeDirection side)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Signal> getNetworkOutputs(ForgeDirection side)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DyeColor getSignalColor(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNetwork()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getOutputValues(World world, int x, int y, int z,
			ForgeDirection side)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOutputValue(World world, int x, int y, int z,
			ForgeDirection side, int subnet)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
}
