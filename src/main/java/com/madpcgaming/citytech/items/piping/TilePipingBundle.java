package com.madpcgaming.citytech.items.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.items.piping.geom.CollidableCache;
import com.madpcgaming.citytech.items.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.items.piping.geom.Offset;
import com.madpcgaming.citytech.items.piping.geom.Offsets;
import com.madpcgaming.citytech.items.piping.geom.Offsets.Axis;
import com.madpcgaming.citytech.items.piping.geom.PipingConnectorType;
import com.madpcgaming.citytech.items.piping.geom.PipingGeometryUtil;
import com.madpcgaming.citytech.items.piping.item.IItemPiping;
import com.madpcgaming.citytech.items.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.lib.BlockCoord;
import com.madpcgaming.citytech.render.BoundingBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePipingBundle extends TileEntity implements IPipingBundle {

	  private final List<IPiping> pipe = new ArrayList<IPiping>();

	  private int facadeId = -1;
	  private int facadeMeta = 0;

	  private boolean facadeChanged;

	  private final List<CollidableComponent> cachedCollidables = new ArrayList<CollidableComponent>();

	  private final List<CollidableComponent> cachedConnectors = new ArrayList<CollidableComponent>();

	  private boolean pipingDirty = true;
	  private boolean collidablesDirty = true;
	  private boolean connectorsDirty = true;

	  private boolean clientUpdated = false;

	  private int lightOpacity = -1;

	  @SideOnly(Side.CLIENT)
	  private FacadeRenderState facadeRenderAs;

	  private PipingDisplayMode lastMode = PipingDisplayMode.ALL;

	  public TilePipingBundle() {
		  field_145854_h = ModBlocks.blockConduitBundle;
	  }

	  @Override
	  public void dirty() {
	    pipingDirty = true;
	    collidablesDirty = true;
	  }

	  @Override
	  public void func_145841_b(NBTTagCompound nbtRoot) {
	    super.func_145841_b(nbtRoot);

	    NBTTagList pipingTags = new NBTTagList();
	    for (IPiping piping : pipe) {
	      NBTTagCompound conduitRoot = new NBTTagCompound();
	      PipingUtil.writeToNBT(piping, conduitRoot);
	      pipingTags.appendTag(conduitRoot);
	    }
	    nbtRoot.setTag("piping", pipingTags);
	    nbtRoot.setInteger("facadeId", facadeId);
	    nbtRoot.setInteger("facadeMeta", facadeMeta);
	  }

	  @Override
	  public void func_145839_a(NBTTagCompound nbtRoot) {
	    super.func_145839_a(nbtRoot);

	    pipe.clear();
	    NBTTagList pipingTags = nbtRoot.func_150295_c("piping", 0);
	    for (int i = 0; i < pipingTags.tagCount(); i++) {
	      NBTTagCompound pipingTag = (NBTTagCompound) pipingTags.func_150305_b(i);
	      IPiping pipes = PipingUtil.readPipingFromNBT(pipingTag);
	      if(pipe != null) {
	        pipes.setBundle(this);
	        pipe.add(pipes);
	      }
	    }
	    facadeId = nbtRoot.getInteger("facadeId");
	    facadeMeta = nbtRoot.getInteger("facadeMeta");

	    if(field_145850_b != null && field_145850_b.isRemote) {
	      clientUpdated = true;
	    }

	  }

	  @Override
	  public boolean hasFacade() {
	    return facadeId > 0;
	  }

	  @Override
	  public void setFacadeId(int blockID, boolean triggerUpdate) {
	    this.facadeId = blockID;
	    if(triggerUpdate) {
	      facadeChanged = true;
	    }
	  }

	  @Override
	  public void setFacadeId(int blockID) {
	    setFacadeId(blockID, true);
	  }

	  @Override
	  public int getFacadeId() {
	    return facadeId;
	  }

	  @Override
	  public void setFacadeMetadata(int meta) {
	    facadeMeta = meta;
	  }

	  @Override
	  public int getFacadeMetadata() {
	    return facadeMeta;
	  }

	  @Override
	  @SideOnly(Side.CLIENT)
	  public FacadeRenderState getFacadeRenderedAs() {
	    if(facadeRenderAs == null) {
	      facadeRenderAs = FacadeRenderState.NONE;
	    }
	    return facadeRenderAs;
	  }

	  @Override
	  @SideOnly(Side.CLIENT)
	  public void setFacadeRenderAs(FacadeRenderState state) {
	    this.facadeRenderAs = state;
	  }

	  @Override
	  public int getLightOpacity() {
	    if((field_145850_b != null && !field_145850_b.isRemote) || lightOpacity == -1) {
	      return hasFacade() ? 255 : 0;
	    }
	    return lightOpacity;
	  }

	  @Override
	  public void setLightOpacity(int opacity) {
	    lightOpacity = opacity;
	  }

	  @Override
	  public Packet getDescriptionPacket() {
	    return PacketHandler.getPacket(this);
	  }

	  @Override
	  public void onChunkUnload() {
	    for (IPiping conduit : conduits) {
	      conduit.onChunkUnload(field_145850_b);
	    }
	  }

	  @Override
	  public void func_145845_h() {

	    if(field_145850_b == null) {
	      return;
	    }

	    for (IPiping conduit : pipe) {
	      conduit.updateEntity(field_145850_b);
	    }

	    if(conduitsDirty) {
	      if(!field_145850_b.isRemote) {
	    	  field_145850_b.func_147438_o(field_145851_c, field_145848_d, field_145849_e);
	      }
	      conduitsDirty = false;
	    }

	    if(facadeChanged) {
	      //force re-calc of lighting for both client and server
	      PipingUtil.forceSkylightRecalculation(field_145850_b, field_145851_c, field_145848_d, field_145849_e);
	      field_145850_b.updateAllLightTypes(field_145851_c, field_145848_d, field_145849_e);
	      field_145850_b.markBlockForRenderUpdate(field_145851_c, field_145848_d, field_145849_e);
	      facadeChanged = false;
	    }

	    //client side only, check for changes in rendering of the bundle
	    if(field_145850_b.isRemote) {

	      boolean markForUpdate = false;
	      if(clientUpdated) {
	        //TODO: This is not the correct solution here but just marking the block for a render update server side
	        //seems to get out of sync with the client sometimes so connections are not rendered correctly
	        markForUpdate = true;
	        clientUpdated = false;
	      }

	      FacadeRenderState curRS = getFacadeRenderedAs();
	      FacadeRenderState rs = PipingUtil.getRequiredFacadeRenderState(this, CityTech.proxy.getClientPlayer());
	      int curLO = getLightOpacity();
	      int shouldBeLO = rs == FacadeRenderState.FULL ? 255 : 0;
	      if(curLO != shouldBeLO) {
	        setLightOpacity(shouldBeLO);
	        field_145850_b.updateAllLightTypes(field_145851_c , field_145848_d, field_145849_e);
	      }
	      if(curRS != rs) {
	        setFacadeRenderAs(rs);
	        if(!PipingUtil.forceSkylightRecalculation(field_145850_b, field_145851_c , field_145848_d, field_145849_e)) {
	          markForUpdate = true;
	        }
	      } else { //can do the else as only need to update once      
	        PipingDisplayMode curMode = PipingDisplayMode.getDisplayMode(CityTech.proxy.getClientPlayer().getCurrentEquippedItem());
	        if(curMode != lastMode) {
	          markForUpdate = true;
	          lastMode = curMode;
	        }

	      }
	      if(markForUpdate) {
	    	  field_145850_b.markBlockForRenderUpdate(field_145851_c , field_145848_d, field_145849_e);
	      }
	    }
	  }

	  @Override
	  public BlockCoord getLocation() {
	    return new BlockCoord(field_145851_c , field_145848_d, field_145849_e);
	  }

	  @Override
	  public void onNeighborBlockChange(int blockId) {
	    boolean needsUpdate = false;
	    for (IPiping piping : conduits) {
	      needsUpdate |= piping.onNeighborBlockChange(blockId);
	    }
	    if(needsUpdate) {
	      dirty();
	    }
	  }

	  @Override
	  public TilePipingBundle getEntity() {
	    return this;
	  }

	  @Override
	  public boolean hasType(Class<? extends IPiping> type) {
	    return getPiping(type) != null;
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  public <T extends IPiping> T getPiping(Class<T> type) {
	    if(type == null) {
	      return null;
	    }
	    for (IPiping piping : pipe) {
	      if(type.isInstance(pipe)) {
	        return (T) pipe;
	      }
	    }
	    return null;
	  }

	  @Override
	  public void addConduit(IPiping pipe) {
	    if(field_145850_b.isRemote) {
	      return;
	    }
	    pipe.add(pipe);
	    pipe.setBundle(this);
	    pipe.onAddedToBundle();
	    dirty();
	  }

	  @Override
	  public void removeConduit(IPiping piping) {
	    if(piping != null) {
	      removePiping(piping, true);
	    }
	  }

	  public void removeConduit(IPiping conduit, boolean notify) {
	    if(field_145850_b.isRemote) {
	      return;
	    }
	    conduit.onRemovedFromBundle();
	    pipe.remove(conduit);
	    conduit.setBundle(null);
	    if(notify) {
	      dirty();
	    }
	  }

	  @Override
	  public void onBlockRemoved() {
	    if(field_145850_b.isRemote) {
	      return;
	    }
	    List<IPiping> copy = new ArrayList<IPiping>(pipe);
	    for (IPiping pipe : copy) {
	      removeConduit(pipe, false);
	    }
	    dirty();
	  }

	  @Override
	  public Collection<IPiping> getPiping() {
	    return pipe;
	  }

	  @Override
	  public Set<ForgeDirection> getConnections(Class<? extends IPiping> type) {
	    IPiping con = getPiping(type);
	    if(con != null) {
	      return con.getPipingConnections();
	    }
	    return null;
	  }

	  @Override
	  public boolean containsConnection(Class<? extends IPiping> type, ForgeDirection dir) {
	    IPiping con = getPiping(type);
	    if(con != null) {
	      return con.containsPipingConnection(dir);
	    }
	    return false;
	  }

	  @Override
	  public boolean containsConnection(ForgeDirection dir) {
	    for (IPiping con : pipe) {
	      if(con.containsPipingConnection(dir)) {
	        return true;
	      }
	    }
	    return false;
	  }

	  @Override
	  public Set<ForgeDirection> getAllConnections() {
	    Set<ForgeDirection> result = new HashSet<ForgeDirection>();
	    for (IPiping con : pipe) {
	      result.addAll(con.getPipingConnections());
	    }
	    return result;
	  }

	  // Geometry

	  @Override
	  public Offset getOffset(Class<? extends IPiping> type, ForgeDirection dir) {
	    if(getConnectionCount(dir) < 2) {
	      return Offset.NONE;
	    }
	    return Offsets.get(type, dir);
	  }

	  @Override
	  public List<CollidableComponent> getCollidableComponents() {

	    for (IPiping con : pipe) {
	      collidablesDirty = collidablesDirty || con.haveCollidablesChangedSinceLastCall();
	    }
	    if(collidablesDirty) {
	      connectorsDirty = true;
	    }
	    if(!collidablesDirty && !cachedCollidables.isEmpty()) {
	      return cachedCollidables;
	    }
	    cachedCollidables.clear();
	    for (IPiping conduit : pipe) {
	      cachedCollidables.addAll(conduit.getCollidableComponents());
	    }

	    addConnectors(cachedCollidables);

	    collidablesDirty = false;

	    return cachedCollidables;
	  }

	  @Override
	  public List<CollidableComponent> getConnectors() {
	    List<CollidableComponent> result = new ArrayList<CollidableComponent>();
	    addConnectors(result);
	    return result;
	  }

	  private void addConnectors(List<CollidableComponent> result) {

	    if(pipe.isEmpty()) {
	      return;
	    }

	    for (IPiping con : pipe) {
	      boolean b = con.haveCollidablesChangedSinceLastCall();
	      collidablesDirty = collidablesDirty || b;
	      connectorsDirty = connectorsDirty || b;
	    }

	    if(!connectorsDirty && !cachedConnectors.isEmpty()) {
	      result.addAll(cachedConnectors);
	      return;
	    }

	    cachedConnectors.clear();

	    List<CollidableComponent> coreBounds = new ArrayList<CollidableComponent>();
	    for (IPiping con : pipe) {
	      addPipingCores(coreBounds, con);
	    }
	    cachedConnectors.addAll(coreBounds);
	    result.addAll(coreBounds);

	    List<CollidableComponent> conduitsBounds = new ArrayList<CollidableComponent>();
	    for (IPiping con : pipe) {
	      conduitsBounds.addAll(con.getCollidableComponents());
	      addPipingCores(conduitsBounds, con);
	    }

	    Set<Class<IPiping>> collidingTypes = new HashSet<Class<IPiping>>();
	    for (CollidableComponent conCC : conduitsBounds) {
	      for (CollidableComponent innerCC : conduitsBounds) {
	        if(!InsulatedRedstoneConduit.COLOR_CONTROLLER_ID.equals(innerCC.data) && !InsulatedRedstoneConduit.COLOR_CONTROLLER_ID.equals(conCC.data)
	            && conCC != innerCC && conCC.bound.intersects(innerCC.bound)) {
	          collidingTypes.add((Class<IConduit>) conCC.conduitType);
	        }
	      }
	    }

	    //TODO: Remove the core geometries covered up by this as no point in rendering these
	    if(!collidingTypes.isEmpty()) {
	      List<CollidableComponent> colCores = new ArrayList<CollidableComponent>();
	      for (Class<IPiping> c : collidingTypes) {
	        IPiping con = getPiping(c);
	        if(con != null) {
	          addPipingCores(colCores, con);
	        }
	      }

	      BoundingBox bb = null;
	      for (CollidableComponent cBB : colCores) {
	        if(bb == null) {
	          bb = cBB.bound;
	        } else {
	          bb = bb.expandBy(cBB.bound);
	        }
	      }
	      if(bb != null) {
	        bb = bb.scale(1.05, 1.05, 1.05);
	        CollidableComponent cc = new CollidableComponent(null, bb, ForgeDirection.UNKNOWN,
	            PipingConnectorType.INTERNAL);
	        result.add(cc);
	        cachedConnectors.add(cc);
	      }
	    }

	    // External Connectors
	    Set<ForgeDirection> externalDirs = new HashSet<ForgeDirection>();
	    for (IPiping con : conduits) {
	      Set<ForgeDirection> extCons = con.getExternalConnections();
	      if(extCons != null) {
	        for (ForgeDirection dir : extCons) {
	          if(con.getConnectionMode(dir) != ConnectionMode.DISABLED) {
	            externalDirs.add(dir);
	          }
	        }
	      }
	    }
	    for (ForgeDirection dir : externalDirs) {
	      BoundingBox bb = PipingGeometryUtil.instance.getExternalConnectorBoundingBox(dir);
	      CollidableComponent cc = new CollidableComponent(null, bb, dir, PipingConnectorType.EXTERNAL);
	      result.add(cc);
	      cachedConnectors.add(cc);
	    }

	    connectorsDirty = false;

	  }

	  private boolean axisOfConnectionsEqual(Set<ForgeDirection> cons) {
	    Axis axis = null;
	    for (ForgeDirection dir : cons) {
	      if(axis == null) {
	        axis = Offsets.getAxisForDir(dir);
	      } else {
	        if(axis != Offsets.getAxisForDir(dir)) {
	          return false;
	        }
	      }
	    }
	    return true;
	  }

	  private void addConduitCores(List<CollidableComponent> result, IPiping con) {
	    CollidableCache cc = CollidableCache.instance;
	    Class<? extends IPiping> type = con.getCollidableType();
	    if(con.hasConnections()) {
	      for (ForgeDirection dir : con.getExternalConnections()) {
	        result.addAll(cc.getCollidables(cc.createKey(type, getOffset(con.getBasePipingType(), dir), ForgeDirection.UNKNOWN, false), con));
	      }
	      for (ForgeDirection dir : con.getPipingConnections()) {
	        result.addAll(cc.getCollidables(cc.createKey(type, getOffset(con.getBasePipingType(), dir), ForgeDirection.UNKNOWN, false), con));
	      }
	    } else {
	      result.addAll(cc.getCollidables(cc.createKey(type, getOffset(con.getBasePipingType(), ForgeDirection.UNKNOWN), ForgeDirection.UNKNOWN, false), con));
	    }
	  }

	  private boolean containsOnlySingleVerticalConnections() {
	    return getConnectionCount(ForgeDirection.UP) < 2 && getConnectionCount(ForgeDirection.DOWN) < 2;
	  }

	  private boolean containsOnlySingleHorizontalConnections() {
	    return getConnectionCount(ForgeDirection.WEST) < 2 && getConnectionCount(ForgeDirection.EAST) < 2 &&
	        getConnectionCount(ForgeDirection.NORTH) < 2 && getConnectionCount(ForgeDirection.SOUTH) < 2;
	  }

	  private boolean allDirectionsHaveSameConnectionCount() {
	    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
	      boolean hasCon = pipe.get(0).isConnectedTo(dir);
	      for (int i = 1; i < pipe.size(); i++) {
	        if(hasCon != pipe.get(i).isConnectedTo(dir)) {
	          return false;
	        }
	      }
	    }
	    return true;
	  }

	  private boolean containsOnlyHorizontalConnections() {
	    for (IPiping con : pipe) {
	      for (ForgeDirection dir : con.getPipingConnections()) {
	        if(dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
	          return false;
	        }
	      }
	      for (ForgeDirection dir : con.getExternalConnections()) {
	        if(dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
	          return false;
	        }
	      }
	    }
	    return true;
	  }

	  private int getConnectionCount(ForgeDirection dir) {
	    if(dir == ForgeDirection.UNKNOWN) {
	      return pipe.size();
	    }
	    int result = 0;
	    for (IPiping con : pipe) {
	      if(con.containsPipingConnections(dir) || con.containsExternalConnections(dir)) {
	        result++;
	      }
	    }
	    return result;
	  }

	  @Override
	  public World getWorld() {
	    return field_145850_b;
	  }

	  // ------- Liquids -----------------------------

	  @Override
	  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
	    ILiquidPiping lc = getPiping(ILiquidPiping.class);
	    if(lc != null) {
	      return lc.fill(from, resource, doFill);
	    }
	    return 0;
	  }

	  @Override
	  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
	    ILiquidPiping lp = getPiping(ILiquidPiping.class);
	    if(lp != null) {
	      return lp.drain(from, resource, doDrain);
	    }
	    return null;
	  }

	  @Override
	  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
	    ILiquidPiping lp = getPiping(ILiquidPiping.class);
	    if(lp != null) {
	      return lp.drain(from, maxDrain, doDrain);
	    }
	    return null;
	  }

	  @Override
	  public boolean canFill(ForgeDirection from, Fluid fluid) {
	    ILiquidPiping lp = getPiping(ILiquidPiping.class);
	    if(lp != null) {
	      return lp.canFill(from, fluid);
	    }
	    return false;
	  }

	  @Override
	  public boolean canDrain(ForgeDirection from, Fluid fluid) {
	    ILiquidPiping lp = getPiping(ILiquidPiping.class);
	    if(lp != null) {
	      return lp.canDrain(from, fluid);
	    }
	    return false;
	  }

	  @Override
	  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
	    ILiquidPiping lp = getPiping(ILiquidPiping.class);
	    if(lp != null) {
	      return lp.getTankInfo(from);
	    }
	    return null;
	  }

	  // ---- TE Item Conduits

	  @Override
	  public ItemStack sendItems(ItemStack item, ForgeDirection side) {
	    IItemPiping ip = getPiping(IItemPiping.class);
	    if(ip != null) {
	      return ip.sendItems(item, side);
	    }
	    return item;
	  }

	  @Override
	  public ItemStack insertItem(ForgeDirection from, ItemStack item, boolean simulate) {
	    IItemPiping ip = getPiping(IItemPiping.class);
	    if(ip != null) {
	      return ip.insertItem(from, item, simulate);
	    }
	    return item;
	  }

	  @Override
	  public ItemStack insertItem(ForgeDirection from, ItemStack item) {
	    IItemPiping ip = getPiping(IItemPiping.class);
	    if(ip != null) {
	      return ip.insertItem(from, item);
	    }
	    return item;
	  }

	}
