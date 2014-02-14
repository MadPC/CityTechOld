package com.madpcgaming.citytech.piping.redstone;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.util.BlockCoord;
import com.madpcgaming.citytech.util.DyeColor;
import com.madpcgaming.citytech.util.IconUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RedstonePiping extends AbstractPiping implements IRedstonePiping
{
	static final Map<String, IIcon> ICONS = new HashMap<String, IIcon>();
	
	@SideOnly(Side.CLIENT)
	public static void initIcons()
	{
		IconUtil.addIconProvider(new IconUtil.IIconProvider()
		{
			
			@Override
			public void registerIcons(IIconRegister register)
			{
				// TODO Texturing
				
			}
			
			@Override
			public int getTextureType()
			{
				return 0;
			}
		});
	}
	
	protected RedstonePipingNetwork network;
	protected final Set<Signal> externalSignals = new HashSet<Signal>();
	protected boolean neighborDirty = true;
	
	public RedstonePiping()
	{
		
	}
	
	@Override
	public ItemStack createItem()
	{
		return new ItemStack(ModBlocks.BlockPipingBundle, 1, 0);
	}
	
	@Override
	public Class<? extends IPiping> getBasePipingType()
	{
		return IRedstonePiping.class;
	}
	
	@Override
	public AbstractPipingNetwork<IRedstonePiping, IRedstonePiping> getNetwork()
	{
		return network;
	}
	
	@Override
	public boolean setNetwork(AbstractPipingNetwork<?,?> network)
	{
		this.network = (RedstonePipingNetwork) network;
		return true;
	}
	
	@Override
	public boolean canConnectToExternal(ForgeDirection direction, boolean ignoreDisabled)
	{
		return false;
	}
	
	@Override
	public void updateNetwork()
	{
		World world = getBundle().getEntity().getWorldObj();
		if(world != null)
		{
			updateNetwork(world);
		}
	}
	
	protected boolean acceptSignalsForDir(ForgeDirection dir)
	{
		BlockCoord loc = getLocation().getLocation(dir);
		return PipingUtil.getPiping(getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, IRedstonePiping.class) == null;
	}
	
	@Override
	public Set<Signal> getNetworkInputs()
	{
		return getNetworkInputs(null);
	}
	
	@Override
	public Set<Signal> getNetworkInputs(ForgeDirection side)
	{
		if(network != null)
		{
			network.setNetworkEnabled(false);
		}
		
		Set<Signal> res = new HashSet<Signal>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if((side == null || dir == side) && acceptSignalsForDir(dir))
			{
				int input = getExternalPowerLevel(dir);
				if(input > 1)
				{
					BlockCoord loc = getLocation().getLocation(dir);
					Signal signal = new Signal(loc.x, loc.y, loc.z, dir, input - 1, getSignalColor(dir));
					res.add(signal);
				}
			}
		}
		
		if(network != null)
		{
			network.setNetworkEnabled(true);
		}
		return res;
	}
	
	@Override
	public DyeColor getSignalColor(ForgeDirection dir)
	{
		return DyeColor.RED;
	}
	
	@Override
	public Set<Signal> getNetworkOutputs(ForgeDirection side)
	{
		if(network == null)
		{
			return Collections.emptySet();
		}
		return network.getSignals();
	}
	
	@Override
	public boolean onNeighborBlockChange(Block blockId)
	{
		World world = getBundle().getEntity().getWorldObj();
		if(world.isRemote)
		{
			return false;
		}
		
		boolean res = super.onNeighborBlockChange(blockId);
		if(network == null || network.updatingNetwork)
		{
			return false;
		}
		neighborDirty = res;
		return res;
	}
	
	@Override
	public void updateEntity(World world)
	{
		super.updateEntity(world);
		if(!world.isRemote && neighborDirty)
		{
			network.destroyNetwork();
			updateNetwork(world);
			neighborDirty = false;
		}
	}
	
	protected int getExternalPowerLevel(ForgeDirection dir)
	{
		World world = getBundle().getEntity().getWorldObj();
		BlockCoord loc = getLocation();
		loc = loc.getLocation(dir);
		return world.getIndirectPowerLevelTo(loc.x, loc.y, loc.z, dir.ordinal());
	}
	
	@Override
	public int isProvidingStrongPower(ForgeDirection toDirection)
	{
		return 0;
	}
	
	@Override
	public int isProvidingWeakPower(ForgeDirection toDirection)
	{
		if(network == null || !network.isNetworkEnabled())
		{
			return 0;
		}
		int result = 0;
		for(Signal signal : getNetworkOutputs(toDirection.getOpposite()))
		{
			result = Math.max(result, signal.strength);
		}
		return result;
	}
	
	@Override
	public IIcon getTextureForState(CollidableComponent component)
	{
		if(component.dir == ForgeDirection.UNKNOWN)
		{
			return isActive() ? ICONS.get(KEY_CORE_ON_ICON) : ICONS.get(KEY_CORE_OFF_ICON);
		}
		return isActive() ? ICONS.get(KEY_TRANSMISSION_ICON) : ICONS.get(KEY_PIPING_ICON);
	}
	
	@Override
	public IIcon getTransmitionTextureForState(CollidableComponent component)
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		return "RedstonePiping [network=" + network + " connections=" + pipingConnections + " active=" + active + "]";
	}
	
	@Override
	public int[] getOutputValues(World world, int x, int y, int z, ForgeDirection side)
	{
		int[] result = new int[16];
		Set<Signal> outs = getNetworkOutputs(side);
		if(outs != null)
		{
			for(Signal s : outs)
			{
				result[s.color.ordinal()] = s.strength;
			}
		}
		return result;
	}
	
	@Override
	public int getOutputValue(World world, int x, int y, int z, ForgeDirection side, int subnet)
	{
		Set<Signal> outs = getNetworkOutputs(side);
		if(outs != null)
		{
			for(Signal s : outs)
			{
				if(subnet == s.color.ordinal())
				{
					return s.strength;
				}
			}
		}
		return 0;
	}
}
