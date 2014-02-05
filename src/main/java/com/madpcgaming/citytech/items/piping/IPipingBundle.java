package com.madpcgaming.citytech.items.piping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import com.madpcgaming.citytech.items.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.items.piping.geom.Offset;
import com.madpcgaming.citytech.lib.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IPipingBundle extends IFluidHandler, IItemConduit
{
	TileEntity getEntity();

	// conduits

	boolean hasType(Class<? extends IPiping> type);

	<T extends IPiping> T getPiping(Class<T> type);

	void addConduit(IPiping piping);

	void removeConduit(IPiping piping);

	Collection<IPiping> getPiping();

	Offset getOffset(Class<? extends IPiping> type, ForgeDirection dir);

	// connections

	Set<ForgeDirection> getConnections(Class<? extends IPiping> type);

	boolean containsConnection(Class<? extends IPiping> type, ForgeDirection west);

	Set<ForgeDirection> getAllConnections();

	boolean containsConnection(ForgeDirection dir);

	// geometry

	List<CollidableComponent> getCollidableComponents();

	List<CollidableComponent> getConnectors();

	// events

	void onNeighborBlockChange(int blockId);

	void onBlockRemoved();

	void dirty();

	// Facade

	enum FacadeRenderState
	{
		NONE, FULL, WIRE_FRAME
	}

	@SideOnly(Side.CLIENT)
	FacadeRenderState getFacadeRenderedAs();

	@SideOnly(Side.CLIENT)
	void setFacadeRenderAs(FacadeRenderState state);

	int getLightOpacity();

	void setLightOpacity(int opacity);

	boolean hasFacade();

	void setFacadeId(int blockID);

	void setFacadeId(int blockID, boolean triggerUpdate);

	int getFacadeId();

	void setFacadeMetadata(int meta);

	int getFacadeMetadata();

	BlockCoord getLocation();

}
