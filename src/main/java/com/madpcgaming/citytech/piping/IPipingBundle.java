package com.madpcgaming.citytech.piping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.Offset;
import com.madpcgaming.citytech.util.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IPipingBundle
{
	TileEntity getEntity();
	
	//Pipings
	
	boolean hasType(Class<? extends IPiping> type);
	
	<T extends IPiping> T getPiping(Class<T> type);
	
	void addPiping(IPiping piping);
	
	void removePiping(IPiping piping);
	
	Collection<IPiping> getPiping();
	
	Offset getOffset(Class<? extends IPiping> type, ForgeDirection dir);
	
	//Connections
	
	Set<ForgeDirection> getConnections(Class<? extends IPiping> type);
	
	boolean containsConnection(Class<? extends IPiping> type, ForgeDirection west);
	
	Set<ForgeDirection> getAllConnections();
	
	boolean cotainsConnection(ForgeDirection dir);
	
	//Geomerty
	
	List<CollidableComponent> getCollidableComponents();
	
	List<CollidableComponent> getConnectors();
	
	// Events
	
	void onNeighborBlockChange(Block block);
	
	void onBlockRemoved();
	
	void dirty();
	
	//Facade
	
	enum FacadeRenderState
	{
		NONE,
		FULL,
		WIRE_FRAME
	}
	
	@SideOnly(Side.CLIENT)
	void setFacadeRenderAs(FacadeRenderState state);
	
	int getLightOpacity();
	
	void setLightOpacity(int opacity);
	
	boolean hasFacade();
	
	void setFacadeId(int block);
	
	void setFacadeId(int block, boolean triggerUpdate);
	
	int getFacadeID();
	
	void setFacadeMetadata(int meta);
	
	int getFacadeMetadata();
	
	BlockCoord getLocation();
}
