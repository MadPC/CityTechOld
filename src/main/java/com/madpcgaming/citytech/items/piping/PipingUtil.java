package com.madpcgaming.citytech.items.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.madpcgaming.buildcraft.api.tools.IToolWrench;
import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.items.piping.IPipingBundle.FacadeRenderState;
import com.madpcgaming.citytech.items.piping.item.ItemPipingNetwork;
import com.madpcgaming.citytech.items.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.items.piping.liquid.LiquidPipingNetwork;
import com.madpcgaming.citytech.lib.BlockCoord;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PipingUtil
{
	 public static final Random RANDOM = new Random();

	  public static AbstractPipingNetwork<?> createNetworkForType(Class<? extends IPiping> type) {
	   if(ILiquidPiping.class.isAssignableFrom(type)) {
	      return new LiquidPipingNetwork();
	    } else if(IItemConduit.class.isAssignableFrom(type)) {
	      return new ItemPipingNetwork();
	    }
	    FMLCommonHandler.instance().raiseException(new Exception("Could not determine network type for class " + type), "ConduitUtil.createNetworkForType", false);
	    return null;
	  }

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	  public static void ensureValidNetwork(IPiping piping) {
	    TileEntity te = piping.getBundle().getEntity();
	    World world = te.func_145831_w();
	    Collection<? extends IPiping> connections = PipingUtil.getConnectedPiping(world, te.field_145851_c, te.field_145848_d, te.field_145849_e, piping.getBasePipingType());

	    if(reuseNetwork(piping, connections, world)) {
	      return;
	    }

	    AbstractPipingNetwork res = createNetworkForType(piping.getBasePipingType());
	    res.init(piping.getBundle(), connections, world);
	    return;
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  private static boolean reuseNetwork(IPiping pipe, Collection<? extends IPiping> connections, World world) {
	    AbstractPipingNetwork network = null;
	    for (IPiping conduit : connections) {
	      if(network == null) {
	        network = conduit.getNetwork();
	      } else if(network != conduit.getNetwork()) {
	        return false;
	      }
	    }
	    if(network == null) {
	      return false;
	    }
	    if(pipe.setNetwork(network)) {
	      network.addPiping(pipe);
	      network.notifyNetworkOfUpdate();
	      return true;
	    }
	    return false;
	  }

	  public static <T extends IPiping> void disconectConduits(T pipe, ForgeDirection connDir) {
	    pipe.pipingConnectionRemoved(connDir);
	    BlockCoord loc = pipe.getLocation().getLocation(connDir);
	    IPiping neighbour = PipingUtil.getPiping(pipe.getBundle().getEntity().func_145831_w(), loc.x, loc.y, loc.z, pipe.getBasePipingType());
	    if(neighbour != null) {
	      neighbour.pipingConnectionRemoved(connDir.getOpposite());
	      if(neighbour.getNetwork() != null) {
	        neighbour.getNetwork().destroyNetwork();
	      }
	    }
	    if(pipe.getNetwork() != null) { //this should have been destroyed when destroying the neighbours network but lets just make sure
	      pipe.getNetwork().destroyNetwork();
	    }
	  }

	  public static <T extends IPiping> boolean joinPipes(T pipe, ForgeDirection faceHit) {
	    BlockCoord loc = pipe.getLocation().getLocation(faceHit);
	    IPiping neighbour = PipingUtil.getPiping(pipe.getBundle().getEntity().func_145831_w(), loc.x, loc.y, loc.z, pipe.getBasePipingType());
	    if(neighbour != null && pipe.canConnectToPiping(faceHit, neighbour) && neighbour.canConnectToPiping(faceHit.getOpposite(), pipe)) {
	      pipe.pipingConnectionAdded(faceHit);
	      neighbour.pipingConnectionAdded(faceHit.getOpposite());
	      if(pipe.getNetwork() != null) {
	        pipe.getNetwork().destroyNetwork();
	      }
	      if(neighbour.getNetwork() != null) {
	        neighbour.getNetwork().destroyNetwork();
	      }
	      return true;
	    }
	    return false;
	  }

	  public static boolean forceSkylightRecalculation(World worldObj, int xCoord, int yCoord, int zCoord) {
	    int height = worldObj.getHeightValue(xCoord, zCoord);
	    if(height <= yCoord) {
	      for (int i = 1; i < 12; i++) {
	        if(worldObj.func_147437_c(xCoord, yCoord + i, zCoord)) {
	          //We need to force the re-lighting of the column due to a change
	          //in the light reaching bellow the block from the sky. To avoid 
	          //modifying core classes to expose this functionality I am just placing then breaking
	          //a block above this one to force the check
	          worldObj.func_147465_d(xCoord, yCoord + i, zCoord, Blocks.stone, 0, 3);
	          worldObj.func_147468_f(xCoord, yCoord + i, zCoord);
	          return true;
	        }
	      }
	    }
	    return false;
	  }

	  @SideOnly(Side.CLIENT)
	  public static FacadeRenderState getRequiredFacadeRenderState(IPipingBundle bundle, EntityPlayer player) {
	    if(!bundle.hasFacade()) {
	      return FacadeRenderState.NONE;
	    }
	    if(isFacadeHidden(bundle, player)) {
	      return FacadeRenderState.WIRE_FRAME;
	    }
	    return FacadeRenderState.FULL;
	  }

	  public static boolean isSolidFacadeRendered(IPipingBundle bundle, EntityPlayer player) {
	    return bundle.getFacadeId() > 0 && !isFacadeHidden(bundle, player);
	  }

	  public static boolean isFacadeHidden(IPipingBundle bundle, EntityPlayer player) {
	    //ModuleManager.itemHasActiveModule(player.getCurrentEquippedItem, OmniWrenchModule.MODULE_OMNI_WRENCH)
	    return bundle.getFacadeId() > 0 && (isToolEquipped(player) || isConduitEquipped(player));
	  }

	  public static PipingDisplayMode getDisplayMode(EntityPlayer player) {
	    player = player == null ? CityTech.proxy.getClientPlayer() : player;
	    if(player == null) {
	      return PipingDisplayMode.ALL;
	    }
	    ItemStack equipped = player.getCurrentEquippedItem();
	    if(equipped == null) {
	      return PipingDisplayMode.ALL;
	    }
	    if(equipped.getItem() != ModItems.Wrench) {
	      return PipingDisplayMode.ALL;
	    }
	    PipingDisplayMode result = PipingDisplayMode.getDisplayMode(equipped);
	    if(result == null) {
	      return PipingDisplayMode.ALL;
	    }
	    return result;
	  }

	  public static boolean renderConduit(EntityPlayer player, IPiping pipe) {
	    if(player == null || pipe == null) {
	      return true;
	    }
	    return renderConduit(player, pipe.getBasePipingType());
	  }

	  public static boolean renderConduit(EntityPlayer player, Class<? extends IPiping> pipingType) {
	    if(player == null || pipingType == null) {
	      return true;
	    }
	    PipingDisplayMode mode = getDisplayMode(player);
	    switch (mode) {
	    case ALL:
	      return true;
	    case FLUID:
	      return pipingType == ILiquidPiping.class;
	    case ITEM:
	      return pipingType == IItemConduit.class;
	    default:
	      break;
	    }
	    return true;
	  }

	  public static boolean isConduitEquipped(EntityPlayer player) {
	    player = player == null ? CityTech.proxy.getClientPlayer() : player;
	    if(player == null) {
	      return false;
	    }
	    ItemStack equipped = player.getCurrentEquippedItem();
	    if(equipped == null) {
	      return false;
	    }
	    return equipped.getItem() instanceof IPipingItem;
	  }

	  public static boolean isToolEquipped(EntityPlayer player) {
	    player = player == null ? CityTech.proxy.getClientPlayer() : player;
	    if(player == null) {
	      return false;
	    }
	    ItemStack equipped = player.getCurrentEquippedItem();
	    if(equipped == null) {
	      return false;
	    }
	    //Removed until MPS is updated.
	    //if(MpsUtil.instance.isPowerFistEquiped(equipped)) {
	      //return MpsUtil.instance.isOmniToolActive(equipped);
	    //}
	    return equipped.getItem() instanceof IToolWrench;
	  }

	  public static <T extends IPiping> T getPiping(IBlockAccess world, int x, int y, int z, Class<T> type) {
		    if(world == null) {
		      return null;
		    }
		    TileEntity te = world.func_147438_o(x, y, z);
		    if(te instanceof IPipingBundle) {
		      IPipingBundle con = (IPipingBundle) te;
		      return con.getPiping(type);
		    }
		    return null;
		  }

	  public static <T extends IPiping> T getPiping(IBlockAccess world, TileEntity te, ForgeDirection dir, Class<T> type) {
	    return PipingUtil.getPiping(world, te.field_145851_c + dir.offsetX, te.field_145848_d + dir.offsetY, te.field_145849_e + dir.offsetZ, type);
	  }

	  public static <T extends IPiping> Collection<T> getConnectedPiping(IBlockAccess world, int x, int y, int z, Class<T> type) {
	    TileEntity te = world.func_147438_o(x, y, z);
	    if(!(te instanceof IPipingBundle)) {
	      return Collections.emptyList();
	    }
	    List<T> result = new ArrayList<T>();
	    IPipingBundle root = (IPipingBundle) te;
	    T con = root.getPiping(type);
	    if(con != null) {
	      for (ForgeDirection dir : con.getPipingConnections()) {
	        T connected = getPiping(world, root.getEntity(), dir, type);
	        if(connected != null) {
	          result.add(connected);
	        }

	      }
	    }
	    return result;
	  }

	  public static void writeToNBT(IPiping piping, NBTTagCompound pipingRoot) {
	    if(piping == null) {
	      return;
	    }

	    NBTTagCompound pipingBody = new NBTTagCompound();
	    piping.writeToNBT(pipingBody);

	    pipingRoot.setString("pipingType", piping.getClass().getCanonicalName());
	    pipingRoot.setTag("conduit", pipingBody);
	  }

	  public static IPiping readPipingFromNBT(NBTTagCompound pipingRoot) {
	    String typeName = pipingRoot.getString("pipingType");
	    NBTTagCompound pipingBody = pipingRoot.getCompoundTag("piping");
	    if(typeName == null || pipingBody == null) {
	      return null;
	    }
	    IPiping result;
	    try {
	      result = (IPiping) Class.forName(typeName).newInstance();
	    } catch (Exception e) {
	      throw new RuntimeException("Could not create an instance of the conduit with name: " + typeName, e);
	    }
	    result.readFromNBT(pipingBody);
	    return result;

	  }

	  public static boolean isFluidValid(FluidStack fluidStack) {
	    if(fluidStack != null) {
	      String name = FluidRegistry.getFluidName(fluidStack);
	      if(name != null && !name.trim().isEmpty()) {
	        return true;
	      }
	    }
	    return false;
	  }

	}