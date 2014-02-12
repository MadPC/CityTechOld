package com.madpcgaming.citytech.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cofh.api.transport.IItemConduit;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.piping.geom.CollidableCache;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.Offset;
import com.madpcgaming.citytech.piping.geom.Offsets;
import com.madpcgaming.citytech.piping.geom.Offsets.Axis;
import com.madpcgaming.citytech.piping.geom.PipingConnectorType;
import com.madpcgaming.citytech.piping.geom.PipingGeometryUtil;
import com.madpcgaming.citytech.piping.item.IItemPiping;
import com.madpcgaming.citytech.piping.liquid.ILiquidPiping;
import com.madpcgaming.citytech.piping.power.IPowerPiping;
import com.madpcgaming.citytech.piping.redstone.InsulatedRedstonePiping;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.util.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePipingBundle extends TileEntity implements IPipingBundle
{

	static final short						NBT_VERSION			= 1;
	private final List<IPiping>				pipes				= new ArrayList<IPiping>();
	private int								facadeId			= -1;
	private int								facadeMeta			= 0;
	private boolean							facadeChanged;
	private final List<CollidableComponent>	cachedCollidables	= new ArrayList<CollidableComponent>();
	private final List<CollidableComponent>	cachedConnectors	= new ArrayList<CollidableComponent>();
	private boolean							pipesDirty			= true;
	private boolean							collidablesDirty	= true;
	private boolean							connectorsDirty		= true;
	private boolean							clientUpdated		= false;
	private int								lightOpacity		= -1;
	@SideOnly(Side.CLIENT)
	private FacadeRenderState				facadeRenderAs;
	private PipingDisplayMode				lastMode			= PipingDisplayMode.ALL;
	
	
	public TilePipingBundle()
	{
		blockType = ModBlocks.blockPipingBundle;
	}

	
	public void dirty()
	{
		pipesDirty = true;
		collidablesDirty = true;
	}

	
	public void writeToNBT(NBTTagCompound nbtRoot)
	{
		super.writeToNBT(nbtRoot);

		NBTTagList pipeTags = new NBTTagList();
		for (IPiping pipe : pipes) {
			NBTTagCompound pipeRoot = new NBTTagCompound();
			PipingUtil.writeToNBT(pipe, pipeRoot);
			pipeTags.appendTag(pipeRoot);
		}
		nbtRoot.setTag("pipes", pipeTags);
		nbtRoot.setInteger("facadeId", facadeId);
		nbtRoot.setInteger("facadeMeta", facadeMeta);
		nbtRoot.setShort("nbtVersion", NBT_VERSION);
	}

	
	public void readFromNBT(NBTTagCompound nbtRoot)
	{
		super.readFromNBT(nbtRoot);

		short nbtVersion = nbtRoot.getShort("nbtVersion");

		pipes.clear();
		NBTTagList pipeTags = nbtRoot.getTagList("pipes", 0);
		for (int i = 0; i < pipeTags.tagCount(); i++) {
			NBTTagCompound pipeTag = (NBTTagCompound) pipeTags
					.getCompoundTagAt(i);
			IPiping pipe = PipingUtil.readPipingFromNBT(pipeTag, nbtVersion);
			if (pipe != null) {
				pipe.setBundle(this);
				pipes.add(pipe);
			}
		}
		facadeId = nbtRoot.getInteger("facadeId");
		facadeMeta = nbtRoot.getInteger("facadeMeta");

		if (worldObj != null && worldObj.isRemote) {
			clientUpdated = true;
		}

	}

	
	public boolean hasFacade()
	{
		return facadeId > 0;
	}

	
	public void setFacadeId(int blockID, boolean triggerUpdate)
	{
		this.facadeId = blockID;
		if (triggerUpdate) {
			facadeChanged = true;
		}
	}

	
	public void setFacadeId(int blockID)
	{
		setFacadeId(blockID, true);
	}

	
	public int getFacadeID()
	{
		return facadeId;
	}

	
	public void setFacadeMetadata(int meta)
	{
		facadeMeta = meta;
	}

	
	public int getFacadeMetadata()
	{
		return facadeMeta;
	}

	
	@SideOnly(Side.CLIENT)
	public FacadeRenderState getFacadeRenderedAs()
	{
		if (facadeRenderAs == null) {
			facadeRenderAs = FacadeRenderState.NONE;
		}
		return facadeRenderAs;
	}

	
	@SideOnly(Side.CLIENT)
	public void setFacadeRenderAs(FacadeRenderState state)
	{
		this.facadeRenderAs = state;
	}

	
	public int getLightOpacity()
	{
		if ((worldObj != null && !worldObj.isRemote) || lightOpacity == -1) {
			return hasFacade() ? 255 : 0;
		}
		return lightOpacity;
	}

	
	public void setLightOpacity(int opacity)
	{
		lightOpacity = opacity;
	}

	
	public void onChunkUnload()
	{
		for (IPiping pipe : pipes) {
			pipe.onChunkUnload(worldObj);
		}
	}

	
	public void updateEntity()
	{

		if (worldObj == null) {
			return;
		}

		for (IPiping pipe : pipes) {
			pipe.updateEntity(worldObj);
		}

		if (pipesDirty) {
			if (!worldObj.isRemote) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			pipesDirty = false;
		}

		if (facadeChanged) {
			PipingUtil.forceSkylightRecalculation(worldObj, xCoord, yCoord,	zCoord);
			worldObj.func_147451_t(xCoord, yCoord, zCoord);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			facadeChanged = false;
		}

		// client side only, check for changes in rendering of the bundle
		if (worldObj.isRemote) 
		{
			boolean markForUpdate = false;
			if (clientUpdated)
			{
				clientUpdated = false;
			}

			FacadeRenderState curRS = getFacadeRenderedAs();
			FacadeRenderState rs = PipingUtil.getRequiredFacadeRenderState(this, CityTech.proxy.getClientPlayer());
			int curLO = getLightOpacity();
			int shouldBeLO = rs == FacadeRenderState.FULL ? 255 : 0;
			if (curLO != shouldBeLO) 
			{
				setLightOpacity(shouldBeLO);
				worldObj.func_147451_t(xCoord, yCoord, zCoord);
			}
			if (curRS != rs) 
			{
				setFacadeRenderAs(rs);
				if (!PipingUtil.forceSkylightRecalculation(worldObj, xCoord,
						yCoord, zCoord)) 
				{
					markForUpdate = true;
				}
			}
			else {
				PipingDisplayMode curMode = PipingDisplayMode.getDisplayMode(CityTech.proxy.getClientPlayer().getCurrentEquippedItem());
				if (curMode != lastMode) 
				{
					markForUpdate = true;
					lastMode = curMode;
				}

			}
			if (markForUpdate) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	
	public BlockCoord getLocation()
	{
		return new BlockCoord(xCoord, yCoord, zCoord);
	}
	
	
	public void onNeighborBlockChange(Block block)
	{
		boolean needsUpdate = false;
		for (IPiping pipe : pipes) {
			needsUpdate |= pipe.onNeighborBlockChange(block);
		}
		if (needsUpdate) {
			dirty();
		}
	}	

	
	public TilePipingBundle getEntity()
	{
		return this;
	}

	
	public boolean hasType(Class<? extends IPiping> type)
	{
		return getPiping(type) != null;
	}

	@SuppressWarnings("unchecked")
	
	public <T extends IPiping> T getPiping(Class<T> type)
	{
		if (type == null) {
			return null;
		}
		for (IPiping pipe : pipes) {
			if (type.isInstance(pipe)) {
				return (T) pipe;
			}
		}
		return null;
	}

	
	public void addPiping(IPiping pipe)
	{
		if (worldObj.isRemote) {
			return;
		}
		pipes.add(pipe);
		pipe.setBundle(this);
		pipe.onAddedToBundle();
		dirty();
	}

	
	public void removePiping(IPiping pipe)
	{
		if (pipe != null) {
			removePiping(pipe, true);
		}
	}

	public void removePiping(IPiping pipe, boolean notify)
	{
		if (worldObj.isRemote) {
			return;
		}
		pipe.onRemovedFromBundle();
		pipes.remove(pipe);
		pipe.setBundle(null);
		if (notify) {
			dirty();
		}
	}

	
	public void onBlockRemoved()
	{
		if (worldObj.isRemote) {
			return;
		}
		List<IPiping> copy = new ArrayList<IPiping>(pipes);
		for (IPiping con : copy) {
			removePiping(con, false);
		}
		dirty();
	}

	
	public Collection<IPiping> getPiping()
	{
		return pipes;
	}

	
	public Set<ForgeDirection> getConnections(Class<? extends IPiping> type)
	{
		IPiping con = getPiping(type);
		if (con != null) {
			return con.getPipingConnections();
		}
		return null;
	}

	
	public boolean containsConnection(Class<? extends IPiping> type,
			ForgeDirection dir)
	{
		IPiping con = getPiping(type);
		if (con != null) {
			return con.containsPipingConnection(dir);
		}
		return false;
	}

	
	public boolean containsConnection(ForgeDirection dir)
	{
		for (IPiping con : pipes) {
			if (con.containsPipingConnection(dir)) {
				return true;
			}
		}
		return false;
	}

	
	public Set<ForgeDirection> getAllConnections()
	{
		Set<ForgeDirection> result = new HashSet<ForgeDirection>();
		for (IPiping con : pipes) {
			result.addAll(con.getPipingConnections());
		}
		return result;
	}

	// Geometry

	
	public Offset getOffset(Class<? extends IPiping> type, ForgeDirection dir)
	{
		if (getConnectionCount(dir) < 2) {
			return Offset.NONE;
		}
		return Offsets.get(type, dir);
	}

	
	public List<CollidableComponent> getCollidableComponents()
	{

		for (IPiping con : pipes) {
			collidablesDirty = collidablesDirty
					|| con.haveCollidablesChangedSinceLastCall();
		}
		if (collidablesDirty) {
			connectorsDirty = true;
		}
		if (!collidablesDirty && !cachedCollidables.isEmpty()) {
			return cachedCollidables;
		}
		cachedCollidables.clear();
		for (IPiping pipe : pipes) {
			cachedCollidables.addAll(pipe.getCollidableComponents());
		}

		addConnectors(cachedCollidables);

		collidablesDirty = false;

		return cachedCollidables;
	}

	
	public List<CollidableComponent> getConnectors()
	{
		List<CollidableComponent> result = new ArrayList<CollidableComponent>();
		addConnectors(result);
		return result;
	}

	private void addConnectors(List<CollidableComponent> result)
	{

		if (pipes.isEmpty()) {
			return;
		}

		for (IPiping con : pipes) {
			boolean b = con.haveCollidablesChangedSinceLastCall();
			collidablesDirty = collidablesDirty || b;
			connectorsDirty = connectorsDirty || b;
		}

		if (!connectorsDirty && !cachedConnectors.isEmpty()) {
			result.addAll(cachedConnectors);
			return;
		}

		cachedConnectors.clear();

		List<CollidableComponent> coreBounds = new ArrayList<CollidableComponent>();
		for (IPiping con : pipes) {
			addPipingCores(coreBounds, con);
		}
		cachedConnectors.addAll(coreBounds);
		result.addAll(coreBounds);

		List<CollidableComponent> pipesBounds = new ArrayList<CollidableComponent>();
		for (IPiping con : pipes) {
			pipesBounds.addAll(con.getCollidableComponents());
			addPipingCores(pipesBounds, con);
		}

		Set<Class<IPiping>> collidingTypes = new HashSet<Class<IPiping>>();
		for (CollidableComponent conCC : pipesBounds) {
			for (CollidableComponent innerCC : pipesBounds) {
				if (!InsulatedRedstonePiping.COLOR_CONTROLLER_ID
						.equals(innerCC.data)
						&& !InsulatedRedstonePiping.COLOR_CONTROLLER_ID
								.equals(conCC.data)
						&& conCC != innerCC
						&& conCC.bound.intersects(innerCC.bound)) {
					collidingTypes.add((Class<IPiping>) conCC.pipingType);
				}
			}
		}


		if (!collidingTypes.isEmpty()) {
			List<CollidableComponent> colCores = new ArrayList<CollidableComponent>();
			for (Class<IPiping> c : collidingTypes) {
				IPiping con = getPiping(c);
				if (con != null) {
					addPipingCores(colCores, con);
				}
			}

			BoundingBox bb = null;
			for (CollidableComponent cBB : colCores) {
				if (bb == null) {
					bb = cBB.bound;
				}
				else {
					bb = bb.expandBy(cBB.bound);
				}
			}
			if (bb != null) {
				bb = bb.scale(1.05, 1.05, 1.05);
				CollidableComponent cc = new CollidableComponent(null, bb,
						ForgeDirection.UNKNOWN, PipingConnectorType.INTERNAL);
				result.add(cc);
				cachedConnectors.add(cc);
			}
		}

		// External Connectors
		Set<ForgeDirection> externalDirs = new HashSet<ForgeDirection>();
		for (IPiping con : pipes) {
			Set<ForgeDirection> extCons = con.getExternalConnections();
			if (extCons != null) {
				for (ForgeDirection dir : extCons) {
					if (con.getConnectionMode(dir) != ConnectionMode.DISABLED) {
						externalDirs.add(dir);
					}
				}
			}
		}
		for (ForgeDirection dir : externalDirs) {
			BoundingBox bb = PipingGeometryUtil.instance
					.getExternalConnectorBoundingBox(dir);
			CollidableComponent cc = new CollidableComponent(null, bb, dir,
					PipingConnectorType.EXTERNAL);
			result.add(cc);
			cachedConnectors.add(cc);
		}

		connectorsDirty = false;

	}

	private boolean axisOfConnectionsEqual(Set<ForgeDirection> cons)
	{
		Axis axis = null;
		for (ForgeDirection dir : cons) {
			if (axis == null) {
				axis = Offsets.getAxisForDir(dir);
			}
			else {
				if (axis != Offsets.getAxisForDir(dir)) {
					return false;
				}
			}
		}
		return true;
	}

	private void addPipingCores(List<CollidableComponent> result, IPiping con)
	{
		CollidableCache cc = CollidableCache.instance;
		Class<? extends IPiping> type = con.getCollidableType();
		if (con.hasConnections()) {
			for (ForgeDirection dir : con.getExternalConnections()) {
				result.addAll(cc.getCollidables(cc.createKey(type,
						getOffset(con.getBasePipingType(), dir),
						ForgeDirection.UNKNOWN, false), con));
			}
			for (ForgeDirection dir : con.getPipingConnections()) {
				result.addAll(cc.getCollidables(cc.createKey(type,
						getOffset(con.getBasePipingType(), dir),
						ForgeDirection.UNKNOWN, false), con));
			}
		}
		else {
			result.addAll(cc.getCollidables(cc.createKey(type,
					getOffset(con.getBasePipingType(), ForgeDirection.UNKNOWN),
					ForgeDirection.UNKNOWN, false), con));
		}
	}

	private boolean containsOnlySingleVerticalConnections()
	{
		return getConnectionCount(ForgeDirection.UP) < 2
				&& getConnectionCount(ForgeDirection.DOWN) < 2;
	}

	private boolean containsOnlySingleHorizontalConnections()
	{
		return getConnectionCount(ForgeDirection.WEST) < 2
				&& getConnectionCount(ForgeDirection.EAST) < 2
				&& getConnectionCount(ForgeDirection.NORTH) < 2
				&& getConnectionCount(ForgeDirection.SOUTH) < 2;
	}

	private boolean allDirectionsHaveSameConnectionCount()
	{
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			boolean hasCon = pipes.get(0).isConnectedTo(dir);
			for (int i = 1; i < pipes.size(); i++) {
				if (hasCon != pipes.get(i).isConnectedTo(dir)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean containsOnlyHorizontalConnections()
	{
		for (IPiping con : pipes) {
			for (ForgeDirection dir : con.getPipingConnections()) {
				if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
					return false;
				}
			}
			for (ForgeDirection dir : con.getExternalConnections()) {
				if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
					return false;
				}
			}
		}
		return true;
	}

	private int getConnectionCount(ForgeDirection dir)
	{
		if (dir == ForgeDirection.UNKNOWN) {
			return pipes.size();
		}
		int result = 0;
		for (IPiping con : pipes) {
			if (con.containsPipingConnection(dir)
					|| con.containsExternalConnection(dir)) {
				result++;
			}
		}
		return result;
	}

	// ------------ Power -----------------------------

	
	public void doWork(PowerHandler workProvider)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			pc.doWork(workProvider);
		}
	}

	
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.getPowerReceiver(side);
		}
		return null;
	}

	
	public PowerHandler getPowerHandler()
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.getPowerHandler();
		}
		return null;
	}

	
	public void applyPerdition()
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			pc.applyPerdition();
		}

	}

	
	public World getWorld()
	{
		return worldObj;
	}

	
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.receiveEnergy(from, maxReceive, simulate);
		}
		return 0;
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.extractEnergy(from, maxExtract, simulate);
		}
		return 0;
	}

	public boolean canInterface(ForgeDirection from)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.canInterface(from);
		}
		return false;
	}
	
	public int getEnergyStored(ForgeDirection from)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.getEnergyStored(from);
		}
		return 0;
	}

	public int getMaxEnergyStored(ForgeDirection from)
	{
		IPowerPiping pc = getPiping(IPowerPiping.class);
		if (pc != null) {
			return pc.getMaxEnergyStored(from);
		}
		return 0;
	}

	// ------- Liquids -----------------------------

	
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.fill(from, resource, doFill);
		}
		return 0;
	}

	
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.drain(from, resource, doDrain);
		}
		return null;
	}

	
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.drain(from, maxDrain, doDrain);
		}
		return null;
	}

	
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.canFill(from, fluid);
		}
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.canDrain(from, fluid);
		}
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		ILiquidPiping lc = getPiping(ILiquidPiping.class);
		if (lc != null) {
			return lc.getTankInfo(from);
		}
		return null;
	}

	// ---- TE Item Pipings

	
	public ItemStack sendItems(ItemStack item, ForgeDirection side)
	{
		IItemConduit ic = getPiping(IItemPiping.class);
		if (ic != null) {
			return ic.sendItems(item, side);
		}
		return item;
	}

	
	public ItemStack insertItem(ForgeDirection from, ItemStack item, boolean simulate)
	{
		IItemConduit ic = getPiping(IItemPiping.class);
		if (ic != null) {
			return ic.insertItem(from, item, simulate);
		}
		return item;
	}

	
	public ItemStack insertItem(ForgeDirection from, ItemStack item)
	{
		IItemConduit ic = getPiping(IItemPiping.class);
		if (ic != null) {
			return ic.insertItem(from, item);
		}
		return item;
	}

}