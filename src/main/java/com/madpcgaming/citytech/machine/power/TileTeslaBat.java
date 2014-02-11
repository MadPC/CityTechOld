package com.madpcgaming.citytech.machine.power;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thermalexpansion.api.item.IChargeableItem;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyContainerItem;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.power.IPowerPiping;
import com.madpcgaming.citytech.power.BasicTeslaBat;
import com.madpcgaming.citytech.power.IInternalPowerReceptor;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.power.PowerHandlerUtil;
import com.madpcgaming.citytech.util.BlockCoord;
import com.madpcgaming.citytech.util.Util;
import com.madpcgaming.citytech.vecmath.VecmathUtil;

public class TileTeslaBat extends TileEntity implements IInternalPowerReceptor, IInventory
{

  static enum FaceConnectionMode 
  {
    INPUT,
    OUTPUT,
    LOCKED,
    NONE
  }

  static final BasicTeslaBat BASE_BAT = new BasicTeslaBat(100, 500000);

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
    maxStoredEnergy = BASE_BAT.getMaxEnergyStored();
    maxIO = BASE_BAT.getMaxEnergyExtracted();
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
    if(curMode == FaceConnectionMode.LOCKED) {
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
    return null;
  }

  public FaceConnectionMode getFaceModeForFace(ForgeDirection face) 
  {
    if(faceModes == null) 
    {
      return FaceConnectionMode.NONE;
    }
    FaceConnectionMode res = faceModes.get(face);
    if(res == null) 
    {
      return FaceConnectionMode.NONE;
    }
    return res;
  }

  @Override
  public void updateEntity()
  {
    if(worldObj == null)
    {
    	return;
    }
    if(worldObj.isRemote)
    {
      if(render) 
      {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        render = false;
      }
      return;
    }

    if(multiblockDirty)
    {
      formMultiblock();
      multiblockDirty = false;
    }

    if(!isContoller()) 
    {
      return;
    }
    ;
    boolean requiresClientSync = false;
    requiresClientSync = chargeItems();

    boolean hasSignal = isRecievingRedstoneSignal();
    if(inputControlMode == RedstoneControlMode.IGNORE) 
    {
      inputEnabled = true;
    } else if(inputControlMode == RedstoneControlMode.NEVER) 
    {
      inputEnabled = false;
    } else {
      inputEnabled = (inputControlMode == RedstoneControlMode.ON && hasSignal) || (inputControlMode == RedstoneControlMode.OFF && !hasSignal);
    }
    if(outputControlMode == RedstoneControlMode.IGNORE) 
    {
      outputEnabled = true;
    } else if(outputControlMode == RedstoneControlMode.NEVER) 
    {
      outputEnabled = false;
    } else {
      outputEnabled = (outputControlMode == RedstoneControlMode.ON && hasSignal) || (outputControlMode == RedstoneControlMode.OFF && !hasSignal);
    }

    if(outputEnabled) 
    {
      transmitEnergy();
    }
    if(powerHandler != null && powerHandler.getEnergyStored() > 0) 
    {
      storedEnergy += powerHandler.getEnergyStored();
      powerHandler.setEnergy(0);
    }

    requiresClientSync |= lastSyncPowerStored != storedEnergy && worldObj.getTotalWorldTime() % 10 == 0;

    if(requiresClientSync) 
    {
      lastSyncPowerStored = storedEnergy;
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
      markDirty();
    }

  }

  public List<GaugeBounds> getGaugeBounds() 
  {
    if(gaugeBounds == null) {
      gaugeBounds = GaugeBounds.calculateGaugeBounds(new BlockCoord(this), multiblock);
    }
    return gaugeBounds;
  }

  private boolean chargeItems() 
  {
    boolean chargedItem = false;
    float available = Math.min(maxIO, storedEnergy);
    for (ItemStack item : inventory) 
    {
      if(item != null && available > 0) 
      {
        float used = 0;
        if(item.getItem() instanceof IEnergyContainerItem) 
        {
          IEnergyContainerItem chargable = (IEnergyContainerItem) item.getItem();

          float max = chargable.getMaxEnergyStored(item);
          float cur = chargable.getEnergyStored(item);
          float canUse = Math.min(available * 10, max - cur);
          if(cur < max) {
            used = chargable.receiveEnergy(item, (int) canUse, false) / 10;
            used = (chargable.getEnergyStored(item) - cur) / 10;
          }

        } else if(item.getItem() instanceof IChargeableItem) 
        {
          IChargeableItem chargable = (IChargeableItem) item.getItem();
          float max = chargable.getMaxEnergyStored(item);
          float cur = chargable.getEnergyStored(item);
          float canUse = Math.min(available, max - cur);

          if(cur < max) 
          {
            if(chargable.getClass().getName().startsWith("appeng") || "appeng.common.base.AppEngMultiChargeable".equals(chargable.getClass().getName())) {
              // 256 max limit
              canUse = Math.min(canUse, 256);
              NBTTagCompound tc = item.getTagCompound();
              if(tc == null) 
              {
                item.setTagCompound(tc = new NBTTagCompound());
              }
              double newVal = (cur + canUse) * 5.0;
              tc.setDouble("powerLevel", newVal);
              used = canUse;

            } else {
              used = chargable.receiveEnergy(item, canUse, true);
            }
          }
        }
        if(used > 0) 
        {
          storedEnergy = storedEnergy - used;
          chargedItem = true;
          available -= used;
        }
      }
    }
    return chargedItem;
  }

  public boolean isOutputEnabled() 
  {
    return getController().outputEnabled;
  }

  public boolean isOutputEnabled(ForgeDirection direction) 
  {
    FaceConnectionMode mode = getFaceModeForFace(direction);
    return mode == FaceConnectionMode.OUTPUT || mode == FaceConnectionMode.NONE && isOutputEnabled();
  }

  public boolean isInputEnabled() 
  {
    return getController().inputEnabled;
  }

  public boolean isInputEnabled(ForgeDirection direction) 
  {
    FaceConnectionMode mode = getFaceModeForFace(direction);
    return mode == FaceConnectionMode.INPUT || mode == FaceConnectionMode.NONE && isInputEnabled();
  }

  private boolean transmitEnergy()
  {

    if(storedEnergy <= 0) 
    {
      return false;
    }
    float canTransmit = Math.min(storedEnergy, maxOutput);
    float transmitted = 0;

    updateMasterReceptors();

    if(!masterReceptors.isEmpty() && !receptorIterator.hasNext()) 
    {
      receptorIterator = masterReceptors.listIterator();
    }

    int appliedCount = 0;
    int numReceptors = masterReceptors.size();
    while (receptorIterator.hasNext() && canTransmit > 0 && appliedCount < numReceptors) {

      Receptor receptor = receptorIterator.next();
      IPowerInterface powerInterface = receptor.receptor;
      FaceConnectionMode mode = receptor.mode;
      if(powerInterface != null
          && mode != FaceConnectionMode.INPUT && mode != FaceConnectionMode.LOCKED
          && powerInterface.getMinEnergyReceived(receptor.fromDir.getOpposite()) <= canTransmit) {
        float used;
        if(receptor.receptor.getDelegate() instanceof IPipingBundle) {
          IPipingBundle bundle = (IPipingBundle) receptor.receptor.getDelegate();
          IPowerPiping piping = bundle.getPiping(IPowerPiping.class);
          if(piping != null && piping.getConnectionMode(receptor.fromDir.getOpposite()) == ConnectionMode.INPUT) {
            used = powerInterface.receiveEnergy(receptor.fromDir.getOpposite(), canTransmit);
          } else {
            used = 0;
          }
        } else {
          used = powerInterface.receiveEnergy(receptor.fromDir.getOpposite(), canTransmit);
        }

        transmitted += used;
        canTransmit -= used;
      }

      if(canTransmit <= 0) {
        break;
      }

      if(!masterReceptors.isEmpty() && !receptorIterator.hasNext()) {
        receptorIterator = masterReceptors.listIterator();
      }
      appliedCount++;
    }
    storedEnergy = storedEnergy - transmitted;

    return transmitted > 0;

  }

  private void updateMasterReceptors() 
  {
    if(!masterReceptorsDirty && masterReceptors != null) 
    {
      return;
    }

    if(masterReceptors == null) 
    {
      masterReceptors = new ArrayList<Receptor>();
    }
    masterReceptors.clear();

    if(multiblock == null)
    {
      updateReceptors();
      if(localReceptors != null) 
      {
        masterReceptors.addAll(localReceptors);
      }
    } else {
      for (BlockCoord bc : multiblock)
      {
        TileEntity te = worldObj.getTileEntity(bc.x, bc.y, bc.z);
        if(te instanceof TileTeslaBat) 
        {
          TileTeslaBat tb = ((TileTeslaBat) te);
          tb.updateReceptors();
          if(tb.localReceptors != null) 
          {
            masterReceptors.addAll(tb.localReceptors);
          }
        }
      }
    }

    receptorIterator = masterReceptors.listIterator();
    masterReceptorsDirty = false;
  }

  private void updateReceptors()
  {

    if(!receptorsDirty) 
    {
      return;
    }
    if(localReceptors != null) 
    {
      localReceptors.clear();
    }

    BlockCoord bc = new BlockCoord(this);
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) 
    {
      FaceConnectionMode mode = getFaceModeForFace(dir);
      if(mode != FaceConnectionMode.LOCKED) 
      {
        BlockCoord checkLoc = bc.getLocation(dir);
        TileEntity te = worldObj.getTileEntity(checkLoc.x, checkLoc.y, checkLoc.z);
        if(!(te instanceof TileTeslaBat)) 
        {
          IPowerInterface ph = PowerHandlerUtil.create(te);
          if(ph != null && ph.canPipingConnect(dir)) 
          {
            if(localReceptors == null) 
            {
              localReceptors = new ArrayList<Receptor>();
            }
            Receptor r = new Receptor(ph, dir, mode);
            localReceptors.add(r);
            if(mode == FaceConnectionMode.NONE && !(ph.getDelegate() instanceof IInternalPowerReceptor)) 
            {
              setFaceMode(dir, FaceConnectionMode.INPUT, false);
              worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
              render = true;
            }
          }
        }
      }
    }

    receptorsDirty = false;
  }

  // ------------ Multiblock overrides

  public int getEnergyStoredScaled(int scale)
  {
    return getController().doGetEnergyStoredScaled(scale);
  }

  public int getMaxInput()
  {
    return maxInput;
  }

  public void setMaxInput(int maxInput)
  {
    getController().doSetMaxInput(maxInput);
  }

  public int getMaxOutput() 
  {
    return maxOutput;
  }

  public void setMaxOutput(int maxOutput) 
  {
    getController().doSetMaxOutput(maxOutput);
  }

  public float getEnergyStored() 
  {
    return getController().doGetEnergyStored();
  }

  public float getEnergyStoredRatio()
  {
    return getController().doGetEnergyStoredRatio();
  }

  public int getMaxEnergyStored() 
  {
    return getController().doGetMaxEnergyStored();
  }

  public int getMaxIO() 
  {
    return getController().doGetMaxIO();
  }

  @Override
  public PowerHandler getPowerHandler() 
  {
    return getController().doGetPowerHandler();
  }

  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side)
  {

    FaceConnectionMode mode = getFaceModeForFace(side);
    if(mode == FaceConnectionMode.LOCKED || mode == FaceConnectionMode.OUTPUT)
    {
      return getDisabledPowerHandler().getPowerReceiver();
    }
    return getPowerHandler().getPowerReceiver();
  }

  // RF Power

  @Override
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) 
  {
    return 0;
  }

  @Override
  public boolean canInterface(ForgeDirection from) 
  {
    return getFaceModeForFace(from) != FaceConnectionMode.LOCKED;
  }

  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) 
  {
    FaceConnectionMode mode = getFaceModeForFace(from);
    if(mode == FaceConnectionMode.LOCKED || mode == FaceConnectionMode.OUTPUT) 
    {
      return 0;
    }
    return getController().doReceiveEnergy(from, maxReceive, simulate);
  }

  @Override
  public int getEnergyStored(ForgeDirection from) 
  {
    return getController().doGetEnergyStored(from);
  }

  @Override
  public int getMaxEnergyStored(ForgeDirection from) 
  {
    return getController().doGetMaxEnergyStored();
  }

  public int doReceiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) 
  {
    float freeSpace = maxStoredEnergy - storedEnergy;
    int result = (int) Math.min(maxReceive / 10, freeSpace);
    if(!simulate)
    {
      storedEnergy += result;
    }
    return result * 10;
  }

  public int doGetEnergyStored(ForgeDirection from) 
  {
    return (int) (storedEnergy * 10);
  }

  public int doGetMaxEnergyStored(ForgeDirection from)
  {
    return maxStoredEnergy * 10;
  }


  public void addEnergy(float add) 
  {
    getController().doAddEnergy(add);
  }

  private boolean isRecievingRedstoneSignal() 
  {
    if(!redstoneStateDirty) 
    {
      return isRecievingRedstoneSignal;
    }

    isRecievingRedstoneSignal = false;
    redstoneStateDirty = false;

    if(!isMultiblock()) {
      isRecievingRedstoneSignal = worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord) > 0;
    } else {
      for (BlockCoord bc : multiblock) {
        if(worldObj.getStrongestIndirectPower(bc.x, bc.y, bc.z) > 0) {
          isRecievingRedstoneSignal = true;
          break;
        }
      }
    }

    return isRecievingRedstoneSignal;

  }

  public RedstoneControlMode getInputControlMode() 
  {
    return inputControlMode;
  }

  public void setInputControlMode(RedstoneControlMode inputControlMode) 
  {
    if(!isMultiblock()) 
    {
      this.inputControlMode = inputControlMode;
    } else {
      for (BlockCoord bc : multiblock) 
      {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) 
        {
          tb.inputControlMode = inputControlMode;
        }
      }
    }
  }

  public RedstoneControlMode getOutputControlMode() 
  {
    return outputControlMode;
  }

  public void setOutputControlMode(RedstoneControlMode outputControlMode) 
  {
    if(!isMultiblock()) 
    {
      this.outputControlMode = outputControlMode;
    } else {
      for (BlockCoord bc : multiblock)
      {
        TileTeslaBat cp = getTeslaBat(bc);
        if(cp != null) {
          cp.outputControlMode = outputControlMode;
        }
      }
    }
  }

  int doGetMaxIO()
  {
    return maxIO;
  }

  int doGetMaxEnergyStored() 
  {
    return maxStoredEnergy;
  }

  PowerHandler doGetPowerHandler() 
  {
    if(inputEnabled) 
    {
      if(powerHandler == null) 
      {
        powerHandler = PowerHandlerUtil.createHandler(new BasicTeslaBat(maxInput, maxInput, maxOutput), this, Type.STORAGE);
      }
      return powerHandler;
    }
    return getDisabledPowerHandler();
  }

  private PowerHandler getDisabledPowerHandler() 
  {
    if(disabledPowerHandler == null)
    {
      disabledPowerHandler = PowerHandlerUtil.createHandler(new BasicTeslaBat(0, 0), this, Type.STORAGE);
    }
    return disabledPowerHandler;
  }

  int doGetEnergyStoredScaled(int scale) 
  {
    return VecmathUtil.clamp(Math.round(scale * (storedEnergy / maxStoredEnergy)), 0, scale);
  }

  float doGetEnergyStored() 
  {
    return storedEnergy;
  }

  float doGetEnergyStoredRatio() 
  {
    return storedEnergy / maxStoredEnergy;
  }

  void doAddEnergy(float add)
  {
    storedEnergy = Math.min(maxStoredEnergy, storedEnergy + add);
  }

  void doSetMaxInput(int in) 
  {
    maxInput = Math.min(in, maxIO);
    maxInput = Math.max(0, maxInput);
    if(isMultiblock()) {
      for (BlockCoord bc : multiblock) {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) {
          tb.maxInput = maxInput;
        }
      }
    }
    updatePowerHandler();
  }

  void doSetMaxOutput(int out)
  {
    maxOutput = Math.min(out, maxIO);
    maxOutput = Math.max(0, maxOutput);
    if(isMultiblock()) 
    {
      for (BlockCoord bc : multiblock) 
      {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) {
          tb.maxOutput = maxOutput;
        }
      }
    }
    updatePowerHandler();
  }


  @Override
  public void doWork(PowerHandler workProvider) 
  {
  }

  @Override
  public World getWorld() 
  {
    return worldObj;
  }

  @Override
  public void applyPerdition() 
  {
  }

  private void updatePowerHandler() 
  {
    if(storedEnergy > maxStoredEnergy) 
    {
      storedEnergy = maxStoredEnergy;
    }
    powerHandler = null;
  }

  public void onBlockAdded() 
  {
    multiblockDirty = true;
  }

  public void onNeighborBlockChange(Block blockId) 
  {
    if(blockId != ModBlocks.blockCapacitorBank) 
    {
      receptorsDirty = true;
      getController().masterReceptorsDirty = true;
    }
    redstoneStateDirty = true;
  }

  public void onBreakBlock() 
  {
    TileTeslaBat controller = getController();
    controller.clearCurrentMultiblock();
  }

  private void clearCurrentMultiblock()
  {
    if(multiblock == null)
    {
      return;
    }
    for (BlockCoord bc : multiblock) 
    {
      TileTeslaBat res = getTeslaBat(bc);
      if(res != null) {
        res.setMultiblock(null);

      }
    }
    multiblock = null;
    redstoneStateDirty = true;
  }

  private void formMultiblock() 
  {
    List<TileTeslaBat> blocks = new ArrayList<TileTeslaBat>();
    blocks.add(this);
    findNighbouringBanks(this, blocks);

    if(blocks.size() < 2) 
    {
      return;
    }
    for (TileTeslaBat tb : blocks)
    {
      tb.clearCurrentMultiblock();
    }

    BlockCoord[] mb = new BlockCoord[blocks.size()];
    for (int i = 0; i < blocks.size(); i++) 
    {
      mb[i] = new BlockCoord(blocks.get(i));
    }

    for (TileTeslaBat tb : blocks) 
    {
      tb.setMultiblock(mb);
    }
  }

  private void findNighbouringBanks(TileTeslaBat tileTeslaBat, List<TileTeslaBat> blocks) 
  {
    BlockCoord bc = new BlockCoord(tileTeslaBat);
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) 
    {
      TileTeslaBat tb = getTeslaBat(bc.getLocation(dir));
      if(tb != null && !blocks.contains(tb)) 
      {
        blocks.add(tb);
        findNighbouringBanks(tb, blocks);
      }
    }
  }

  private void setMultiblock(BlockCoord[] mb) 
  {

    if(multiblock != null && isMaster()) 
    {
      float powerPerBlock = storedEnergy / multiblock.length;
      for (BlockCoord bc : multiblock) 
      {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) {
          tb.maxStoredEnergy = BASE_BAT.getMaxEnergyStored();
          tb.maxIO = BASE_BAT.getMaxEnergyExtracted();
          tb.maxInput = tb.maxIO;
          tb.maxOutput = tb.maxIO;
          tb.storedEnergy = powerPerBlock;
          tb.updatePowerHandler();
          tb.multiblockDirty = true;
        }
      }

    }
    multiblock = mb;
    if(isMaster()) {

      List<ItemStack> invItems = new ArrayList<ItemStack>();

      float totalStored = 0;
      int totalCap = multiblock.length * BASE_BAT.getMaxEnergyStored();
      int totalIO = multiblock.length * BASE_BAT.getMaxEnergyExtracted();
      for (BlockCoord bc : multiblock) {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) {
          totalStored += tb.storedEnergy;
        }
        ItemStack[] inv = tb.inventory;
        for (int i = 0; i < inv.length; i++) {
          if(inv[i] != null) {
            invItems.add(inv[i]);
            inv[i] = null;
          }
        }
        tb.multiblockDirty = false;
      }
      storedEnergy = totalStored;
      maxStoredEnergy = totalCap;
      maxIO = totalIO;
      maxInput = maxIO;
      maxOutput = maxIO;
      for (BlockCoord bc : multiblock) {
        TileTeslaBat tb = getTeslaBat(bc);
        if(tb != null) {
          tb.maxIO = totalIO;
          tb.maxInput = maxIO;
          tb.maxOutput = maxIO;
        }
      }

      if(invItems.size() > inventory.length) {
        for (int i = inventory.length; i < invItems.size(); i++) {
          Util.dropItems(worldObj, invItems.get(i), xCoord, yCoord, zCoord, true);
        }
      }
      for (int i = 0; i < inventory.length && i < invItems.size(); i++) {
        inventory[i] = invItems.get(i);
      }

      updatePowerHandler();
    }
    receptorsDirty = true;
    getController().masterReceptorsDirty = true;
    redstoneStateDirty = true;

    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isMultiblock() ? 1 : 0, 2);
  }

  TileTeslaBat getController() 
  {
    if(isMaster() || !isMultiblock()) 
    {
      return this;
    }
    TileTeslaBat res = getTeslaBat(multiblock[0]);
    return res != null ? res : this;
  }

  boolean isContoller() 
  {
    return multiblock == null ? true : isMaster();
  }

  boolean isMaster() 
  {
    if(multiblock != null) 
    {
      return multiblock[0].equals(xCoord, yCoord, zCoord);
    }
    return false;
  }

  public boolean isMultiblock() 
  {
    return multiblock != null;
  }

  private boolean isCurrentMultiblockValid() 
  {
    if(multiblock == null) 
    {
      return false;
    }
    for (BlockCoord bc : multiblock) 
    {
      TileTeslaBat res = getTeslaBat(bc);
      if(res == null || !res.isMultiblock()) 
      {
        return false;
      }
    }
    return true;
  }

  private TileTeslaBat getTeslaBat(BlockCoord bc) 
  {
    return getTeslaBat(bc.x, bc.y, bc.z);
  }

  private TileTeslaBat getTeslaBat(int x, int y, int z) 
  {
    if(worldObj == null) 
    {
      return null;
    }
    TileEntity te = worldObj.getTileEntity(x, y, z);
    if(te instanceof TileTeslaBat) 
    {
      return (TileTeslaBat) te;
    }
    return null;
  }


  @Override
  public int getSizeInventory() 
  {
    return getController().doGetSizeInventory();
  }

  @Override
  public ItemStack getStackInSlot(int i) 
  {
    return getController().doGetStackInSlot(i);
  }

  @Override
  public ItemStack decrStackSize(int i, int j) 
  {
    return getController().doDecrStackSize(i, j);
  }

  @Override
  public void setInventorySlotContents(int i, ItemStack itemstack) 
  {
    getController().doSetInventorySlotContents(i, itemstack);
  }

  public ItemStack doGetStackInSlot(int i) 
  {
    if(i < 0 || i >= inventory.length) 
    {
      return null;
    }
    return inventory[i];
  }

  public int doGetSizeInventory() 
  {
    return inventory.length;
  }

  public ItemStack doDecrStackSize(int fromSlot, int amount) 
  {
    if(fromSlot < 0 || fromSlot >= inventory.length) 
    {
      return null;
    }
    ItemStack item = inventory[fromSlot];
    if(item == null) 
    {
      return null;
    }
    if(item.stackSize <= amount) 
    {
      ItemStack result = item.copy();
      inventory[fromSlot] = null;
      return result;
    }
    item.stackSize -= amount;
    return item.copy();
  }

  public void doSetInventorySlotContents(int i, ItemStack itemstack)
  {
    if(i < 0 || i >= inventory.length) 
    {
      return;
    }
    inventory[i] = itemstack;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int i) 
  {
    return null;
  }

  @Override
  public String getInventoryName() 
  {
    return null;
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  @Override
  public int getInventoryStackLimit() 
  {
    return 1;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer entityplayer) 
  {
    return true;
  }

  @Override
  public void openInventory() 
  {
  }

  @Override
  public void closeInventory()
  {
  }

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack) 
  {
    if(itemstack == null) 
    {
      return false;
    }
    return itemstack.getItem() instanceof IChargeableItem || itemstack.getItem() instanceof IEnergyContainerItem;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtRoot) 
  {
    super.readFromNBT(nbtRoot);

    float oldEnergy = storedEnergy;
    storedEnergy = nbtRoot.getFloat("storedEnergy");
    maxStoredEnergy = nbtRoot.getInteger("maxStoredEnergy");

    float newEnergy = storedEnergy;
    if(maxStoredEnergy != 0 && Math.abs(oldEnergy - newEnergy) / maxStoredEnergy > 0.05 || nbtRoot.hasKey("render")) 
    {
      render = true;
    }
    maxIO = nbtRoot.getInteger("maxIO");
    if(nbtRoot.hasKey("maxInput")) 
    {
      maxInput = nbtRoot.getInteger("maxInput");
      maxOutput = nbtRoot.getInteger("maxOutput");
    } else {
      maxInput = maxIO;
      maxOutput = maxIO;
    }

    inputControlMode = RedstoneControlMode.values()[nbtRoot.getShort("inputControlMode")];
    outputControlMode = RedstoneControlMode.values()[nbtRoot.getShort("outputControlMode")];

    updatePowerHandler();

    boolean wasMulti = isMultiblock();
    if(nbtRoot.getBoolean("isMultiblock")) 
    {
      int[] coords = nbtRoot.getIntArray("multiblock");
      multiblock = new BlockCoord[coords.length / 3];
      int c = 0;
      for (int i = 0; i < multiblock.length; i++) 
      {
        multiblock[i] = new BlockCoord(coords[c++], coords[c++], coords[c++]);
      }

    } else {
      multiblock = null;
    }

    for (int i = 0; i < inventory.length; i++) 
    {
      inventory[i] = null;
    }

    						//TODO: Fix error here.
    NBTTagList itemList = nbtRoot.getTagList("Items");
    for (int i = 0; i < itemList.tagCount(); i++) 
    {
    	  									//TODO: Fix error here.
      NBTTagCompound itemStack = (NBTTagCompound) itemList.tagAt(i);
      byte slot = itemStack.getByte("Slot");
      if(slot >= 0 && slot < inventory.length) 
      {
        inventory[slot] = ItemStack.loadItemStackFromNBT(itemStack);
      }
    }

    if(nbtRoot.hasKey("hasFaces")) {
      for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
        if(nbtRoot.hasKey("face" + dir.ordinal())) {
          setFaceMode(dir, FaceConnectionMode.values()[nbtRoot.getShort("face" + dir.ordinal())], false);
        }
      }
    }

    gaugeBounds = null;
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtRoot) 
  {
    super.writeToNBT(nbtRoot);

    nbtRoot.setFloat("storedEnergy", storedEnergy);
    nbtRoot.setInteger("maxStoredEnergy", maxStoredEnergy);
    nbtRoot.setInteger("maxIO", maxIO);
    nbtRoot.setInteger("maxInput", maxInput);
    nbtRoot.setInteger("maxOutput", maxOutput);
    nbtRoot.setShort("inputControlMode", (short) inputControlMode.ordinal());
    nbtRoot.setShort("outputControlMode", (short) outputControlMode.ordinal());

    nbtRoot.setBoolean("isMultiblock", isMultiblock());
    if(isMultiblock()) 
    {
      int[] vals = new int[multiblock.length * 3];
      int i = 0;
      for (BlockCoord bc : multiblock) 
      {
        vals[i++] = bc.x;
        vals[i++] = bc.y;
        vals[i++] = bc.z;
      }
      nbtRoot.setIntArray("multiblock", vals);
    }

    // write inventory list
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inventory.length; i++) 
    {
      if(inventory[i] != null) 
      {
        NBTTagCompound itemStackNBT = new NBTTagCompound();
        itemStackNBT.setByte("Slot", (byte) i);
        inventory[i].writeToNBT(itemStackNBT);
        itemList.appendTag(itemStackNBT);
      }
    }
    nbtRoot.setTag("Items", itemList);

    //face modes
    if(faceModes != null) 
    {
      nbtRoot.setByte("hasFaces", (byte) 1);
      for (Entry<ForgeDirection, FaceConnectionMode> e : faceModes.entrySet()) 
      {
        nbtRoot.setShort("face" + e.getKey().ordinal(), (short) e.getValue().ordinal());
      }
    }

    if(render) 
    {
      nbtRoot.setBoolean("render", true);
      render = false;
    }
  }

  static class Receptor 
  {
    IPowerInterface receptor;
    ForgeDirection fromDir;
    FaceConnectionMode mode;

    private Receptor(IPowerInterface rec, ForgeDirection fromDir, FaceConnectionMode mode) 
    {
      this.receptor = rec;
      this.fromDir = fromDir;
      this.mode = mode;
    }

    @Override
    public String toString() 
    {
      return "Receptor [receptor=" + receptor + ", fromDir=" + fromDir + ", mode=" + mode + "]";
    }

  }

}