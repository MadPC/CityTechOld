package com.madpcgaming.citytech.machine.power;

import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.PowerHandler;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.power.BasicTeslaBat;
import com.madpcgaming.citytech.power.IInternalPowerReceptor;
import com.madpcgaming.citytech.power.PowerHandlerUtil;
import com.madpcgaming.citytech.util.BlockCoord;

public class TileTeslaBat extends TileEntity implements IInternalPowerReceptor,
		IInventory
{
	static enum FaceConnectionMode
	{
		INPUT,
		OUTPUT,
		LOCKED,
		NONE
	}
	
	static final BasicTeslaBat BASE_TESLA = new BasicTeslaBat(100, 500000);
	
	BlockCoord[] multiblock = null;
	private PowerHandler powerHandler;
	private PowerHandler disabledPowerHandler;
	private float lastSyncPowerStored;
	private float storedEnergy;
	private int maxStoredEnergy;
	private int maxIO;
	private int maxInput;
	private int maxOutput;
	private boolean multiblockDirty = false;
	private RedstoneControlMode inputControlMode;
	private RedstoneControlMode outputControlMode;
	private boolean outputEnabled;
	private boolean inputEnabled;
	private boolean isRecievingRedstoneSignal;
	private boolean redstoneStateDirty = true;
	private List<Receptor> masterReceptors;
	private ListIterator<Receptor> receptorIterator;
	private List<Receptor> localReceptors;
	private boolean receptorsDirty = true;
	private final ItemStack[] inventory;
	private List<GaugeBounds> gaugeBounds;
	private Map<ForgeDirection, FaceConnectionMode> faceModes;
	private boolean render = false;
	private boolean masterReceptorsDirty;
	
	public TileTeslaBat()
	{
		inventory = new ItemStack[4];
		storedEnergy = 0;
		inputControlMode = RedstoneControlMode.IGNORE;
		outputControlMode = RedstoneControlMode.IGNORE;
		maxStoredEnergy = BASE_TESLA.getMaxEnergyStored();
		maxIO = BASE_TESLA.getMaxEnergyExtracted();
		maxInput = maxIO;
		maxOutput = maxIO;
		updatePowerHandler();
	}
	
	public FaceConnectionMode toggleModeForFace(ForgeDirection faceHit)
	{
		Object rec = getReceptorForFace(faceHit);
		FaceConnectionMode curMode = getFaceModeForFace(faceHit);
		if(curMode == FaceConnectionMode.INPUT)
		{
			setFaceMode(faceHit, FaceConnectionMode.OUTPUT, true);
			return FaceConnectionMode.OUTPUT;
		}
		if(curMode == FaceConnectionMode.OUTPUT)
		{
			setFaceMode(faceHit, FaceConnectionMode.LOCKED, true);
			return FaceConnectionMode.LOCKED;
		}
		if(curMode == FaceConnectionMode.LOCKED)
		{
			if(rec == null || rec instanceof IInternalPowerReceptor)
			{
				setFaceMode(faceHit, FaceConnectionMode.NONE, true);
				return FaceConnectionMode.NONE;
			}
		}
		setFaceMode(faceHit, FaceConnectionMode.INPUT, true);
		return FaceConnectionMode.INPUT;
	}
	
	private void setFaceMode(ForgeDirection faceHit, FaceConnectionMode mode, boolean b)
	{
		if(mode == FaceConnectionMode.NONE && faceModes == null)
		{
			return;
		}
		if(faceModes == null)
		{
			faceModes = new EnumMap<ForgeDirection, TileTeslaBat.FaceConnectionMode>(ForgeDirection.class);
		}
		faceModes.put(faceHit, mode);
		if(b)
		{
			receptorsDirty = true;
			getController().masterReceptorsDirty = true;
		}
	}
	
	private Object getReceptorForFace(ForgeDirection faceHit)
	{
		BlockCoord checkLoc = new BlockCoord(this).getLocation(faceHit);
		TileEntity te = worldObj.getTileEntity(checkLoc.x, checkLoc.y, checkLoc.z);
		if(!(te instanceof TileTeslaBat))
		{
			return PowerHandlerUtil.create(te);
		}
	}
}
