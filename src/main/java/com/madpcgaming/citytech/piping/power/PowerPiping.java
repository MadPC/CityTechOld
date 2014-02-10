package com.madpcgaming.citytech.piping.power;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.power.BasicTeslaBat;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.power.ITeslaBat;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.util.DyeColor;
import com.madpcgaming.citytech.util.IconUtil;
import com.madpcgaming.citytech.vecmath.Vector3d;

public class PowerPiping extends AbstractPiping implements IPowerPiping
{
	static final Map<String, IIcon> ICONS = new HashMap<String, IIcon>();
	
	static final ITeslaBat[] TESLA_BAT = new BasicTeslaBat[]
	{
		new BasicTeslaBat(500, 1500, 128),
		new BasicTeslaBat(512, 3000, 512),
		new BasicTeslaBat(2048, 5000, 2048)
	};
	
	static final String[] POSTFIX = new String[] {"", "Enchanced", "Advanced"};
	
	static ItemStack createItemStackForSubtype(int subtype)
	{
		ItemStack result = new ItemStack(ModBlocks.itemPowerPiping, 1, subtype);
		return result;
	}
	
	public static void initIcons()
	{
		IconUtil.addIconProvider(new IconUtil.IIconProvider()
		{
			
			@Override
			public void registerIcons(IIconRegister register)
			{
				for(String pf : POSTFIX)
				{
					ICONS.put(ICON_KEY + pf, register.registerIcon(ICON_KEY + pf));
					ICONS.put(ICON_KEY_INPUT + pf, register.registerIcon(ICON_KEY_INPUT + pf));
			        ICONS.put(ICON_KEY_OUTPUT + pf, register.registerIcon(ICON_KEY_OUTPUT + pf));
			        ICONS.put(ICON_CORE_KEY + pf, register.registerIcon(ICON_CORE_KEY + pf));
				}
				ICONS.put(ICON_TRANSMISSION_KEY, register.registerIcon(ICON_TRANSMISSION_KEY));
			}
			
			@Override
			public int getTextureType()
			{
				// TODO Auto-generated method stub
				return 0;
			}
		});
	}
	
	public static final float WIDTH = 0.075f;
	public static final float HEIGHT = 0.075f;
	
	public static final Vector3d MIN = new Vector3d(0.5f - WIDTH, 0.5 - HEIGHT, 0.5 - WIDTH);
	public static final Vector3d MAX = new Vector3d(MIN.x + WIDTH, MIN.y + HEIGHT, MIN.z + WIDTH);
	
	public static final BoundingBox BOUNDS = new BoundingBox(MIN, MAX);
	
	protected PowerPipingNetwork network;

	@Override
	public PowerHandler getPowerHandler()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyPerdition()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doWork(PowerHandler workProvider)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public World getWorld()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return 0;
	}

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
	public IPowerInterface getExternalPowerReceptor(ForgeDirection direction)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITeslaBat getTeslaBat()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMaxEnergyExtracted(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxEnergyReceived(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRedstoneMode(RedstoneControlMode mode, ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public RedstoneControlMode getRedstoneMode(ForgeDirection dir)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSignalColor(ForgeDirection dir, DyeColor col)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public DyeColor getSignalColor(ForgeDirection dir)
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
	public void onTick()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getEnergyStored()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setEnergyStored(float give)
	{
		// TODO Auto-generated method stub
		
	}
	
}
