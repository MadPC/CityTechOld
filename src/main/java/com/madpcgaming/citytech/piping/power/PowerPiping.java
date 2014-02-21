package com.madpcgaming.citytech.piping.power;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.piping.AbstractPipingNetwork;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.piping.RaytraceResult;
import com.madpcgaming.citytech.piping.geom.CollidableCache.CacheKey;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.PipingGeometryUtil;
import com.madpcgaming.citytech.power.BasicTeslaBat;
import com.madpcgaming.citytech.power.IPowerInterface;
import com.madpcgaming.citytech.power.ITeslaBat;
import com.madpcgaming.citytech.power.PowerHandlerUtil;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.util.DyeColor;
import com.madpcgaming.citytech.util.IconUtil;
import com.madpcgaming.citytech.vecmath.Vector3d;

public class PowerPiping extends AbstractPiping implements IPowerPiping {

  static final Map<String, IIcon> ICONS = new HashMap<String, IIcon>();

  static final ITeslaBat[] TESLA_BAT = new BasicTeslaBat[] {
      new BasicTeslaBat(500, 1500, 128),
      new BasicTeslaBat(512, 3000, 512),
      new BasicTeslaBat(2048, 5000, 2048)
  };

  static final String[] POSTFIX = new String[] {"", "Enhanced", "Advanced" };

  static ItemStack createItemStackForSubtype(int subtype) {
    ItemStack result = new ItemStack(ModBlocks.itemPowerPiping, 1, subtype);
    return result;
  }

  public static void initIcons() {
    IconUtil.addIconProvider(new IconUtil.IIconProvider() {

      @Override
      public void registerIcons(IIconRegister register) {
        for (String pf : POSTFIX) {
          ICONS.put(ICON_KEY + pf, register.registerIcon(ICON_KEY + pf));
          ICONS.put(ICON_KEY_INPUT + pf, register.registerIcon(ICON_KEY_INPUT + pf));
          ICONS.put(ICON_KEY_OUTPUT + pf, register.registerIcon(ICON_KEY_OUTPUT + pf));
          ICONS.put(ICON_CORE_KEY + pf, register.registerIcon(ICON_CORE_KEY + pf));
        }
        ICONS.put(ICON_TRANSMISSION_KEY, register.registerIcon(ICON_TRANSMISSION_KEY));
      }

      @Override
      public int getTextureType() {
        return 0;
      }

    });
  }

  public static final float WIDTH = 0.075f;
  public static final float HEIGHT = 0.075f;

  public static final Vector3d MIN = new Vector3d(0.5f - WIDTH, 0.5 - HEIGHT, 0.5 - WIDTH);
  public static final Vector3d MAX = new Vector3d(MIN.x + WIDTH, MIN.y + HEIGHT, MIN.z + WIDTH);

  public static final BoundingBox BOUNDS = new BoundingBox(MIN, MAX);

  protected PowerPipingNetwork network;

  private PowerHandler powerHandler;
  private PowerHandler noInputPH;

  private float energyStored;

  private int subtype;

  protected final EnumMap<ForgeDirection, RedstoneControlMode> rsModes = new EnumMap<ForgeDirection, RedstoneControlMode>(ForgeDirection.class);
  protected final EnumMap<ForgeDirection, DyeColor> rsColors = new EnumMap<ForgeDirection, DyeColor>(ForgeDirection.class);

  private final Map<ForgeDirection, Integer> externalRedstoneSignals = new HashMap<ForgeDirection, Integer>();

  private boolean redstoneStateDirty = true;

  public PowerPiping() {
  }

  public PowerPiping(int meta) {
    this.subtype = meta;
  }

  @Override
  public boolean onBlockActivated(EntityPlayer player, RaytraceResult res, List<RaytraceResult> all) {
    DyeColor col = DyeColor.getColorFromDye(player.getCurrentEquippedItem());
    if(col != null && res.component != null && isColorBandRendered(res.component.dir)) {
      setSignalColor(res.component.dir, col);
      return true;
    } else if(PipingUtil.isToolEquipped(player)) {
      if(!getBundle().getEntity().getWorldObj().isRemote) {
        if(res != null && res.component != null) {
          ForgeDirection connDir = res.component.dir;
          ForgeDirection faceHit = ForgeDirection.getOrientation(res.movingObjectPosition.sideHit);
          if(connDir == ForgeDirection.UNKNOWN || connDir == faceHit) {
            // Attempt to join networks
            return PipingUtil.joinPiping(this, faceHit);
          } else if(externalConnections.contains(connDir)) {
            setConnectionMode(connDir, getNextConnectionMode(connDir));
            return true;
          } else if(containsPipingConnection(connDir)) {
            PipingUtil.disconnectPiping(this, connDir);
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean isColorBandRendered(ForgeDirection dir) {
    return getConnectionMode(dir) != ConnectionMode.DISABLED && getRedstoneMode(dir) != RedstoneControlMode.IGNORE;
  }

  @Override
  public ITeslaBat getTeslaBat() {
    return TESLA_BAT[subtype];
  }

  private PowerHandler createPowerHandlerForType() {
    return PowerHandlerUtil.createHandler(TESLA_BAT[subtype], this, Type.PIPE);
  }

  @Override
  public void setRedstoneMode(RedstoneControlMode mode, ForgeDirection dir) {
    rsModes.put(dir, mode);
    setClientStateDirty();
  }

  @Override
  public RedstoneControlMode getRedstoneMode(ForgeDirection dir) {
    RedstoneControlMode res = rsModes.get(dir);
    if(res == null) {
      res = RedstoneControlMode.IGNORE;
    }
    return res;
  }

  @Override
  public void setSignalColor(ForgeDirection dir, DyeColor col) {
    rsColors.put(dir, col);
    setClientStateDirty();
  }

  @Override
  public DyeColor getSignalColor(ForgeDirection dir) {
    DyeColor res = rsColors.get(dir);
    if(res == null) {
      res = DyeColor.RED;
    }
    return res;
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtRoot) {
    super.writeToNBT(nbtRoot);
    nbtRoot.setShort("subtype", (short) subtype);
    nbtRoot.setFloat("energyStored", energyStored);

    for (Entry<ForgeDirection, RedstoneControlMode> entry : rsModes.entrySet()) {
      if(entry.getValue() != null) {
        short ord = (short) entry.getValue().ordinal();
        nbtRoot.setShort("pRsMode." + entry.getKey().name(), ord);
      }
    }

    for (Entry<ForgeDirection, DyeColor> entry : rsColors.entrySet()) {
      if(entry.getValue() != null) {
        short ord = (short) entry.getValue().ordinal();
        nbtRoot.setShort("pRsCol." + entry.getKey().name(), ord);
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtRoot) {
    super.readFromNBT(nbtRoot);
    subtype = nbtRoot.getShort("subtype");

    energyStored = Math.min(getTeslaBat().getMaxEnergyStored(), nbtRoot.getFloat("energyStored"));

    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      String key = "pRsMode." + dir.name();
      if(nbtRoot.hasKey(key)) {
        short ord = nbtRoot.getShort(key);
        if(ord >= 0 && ord < RedstoneControlMode.values().length) {
          rsModes.put(dir, RedstoneControlMode.values()[ord]);
        }
      }
      key = "pRsCol." + dir.name();
      if(nbtRoot.hasKey(key)) {
        short ord = nbtRoot.getShort(key);
        if(ord >= 0 && ord < DyeColor.values().length) {
          rsColors.put(dir, DyeColor.values()[ord]);
        }
      }
    }
  }

  @Override
  public void onTick() {
    if(powerHandler != null && powerHandler.getEnergyStored() > 0) {
      energyStored = Math.min(energyStored + powerHandler.getEnergyStored(), getTeslaBat().getMaxEnergyStored());
      powerHandler.setEnergy(0);
    }
  }

  @Override
  public float getEnergyStored() {
    return energyStored;
  }

  @Override
  public void setEnergyStored(float energyStored) {
    this.energyStored = energyStored;
  }

  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side) {
    ConnectionMode mode = getConnectionMode(side);
    if(mode == ConnectionMode.OUTPUT || mode == ConnectionMode.DISABLED || !isRedstoneEnabled(side)) {
      if(noInputPH == null) {
        noInputPH = new PowerHandler(this, Type.PIPE);
        noInputPH.configure(0, 0, 0, powerHandler.getMaxEnergyStored());
      }
      return noInputPH.getPowerReceiver();
    }
    if(powerHandler == null) {
      powerHandler = createPowerHandlerForType();
    }
    return powerHandler.getPowerReceiver();
  }

  @Override
  public float getMaxEnergyExtracted(ForgeDirection dir) {
    ConnectionMode mode = getConnectionMode(dir);
    if(mode == ConnectionMode.INPUT || mode == ConnectionMode.DISABLED || !isRedstoneEnabled(dir)) {
      return 0;
    }
    return getTeslaBat().getMaxEnergyExtracted();
  }

  private boolean isRedstoneEnabled(ForgeDirection dir) {
    boolean result;
    RedstoneControlMode mode = getRedstoneMode(dir);
    if(mode == RedstoneControlMode.NEVER) {
      return false;
    }
    if(mode == RedstoneControlMode.IGNORE) {
      return true;
    }

    DyeColor col = getSignalColor(dir);
    int signal = PipingUtil.getInternalSignalForColor(getBundle(), col);
    if(mode.isConditionMet(mode, signal)) {
      return true;
    }

    if(col != DyeColor.RED) {
      return false;
    }
    int val = getExternalRedstoneSignalForDir(dir);
    return mode.isConditionMet(mode, val);
  }

  private int getExternalRedstoneSignalForDir(ForgeDirection dir) {
    if(redstoneStateDirty) {
      externalRedstoneSignals.clear();
      redstoneStateDirty = false;
    }
    Integer cached = externalRedstoneSignals.get(dir);
    int result;
    if(cached == null) {
      TileEntity te = getBundle().getEntity();
      result = te.getWorldObj().getStrongestIndirectPower(te.xCoord, te.yCoord, te.zCoord);
      externalRedstoneSignals.put(dir, result);
    } else {
      result = cached;
    }
    return result;
  }
  
  @Override
  public float getMaxEnergyReceived(ForgeDirection dir)
  {
	  ConnectionMode mode = getConnectionMode(dir);
	  if(mode == ConnectionMode.OUTPUT || mode == ConnectionMode.DISABLED || !isRedstoneEnabled(dir)) 
	  {
		  return 0;
	  }
	  return getTeslaBat().getMaxEnergyReceived();
  }

  @Override
  public PowerHandler getPowerHandler() {
    return powerHandler;
  }

  @Override
  public void applyPerdition() {
  }

  @Override
  public void doWork(PowerHandler workProvider) {
  }

  @Override
  public World getWorld() {
    return getBundle().getEntity().getWorldObj();
  }

  @Override
  public boolean onNeighborBlockChange(Block blockId) {
    redstoneStateDirty = true;
    if(network != null) {
      network.powerManager.receptorsChanged();
    }
    return super.onNeighborBlockChange(blockId);
  }

  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    if(getMaxEnergyReceived(from) == 0) {
      return 0;
    }
    float freeSpace = getTeslaBat().getMaxEnergyStored() - energyStored;
    int result = (int) Math.min(maxReceive / 10, freeSpace);
    if(!simulate) {
      energyStored += result;
    }
    return result * 10;
  }

  @Override
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    return 0;
  }

  @Override
  public boolean canInterface(ForgeDirection from) {
    return true;
  }

  @Override
  public int getEnergyStored(ForgeDirection from) {
    return (int) (energyStored * 10);
  }

  @Override
  public int getMaxEnergyStored(ForgeDirection from) {
    return getTeslaBat().getMaxEnergyStored();
  }

  @Override
  public AbstractPipingNetwork<?, ?> getNetwork() {
    return network;
  }

  @Override
  public boolean setNetwork(AbstractPipingNetwork<?, ?> network) {
    this.network = (PowerPipingNetwork) network;
    return true;
  }

  @Override
  public boolean canConnectToExternal(ForgeDirection direction, boolean ignoreDisabled) {
    IPowerInterface rec = getExternalPowerReceptor(direction);
    return rec != null && rec.canPipingConnect(direction);
  }

  @Override
  public void externalConnectionAdded(ForgeDirection direction) {
    super.externalConnectionAdded(direction);
    if(network != null) {
      TileEntity te = bundle.getEntity();
      network.powerReceptorAdded(this, direction, te.xCoord + direction.offsetX, te.yCoord + direction.offsetY, te.zCoord + direction.offsetZ,
          getExternalPowerReceptor(direction));
    }
  }

  @Override
  public void externalConnectionRemoved(ForgeDirection direction) {
    super.externalConnectionRemoved(direction);
    if(network != null) {
      TileEntity te = bundle.getEntity();
      network.powerReceptorRemoved(te.xCoord + direction.offsetX, te.yCoord + direction.offsetY, te.zCoord + direction.offsetZ);
    }
  }

  @Override
  public IPowerInterface getExternalPowerReceptor(ForgeDirection direction) {
    TileEntity te = bundle.getEntity();
    World world = te.getWorldObj();
    if(world == null) {
      return null;
    }
    TileEntity test = world.getTileEntity(te.xCoord + direction.offsetX, te.yCoord + direction.offsetY, te.zCoord + direction.offsetZ);
    if(test == null) {
      return null;
    }
    if(test instanceof IPipingBundle) {
      return null;
    }
    return PowerHandlerUtil.create(test);
  }

  @Override
  public ItemStack createItem() {
    return createItemStackForSubtype(subtype);
  }

  @Override
  public Class<? extends IPiping> getBasePipingType() {
    return IPowerPiping.class;
  }

  // Rendering
  @Override
  public IIcon getTextureForState(CollidableComponent component) {
    if(component.dir == ForgeDirection.UNKNOWN) {
      return ICONS.get(ICON_CORE_KEY + POSTFIX[subtype]);
    }
    if(COLOR_CONTROLLER_ID.equals(component.data)) {
      return IconUtil.whiteTexture;
    }
    return ICONS.get(ICON_KEY + POSTFIX[subtype]);
  }

  @Override
  public IIcon getTextureForInputMode() {
    return ICONS.get(ICON_KEY_INPUT + POSTFIX[subtype]);
  }

  @Override
  public IIcon getTextureForOutputMode() {
    return ICONS.get(ICON_KEY_OUTPUT + POSTFIX[subtype]);
  }

  @Override
  public IIcon getTransmitionTextureForState(CollidableComponent component) {
    return null;
  }

  @Override
  public Collection<CollidableComponent> createCollidables(CacheKey key) {
    Collection<CollidableComponent> baseCollidables = super.createCollidables(key);
    if(key.dir == ForgeDirection.UNKNOWN) {
      return baseCollidables;
    }

    BoundingBox bb = PipingGeometryUtil.instance.createBoundsForConnectionController(key.dir, key.offset);
    CollidableComponent cc = new CollidableComponent(IPowerPiping.class, bb, key.dir, COLOR_CONTROLLER_ID);

    List<CollidableComponent> result = new ArrayList<CollidableComponent>();
    result.addAll(baseCollidables);
    result.add(cc);

    return result;
  }



}