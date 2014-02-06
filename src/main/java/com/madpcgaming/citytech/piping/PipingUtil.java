package com.madpcgaming.citytech.piping;

import java.util.Collection;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.IPipingBundle.FacadeRenderState;
import com.madpcgaming.citytech.piping.item.IItemPiping;
import com.madpcgaming.citytech.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.util.BlockCoord;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipingUtil
{
	public static final Random RANDOM = new Random();
	
	public static AbstractPipingNetwork<?,?> createNetworkForType(Class<? extends IPiping> type)
	{
		if(ILiquidPiping.class.isAssignableFrom(type))
		{
			return new LiquidPipingNetwork();
		}
		else if(IItemPiping.class.isAssignableFrom(type))
		{
			return new ItemPipingNetwork();
		}
		FMLCommonHandler.instance().raiseException(new Exception("Could not determine network type for class " + type), "PipingUtil.createNetworkForType", false);
		return null;
	}
	
	public static void ensureValidNetwork(IPiping piping)
	{
		TileEntity te = piping.getBundle().getEntity();
		World world = te.getWorldObj();
		Collection<? extends IPiping> connections = PipingUtil.getConnectedPiping(world, te.xCoord, te.yCoord, te.zCoord, piping.getBasePipingType());
		
		if(reuseNetwork(piping, connections, world))
		{
			return;
		}
		
		AbstractPipingNetwork res = createNetworkForType(piping.getClass());
		res.init(piping.getBundle(), connections, world);
		return;
	}
	
	private static boolean reuseNetwork(IPiping pipe, Collection<? extends IPiping> connections, World world)
	{
		AbstractPipingNetwork network = null;
		for(IPiping pipes : connections)
		{
			if(network == null)
			{
				network = pipes.getNetwork();
			}
			else if(network != pipes.getNetwork())
			{
				return false;
			}
		}
		if(network = null)
		{
			return false;
		}
		if(pipe.setNetwork(network))
		{
			network.addPiping(pipe);
			network.notifyNetworkOfUpdate();
			return true;
		}
		return false;
	}
	
	public static <T extends IPiping> void disconnectPiping(T pipe, ForgeDirection connDir)
	{
		pipe.pipingConnectionRemoved(connDir);
		BlockCoord loc = pipe.getLocation().getLocation(connDir);
		IPiping neighbor = PipingUtil.getPiping(pipe.getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, pipe.getBasePipingType());
		if(neighbor != null)
		{
			neighbor.pipingConnectionRemoved(connDir.getOpposite());
			if(neighbor.getNetwork() != null)
			{
				neighbor.getNetwork().destoryNetwork();
			}
		}
		if(pipe.getNetwork() != null)
		{
			pipe.getNetwork().destoryNetwork();
		}
	}
	
	public static <T extends IPiping> boolean joinPiping(T pipe, ForgeDirection faceHit)
	{
		BlockCoord loc = pipe.getLocation().getLocation(faceHit);
		IPiping neighbor = PipingUtil.getPiping(pipe.getBundle().getEntity().getWorldObj(), loc.x, loc.y, loc.z, pipe.getBasePipingType());
		if(neighbor != null && pipe.canConnectToPiping(faceHit, neighbor) && neighbor.canConnectToPiping(faceHit.getOpposite(), pipe));
		{
			pipe.pipingConnectionAdded(faceHit);
			neighbor.pipingConnectionAdded(faceHit.getOpposite());
			if(pipe.getNetwork() != null)
			{
				pipe.getNetwork().destoryNetwork();
			}
			return true;
		}
		return false;
	}
	
	public static boolean forceSkylightRecalulation(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		int height = worldObj.getHeightValue(xCoord, zCoord);
		if(height <= yCoord)
		{
			for(int i = 1; i < 12; i++)
			{
				if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord))
				{
					worldObj.setBlock(xCoord, yCoord + i, zCoord, Blocks.air);
					worldObj.setBlockToAir(xCoord, yCoord + i, zCoord);
					return true;
				}
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static FacadeRenderState getRequiredFacadeRenderState(IPipingBundle bundle, EntityPlayer player)
	{
		if(!bundle.hasFacade())
		{
			return FacadeRenderState.NONE;
		}
		if(isFacadeHidden(bundle, player))
		{
			return FacadeRenderState.WIRE_FRAME;
		}
		return FacadeRenderState.FULL;
	}
	
	public static boolean isSolidFacadeRendered(IPipingBundle bundle, EntityPlayer player)
	{
		return bundle.getFacadeID() > 0 && !isFacadeHidden(bundle, player);
	}
	
	public static boolean isFacadeHidden(IPipingBundle bundle, EntityPlayer player)
	{
		return bundle.getFacadeID() > 0 && (isToolEquipped(player) || isPipingEquipped(player));
	}
	
	
}
