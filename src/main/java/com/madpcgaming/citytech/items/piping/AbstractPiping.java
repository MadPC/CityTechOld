package com.madpcgaming.citytech.items.piping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.items.piping.geom.CollidableCache;
import com.madpcgaming.citytech.items.piping.geom.CollidableCache.CacheKey;
import com.madpcgaming.citytech.items.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.lib.BlockCoord;

public class AbstractPiping implements IPiping
{
	protected final Set<ForgeDirection>						conduitConnections						= new HashSet<ForgeDirection>();

	protected final Set<ForgeDirection>						externalConnections						= new HashSet<ForgeDirection>();

	public static final float								STUB_WIDTH								= 0.2f;

	public static final float								STUB_HEIGHT								= 0.2f;

	public static final float								TRANSMISSION_SCALE						= 0.3f;

	// NB: This is a transient field controlled by the owning bundle. It is not
	// written to the NBT etc
	protected IPipingBundle								bundle;

	protected boolean										active;

	protected List<CollidableComponent>						collidables;

	protected final EnumMap<ForgeDirection, ConnectionMode>	conectionModes							= new EnumMap<ForgeDirection, ConnectionMode>(
																											ForgeDirection.class);

	protected boolean										collidablesDirty						= true;

	private boolean											clientStateDirty						= true;

	private boolean											dodgyChangeSinceLastCallFlagForBundle	= true;

	private int												lastNumConections						= -1;

	private boolean											updateConnections						= true;

	protected AbstractPiping()
	{
	}

	@Override
	public ConnectionMode getConnectionMode(ForgeDirection dir)
	{
		ConnectionMode res = conectionModes.get(dir);
		if (res == null) {
			return getDefaultConnectionMode();
		}
		return res;
	}

	protected ConnectionMode getDefaultConnectionMode()
	{
		return ConnectionMode.IN_OUT;
	}

	@Override
	public void setConnectionMode(ForgeDirection dir, ConnectionMode mode)
	{
		ConnectionMode oldVal = conectionModes.get(dir);
		if (oldVal == mode) {
			return;
		}
		if (mode == null) {
			conectionModes.remove(dir);
		} else {
			conectionModes.put(dir, mode);
		}
		clientStateDirty = true;
		collidablesDirty = true;
	}

	@Override
	public boolean hasConnectionMode(ConnectionMode mode)
	{
		for (ConnectionMode cm : conectionModes.values()) {
			if (cm == mode) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ConnectionMode getNextConnectionMode(ForgeDirection dir)
	{
		ConnectionMode curMode = getConnectionMode(dir);
		ConnectionMode next = ConnectionMode.getNext(curMode);
		if (next == ConnectionMode.NOT_SET) {
			next = ConnectionMode.IN_OUT;
		}
		return next;
	}

	@Override
	public ConnectionMode getPreviousConnectionMode(ForgeDirection dir)
	{
		ConnectionMode curMode = getConnectionMode(dir);
		ConnectionMode prev = ConnectionMode.getPrevious(curMode);
		if (prev == ConnectionMode.NOT_SET) {
			prev = ConnectionMode.DISABLED;
		}
		return prev;
	}

	@Override
	public boolean haveCollidablesChangedSinceLastCall()
	{
		if (dodgyChangeSinceLastCallFlagForBundle) {
			dodgyChangeSinceLastCallFlagForBundle = false;
			return true;
		}
		return false;
	}

	@Override
	public BlockCoord getLocation()
	{
		if (bundle == null) {
			return null;
		}
		TileEntity te = bundle.getEntity();
		if (te == null) {
			return null;
		}
		return new BlockCoord(te.field_145851_c, te.field_145848_d, te.field_145849_e);
	}

	@Override
	public void setBundle(IPipingBundle tileConduitBundle)
	{
		bundle = tilePipingBundle;
	}

	@Override
	public IPipingBundle getBundle()
	{
		return bundle;
	}

	// Connections
	@Override
	public Set<ForgeDirection> getPipingConnections()
	{
		return conduitConnections;
	}

	@Override
	public boolean containsPipingConnection(ForgeDirection dir)
	{
		return conduitConnections.contains(dir);
	}

	@Override
	public void pipingConnectionAdded(ForgeDirection fromDirection)
	{
		conduitConnections.add(fromDirection);
		connectionsChanged();
	}

	@Override
	public void pipingConnectionRemoved(ForgeDirection fromDirection)
	{
		conduitConnections.remove(fromDirection);
		connectionsChanged();
	}

	@Override
	public boolean canConnectToPiping(ForgeDirection direction,
			IPiping conduit)
	{
		if (conduit == null) {
			return false;
		}
		return getConnectionMode(direction) != ConnectionMode.DISABLED
				&& conduit.getConnectionMode(direction.getOpposite()) != ConnectionMode.DISABLED;
	}

	@Override
	public boolean canConnectToExternal(ForgeDirection direction,
			boolean ignoreConnectionMode)
	{
		return false;
	}

	@Override
	public Set<ForgeDirection> getExternalConnections()
	{
		return externalConnections;
	}

	@Override
	public boolean hasExternalConnections()
	{
		return !externalConnections.isEmpty();
	}

	@Override
	public boolean hasConnections()
	{
		return hasPipingConnections() || hasExternalConnections();
	}

	@Override
	public boolean hasPipingConnections()
	{
		return !conduitConnections.isEmpty();
	}

	@Override
	public boolean containsExternalConnections(ForgeDirection dir)
	{
		return externalConnections.contains(dir);
	}

	@Override
	public void externalConnectionAdded(ForgeDirection fromDirection)
	{
		externalConnections.add(fromDirection);
		connectionsChanged();
	}

	@Override
	public void externalConnectionRemoved(ForgeDirection fromDirection)
	{
		externalConnections.remove(fromDirection);
		conectionModes.remove(fromDirection);
		connectionsChanged();
	}

	@Override
	public boolean isConnectedTo(ForgeDirection dir)
	{
		return containsPipingConnection(dir)
				|| containsExternalConnections(dir);
	}

	@Override
	public boolean isActive()
	{
		return active;
	}

	@Override
	public void setActive(boolean active)
	{
		if (active != this.active) {
			clientStateDirty = true;
		}
		this.active = active;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtRoot)
	{
		int[] dirs = new int[conduitConnections.size()];
		Iterator<ForgeDirection> cons = conduitConnections.iterator();
		for (int i = 0; i < dirs.length; i++) {
			dirs[i] = cons.next().ordinal();
		}
		nbtRoot.setIntArray("connections", dirs);

		dirs = new int[externalConnections.size()];
		cons = externalConnections.iterator();
		for (int i = 0; i < dirs.length; i++) {
			dirs[i] = cons.next().ordinal();
		}
		nbtRoot.setIntArray("externalConnections", dirs);
		nbtRoot.setBoolean("signalActive", active);

		if (conectionModes.size() > 0) {
			byte[] modes = new byte[6];
			int i = 0;
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				modes[i] = (byte) getConnectionMode(dir).ordinal();
				i++;
			}
			nbtRoot.setByteArray("conModes", modes);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtRoot)
	{
		conduitConnections.clear();
		int[] dirs = nbtRoot.getIntArray("connections");
		for (int i = 0; i < dirs.length; i++) {
			conduitConnections.add(ForgeDirection.values()[dirs[i]]);
		}

		externalConnections.clear();
		dirs = nbtRoot.getIntArray("externalConnections");
		for (int i = 0; i < dirs.length; i++) {
			externalConnections.add(ForgeDirection.values()[dirs[i]]);
		}
		active = nbtRoot.getBoolean("signalActive");

		conectionModes.clear();
		byte[] modes = nbtRoot.getByteArray("conModes");
		if (modes != null && modes.length == 6) {
			int i = 0;
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				conectionModes.put(dir, ConnectionMode.values()[modes[i]]);
				i++;
			}
		}
	}

	@Override
	public int getLightValue()
	{
		return 0;
	}

	@Override
	public boolean onBlockActivated(EntityPlayer player, RaytraceResult res,
			List<RaytraceResult> all)
	{
		return false;
	}

	@Override
	public float getSelfIlluminationForState(CollidableComponent component)
	{
		return isActive() ? 1 : 0;
	}

	@Override
	public float getTransmitionGeometryScale()
	{
		return TRANSMISSION_SCALE;
	}

	@Override
	public void onChunkUnload(World worldObj)
	{
		AbstractPipingNetwork<?> network = getNetwork();
		if (network != null) {
			network.destroyNetwork();
		}
	}

	@Override
	public void updateEntity(World world)
	{
		if (world.isRemote) {
			return;
		}
		updateNetwork(world);
		updateConnections();
		if (clientStateDirty && getBundle() != null) {
			getBundle().dirty();
			clientStateDirty = false;
		}
	}

	private void updateConnections()
	{
		if (!updateConnections) {
			return;
		}

		boolean externalConnectionsChanged = false;
		List<ForgeDirection> copy = new ArrayList<ForgeDirection>(
				externalConnections);
		// remove any no longer valid connections
		for (ForgeDirection dir : copy) {
			if (!canConnectToExternal(dir, false)) {
				externalConnectionRemoved(dir);
				externalConnectionsChanged = true;
			}
		}

		// then check for new ones
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (!conduitConnections.contains(dir)
					&& !externalConnections.contains(dir)) {
				if (canConnectToExternal(dir, false)) {
					externalConnectionAdded(dir);
					externalConnectionsChanged = true;
				}
			}
		}
		if (externalConnectionsChanged) {
			connectionsChanged();
		}

		updateConnections = false;
	}

	protected void connectionsChanged()
	{
		collidablesDirty = true;
		clientStateDirty = true;
		dodgyChangeSinceLastCallFlagForBundle = true;
	}

	protected void setClientStateDirty()
	{
		clientStateDirty = true;
	}

	protected void updateNetwork(World world)
	{
		if (getNetwork() == null) {
			PipingUtil.ensureValidNetwork(this);
			if (getNetwork() != null && !world.isRemote && bundle != null) {
				world.notifyBlocksOfNeighborChange(bundle.getEntity().field_145851_c,
						bundle.getEntity().field_145848_d, bundle.getEntity().field_145849_e,
						bundle.getEntity().func_145838_q());
			}
		}
		if (getNetwork() != null) {
			getNetwork().onUpdateEntity(this);
		}
	}

	@Override
	public void onAddedToBundle()
	{

		TileEntity te = bundle.getEntity();
		World world = te.func_145831_w();

		conduitConnections.clear();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			IPiping neighbour = PipingUtil.getPiping(world, te, dir,
					getBasePipingType());
			if (neighbour != null
					&& neighbour.canConnectToPiping(dir.getOpposite(), this)) {
				conduitConnections.add(dir);
				neighbour.pipingConnectionAdded(dir.getOpposite());
			}
		}

		externalConnections.clear();
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (!containsPipingConnection(dir)
					&& canConnectToExternal(dir, false)) {
				externalConnectionAdded(dir);
			}
		}

		connectionsChanged();
	}

	@Override
	public void onRemovedFromBundle()
	{
		TileEntity te = bundle.getEntity();
		World world = te.worldObj;

		for (ForgeDirection dir : conduitConnections) {
			IConduit neighbour = ConduitUtil.getConduit(world, te, dir,
					getBaseConduitType());
			if (neighbour != null) {
				neighbour.conduitConnectionRemoved(dir.getOpposite());
			}
		}
		conduitConnections.clear();

		if (!externalConnections.isEmpty()) {
			world.notifyBlocksOfNeighborChange(te.field_145851_c, te.field_145848_d, te.field_145849_e,
					EnderIO.blockConduitBundle.blockID);
		}
		externalConnections.clear();

		AbstractConduitNetwork<?> network = getNetwork();
		if (network != null) {
			network.destroyNetwork();
		}
		connectionsChanged();
	}

	@Override
	public boolean onNeighborBlockChange(int blockId)
	{
		// Check for changes to external connections, connections to conduits
		// are
		// handled by the bundle

		// NB: No need to check externals if the neighbour that changed was a
		// conduit bundle as this
		// can't effect external connections.
		if (blockId == CityTech.blockConduitBundle.blockID) {
			return false;
		}

		updateConnections = true;

		return true;
	}

	@Override
	public Collection<CollidableComponent> createCollidables(CacheKey key)
	{
		return Collections.singletonList(new CollidableComponent(getCollidableType(), PipingGeometryUtil.instance
				.getBoundingBox(getBasePipingType(), key.dir,
								key.isStub, key.offset), key.dir, null));
	}

	@Override
	public Class<? extends IPiping> getCollidableType()
	{
		return getBasePipingType();
	}

	@Override
	public List<CollidableComponent> getCollidableComponents()
	{

		if (collidables != null && !collidablesDirty) {
			return collidables;
		}

		List<CollidableComponent> result = new ArrayList<CollidableComponent>();
		CollidableCache cc = CollidableCache.instance;

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			Collection<CollidableComponent> col = getCollidables(dir);
			if (col != null) {
				result.addAll(col);
			}
		}
		collidables = result;

		collidablesDirty = false;

		return result;
	}

	protected boolean renderStub(ForgeDirection dir)
	{
		return getConnectionMode(dir) == ConnectionMode.DISABLED;
	}

	private Collection<CollidableComponent> getCollidables(ForgeDirection dir)
	{
		CollidableCache cc = CollidableCache.instance;
		Class<? extends IPiping> type = getCollidableType();
		if (isConnectedTo(dir)) {
			return cc.getCollidables(cc.createKey(type,
					getBundle().getOffset(getBasePipingType(), dir), dir,
					renderStub(dir)), this);
		}
		return null;
	}

}