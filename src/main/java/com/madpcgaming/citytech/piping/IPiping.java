package com.madpcgaming.citytech.piping;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.util.BlockCoord;

public interface IPiping
{
	//Basic
	
	Class<? extends IPiping> getBasePipingType();
	
	ItemStack createItem();
	
	int getLightValue();
	
	boolean isActive();
	
	void setActive(boolean active);
	
	void writeToNBT(NBTTagCompound pipingBody);
	
	void readFromNBT(NBTTagCompound pipingBody);
	
	//Container
	
	void setBundle(IPipingBundle tilePipingBundle);
	
	IPipingBundle getBundle();
	
	void onAddedToBundle();
	
	void onRemovedFromBundle();
	
	BlockCoord getLocation();
	
	//Connections
	
	boolean hasConnections();
	
	boolean hasExternalConnections();
	
	boolean hasPipingConnections();
	
	//Conduit Connections
	
	boolean canConnectToPiping(ForgeDirection direction, IPiping piping);
	
	Set<ForgeDirection> getPipingConnections();
	
	boolean containsPipingConnection(ForgeDirection dir);
	
	void pipingConnectionAdded(ForgeDirection fromDirection);
	
	void pipingConnectionRemoved(ForgeDirection fromDirection);
	
	AbstractPipingNetwork<?, ?> getNetwork();
	
	boolean setNetwork(AbstractPipingNetwork<?, ?> network);
	
	//External Connections
	
	boolean canConnectToExternal(ForgeDirection direction, boolean ignoreConnectionMode);
	
	Set<ForgeDirection> getExternalConnections();
	
	boolean containsExternalConnection(ForgeDirection dir);
	
	void externalConnectionAdded(ForgeDirection fromDirection);
	
	void externalConnectionRemoved(ForgeDirection fromDirection);
	
	boolean isConnectedTo(ForgeDirection dir);
}
