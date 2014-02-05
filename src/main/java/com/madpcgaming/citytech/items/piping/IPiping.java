package com.madpcgaming.citytech.items.piping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.items.piping.geom.CollidableCache.CacheKey;
import com.madpcgaming.citytech.items.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.lib.BlockCoord;

public interface IPiping
{
	
	//Basics
	Class<? extends IPiping> getBasePipingType();
	
	ItemStack createItem();
	
	int getLightValue();
	
	boolean isActive();
	
	void setActive(boolean active);
	
	void writeToNBT(NBTTagCompound pipingBody);
	
	void readFromNBT(NBTTagCompound pipingBody);
	
	//Container things
	void setBundle(IPipingBundle tilePipingBundle);
	
	IPipingBundle getBundle();
	
	void onAddedToBundle();
	
	void onRemovedFromBundle();
	
	BlockCoord getLocation();
	
	//Connections Stuff
	boolean hasConnections();
	
	boolean hasExternalConnections();
	
	boolean hasPipingConnections();
	
	//Piping Conenctions
	boolean canConnectToPiping(ForgeDirection direction, IPiping piping);
	
	Set<ForgeDirection> getPipingConnections();
	
	boolean containsPipingConnections();
	
	void pipingConnectionAdded(ForgeDirection fromDirection);
	
	void pipingConnectionRemoved(ForgeDirection fromDirection);
	
	AbstractPipingNetwork<?> getNetwork();
	
	boolean setNetwork(AbstractPipingNetwork<?> network);
	
	//External Stuff
	boolean canConnectToExternal(ForgeDirection direction, boolean ignoreConnectionMode);
	
	Set<ForgeDirection> getExternalConnections();
	
	boolean containsExternalConnections(ForgeDirection dir);
	
	void externalConnectionAdded(ForgeDirection fromDirection);
	
	void externalConnectionRemoved(ForgeDirection fromDirection);
	
	boolean isConnectedTo(ForgeDirection dir);
	
	ConnectionMode getConnectionMode(ForgeDirection dir);
	
	void setConnectionMode(ForgeDirection dir, ConnectionMode mode);
	
	boolean hasConnectionMode(ConnectionMode mode);
	
	ConnectionMode getNextConnectionMode(ForgeDirection dir);
	
	ConnectionMode getPreviousConnectionMode(ForgeDirection dir);
	
	//render
	
	Icon getTextureForState(CollidableComponent component);
	
	Icon getTransmitionTextureForState(CollidableComponent component);
	
	float getTransmitionGeometryScale();
	
	float getSelfIlluminationForState(CollidableComponent component);
	
	//Geometry
	
	boolean haveCollidablesChangedSinceLastCall();
	
	Collection<CollidableComponent> getCollidableComponents();
	
	Collection<CollidableComponent> createCollidables(CacheKey key);
	
	Class<? extends IPiping> getCollidableType();
	
	//Events
	
	boolean onBlockActivated(EntityPlayer player, RaytraceResult res, List<RaytraceResult> all);
	
	void onChunkUnload(World worldobj);
	
	void updateEntity(World worldobj);
	
	boolean onNeighborBlockChange();
}
