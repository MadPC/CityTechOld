package com.madpcgaming.citytech.piping;

import static net.minecraftforge.common.util.ForgeDirection.getOrientation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.rednet.IConnectableRedNet;
import powercrystals.minefactoryreloaded.api.rednet.RedNetConnectionType;
import buildcraft.api.tools.IToolWrench;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.GuiIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.PipingConnectorType;
import com.madpcgaming.citytech.piping.gui.ExternalConnectionContainer;
import com.madpcgaming.citytech.piping.gui.GuiExternalConnection;
import com.madpcgaming.citytech.piping.redstone.IInsulatedRedstonePiping;
import com.madpcgaming.citytech.piping.redstone.IRedstonePiping;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.util.Util;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPipingBundle extends Block implements ITileEntityProvider,
		IConnectableRedNet, IGuiHandler
{

	private static final String	KEY_CONNECTOR_ICON	= "";

	public static BlockPipingBundle create()
	{
		BlockPipingBundle result = new BlockPipingBundle();
		result.init();
		return result;
	}

	public static int rendererId = -1;

	private IIcon connectorIcon;

	private IIcon lastRemovedComponetIcon	= null;

	private Random rand	= new Random();

	protected BlockPipingBundle()
	{
		super(new Material(MapColor.stoneColor));
		setHardness(0.5F);
		setBlockBounds(0.334f, 0.334f, 0.334f, 0.667f, 0.667f, 0.667f);
		setStepSound(Block.soundTypeMetal);
		setBlockName(Strings.PLATINUM_BLOCK_NAME);
		setCreativeTab(null);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		IIcon tex = null;

		TilePipingBundle pb = (TilePipingBundle) world.getTileEntity(target.blockX, target.blockY, target.blockZ);
		if (PipingUtil.isSolidFacadeRendered(pb,Minecraft.getMinecraft().thePlayer)) 
		{
			if (pb.getFacadeID() > 0)
			{
				tex = Block.getBlockById(pb.getFacadeID()).getIcon(
						target.sideHit, pb.getFacadeMetadata());
			}
		}
		else if (target.hitInfo instanceof CollidableComponent) 
		{
			CollidableComponent cc = (CollidableComponent) target.hitInfo;
			IPiping con = pb.getPiping(cc.pipingType);
			if (con != null) 
			{
				tex = con.getTextureForState(cc);
			}
		}
		if (tex == null) 
		{
			tex = blockIcon;
		}
		lastRemovedComponetIcon = tex;
		addBlockHitEffects(world, effectRenderer, target.blockX, target.blockY, target.blockZ, target.sideHit, tex);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		IIcon tex = lastRemovedComponetIcon;
		byte b0 = 4;
		for (int j1 = 0; j1 < b0; ++j1) {
			for (int k1 = 0; k1 < b0; ++k1) {
				for (int l1 = 0; l1 < b0; ++l1) {
					double d0 = x + (j1 + 0.5D) / b0;
					double d1 = y + (k1 + 0.5D) / b0;
					double d2 = z + (l1 + 0.5D) / b0;
					int i2 = this.rand.nextInt(6);
					EntityDiggingFX fx = new EntityDiggingFX(world, d0, d1, d2,
							d0 - x - 0.5D, d1 - y - 0.5D, d2 - z - 0.5D, this,
							i2, 0).applyColourMultiplier(x, y, z);
					fx.setParticleIcon(tex);
					effectRenderer.addEffect(fx);
				}
			}
		}
		return true;

	}

	@SideOnly(Side.CLIENT)
	private void addBlockHitEffects(World world, EffectRenderer effectRenderer,
			int x, int y, int z, int side, IIcon tex)
	{
		float f = 0.1F;
		double d0 = x + rand.nextDouble()
				* (getBlockBoundsMaxX() - getBlockBoundsMinX() - f * 2.0F) + f
				+ getBlockBoundsMinX();
		double d1 = y + rand.nextDouble()
				* (getBlockBoundsMaxY() - getBlockBoundsMinY() - f * 2.0F) + f
				+ getBlockBoundsMinY();
		double d2 = z + rand.nextDouble()
				* (getBlockBoundsMaxZ() - getBlockBoundsMinZ() - f * 2.0F) + f
				+ getBlockBoundsMinZ();
		if (side == 0) {
			d1 = y + getBlockBoundsMinY() - f;
		}
		else if (side == 1) {
			d1 = y + getBlockBoundsMaxY() + f;
		}
		else if (side == 2) {
			d2 = z + getBlockBoundsMinZ() - f;
		}
		else if (side == 3) {
			d2 = z + getBlockBoundsMaxZ() + f;
		}
		else if (side == 4) {
			d0 = x + getBlockBoundsMinX() - f;
		}
		else if (side == 5) {
			d0 = x + getBlockBoundsMaxX() + f;
		}
		EntityDiggingFX digFX = new EntityDiggingFX(world, d0, d1, d2, 0.0D,
				0.0D, 0.0D, this, side, 0);
		digFX.applyColourMultiplier(x, y, z).multiplyVelocity(0.2F)
				.multipleParticleScaleBy(0.6F);
		digFX.setParticleIcon(tex);
		effectRenderer.addEffect(digFX);
	}

	private void init()
	{
		GameRegistry.registerBlock(this, Strings.PIPING_BUNDLE_NAME);
		GameRegistry.registerTileEntity(TilePipingBundle.class,	ModBlocks.blockPipingBundle + "TileEntity");

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			// Don't exist
			CityTech.proxy.registerGuiHandler(GuiIds.GUI_ID_EXTERNAL_CONNECTION_BASE + dir.ordinal(),this);
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			int x, int y, int z)
	{
		if (target != null && target.hitInfo instanceof CollidableComponent) {
			CollidableComponent cc = (CollidableComponent) target.hitInfo;
			TilePipingBundle bundle = (TilePipingBundle) world.getTileEntity(x,
					y, z);
			IPiping pipe = bundle.getPiping(cc.pipingType);
			if (pipe != null) {
				return pipe.createItem();
			}
			else if (cc.pipingType == null && bundle.getFacadeID() > 0) {
				ItemStack fac = new ItemStack(ModItems.itemPipingFacade, 1, 0);
				return fac;
			}
		}
		return null;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return 0;
		}
		IPipingBundle bun = (IPipingBundle) te;
		return bun.getFacadeID() > 0 ? bun.getFacadeMetadata() : 0;
	}

	@Override
	public int quantityDropped(Random r)
	{
		return 0;
	}

	public IIcon getConnectorIcon()
	{
		return connectorIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		connectorIcon = iconRegister.registerIcon(KEY_CONNECTOR_ICON);
		blockIcon = connectorIcon;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return false;
		}
		IPipingBundle con = (IPipingBundle) te;
		if (con.getFacadeID() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return rendererId;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TilePipingBundle();
	}


	public int getLightOpacity(World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return super.getLightOpacity(world, x, y, z);
		}
		IPipingBundle con = (IPipingBundle) te;
		return con.getLightOpacity();
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return super.getLightValue(world, x, y, z);
		}
		IPipingBundle con = (IPipingBundle) te;
		if (con.getFacadeID() > 0) {
			return 0;
		}
		Collection<IPiping> pipes = con.getPiping();
		int result = 0;
		for (IPiping pipe : pipes) {
			result += pipe.getLightValue();
		}
		return result;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z,
			int par5)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return 0;
		}
		IPipingBundle bundle = (IPipingBundle) te;
		IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
		if (con == null) {
			return 0;
		}
		return con.isProvidingStrongPower(getOrientation(par5));
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z,
			int par5)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return 0;
		}
		IPipingBundle bundle = (IPipingBundle) te;
		IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
		if (con == null) {
			return 0;
		}
		return con.isProvidingWeakPower(getOrientation(par5));
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public RedNetConnectionType getConnectionType(World world, int x, int y,
			int z, ForgeDirection side)
	{
		return RedNetConnectionType.CableAll;
	}

	@Override
	public int[] getOutputValues(World world, int x, int y, int z,
			ForgeDirection side)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return new int[16];
		}
		IPipingBundle bundle = (IPipingBundle) te;
		IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
		if (con == null) {
			return new int[16];
		}
		return con.getOutputValues(world, x, y, z, side);
	}

	@Override
	public int getOutputValue(World world, int x, int y, int z,
			ForgeDirection side, int subnet)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return 0;
		}
		IPipingBundle bundle = (IPipingBundle) te;
		IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
		if (con == null) {
			return 0;
		}
		return con.getOutputValue(world, x, y, z, side, subnet);
	}

	@Override
	public void onInputsChanged(World world, int x, int y, int z,
			ForgeDirection side, int[] inputValues)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPipingBundle) {
			IPipingBundle bundle = (IPipingBundle) te;
			IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
			if (con != null) {
				if (con instanceof IInsulatedRedstonePiping) {
					((IInsulatedRedstonePiping) con).onInputsChanged(side,
							inputValues);
				}
				con.onNeighborBlockChange(world.getBlock(x, y, z));
			}
		}

	}

	@Override
	public void onInputChanged(World world, int x, int y, int z,
			ForgeDirection side, int inputValue)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPipingBundle) {
			IPipingBundle bundle = (IPipingBundle) te;
			IRedstonePiping con = bundle.getPiping(IRedstonePiping.class);
			if (con != null) {
				if (con instanceof IInsulatedRedstonePiping) {
					((IInsulatedRedstonePiping) con).onInputChanged(side,
							inputValue);
				}
				con.onNeighborBlockChange(world.getBlock(x, y, z));
			}
		}

	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
	{
		IPipingBundle te = (IPipingBundle) world.getTileEntity(x, y, z);
		if (te == null) {
			return;
		}
		EntityPlayer player = world.getClosestPlayer(x, y, z, 10);
		boolean breakBlock = true;
		List<ItemStack> drop = new ArrayList<ItemStack>();
		if (PipingUtil.isSolidFacadeRendered(te, player)) {
			breakBlock = false;
			ItemStack fac = new ItemStack(ModItems.itemPipingFacade, 1, 0);
			drop.add(fac);
			te.setFacadeId(-1);
			te.setFacadeMetadata(0);
		}

		if (breakBlock) {
			List<RaytraceResult> results = doRayTraceAll(world, x, y, z, player);
			RaytraceResult.sort(getEyePosition(world, player), results);
			for (RaytraceResult rt : results) {
				if (breakPiping(te, drop, rt, player)) {
					break;
				}
			}
		}

		breakBlock = te.getPiping().isEmpty() && !te.hasFacade();

		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			for (ItemStack st : drop) {
				Util.dropItems(world, st, x, y, z, false);
			}
		}

		if (!breakBlock) {
			world.markBlockForUpdate(x, y, z);
			return;
		}
		world.setBlockToAir(x, y, z);
	}

	private boolean breakPiping(IPipingBundle te, List<ItemStack> drop, RaytraceResult rt, EntityPlayer player)
	{
		if (rt == null || rt.component == null) {
			return false;
		}
		Class<? extends IPiping> type = rt.component.pipingType;
		if (!PipingUtil.renderPiping(player, type)) {
			return false;
		}

		if (type == null) {
			List<IPiping> cons = new ArrayList<IPiping>(te.getPiping());
			boolean droppedUnconected = false;
			for (IPiping con : cons) {
				if (con.getPipingConnections().isEmpty()
						&& con.getExternalConnections().isEmpty()
						&& PipingUtil.renderPiping(player, con)) {
					te.removePiping(con);
					drop.add(con.createItem());
					droppedUnconected = true;
				}
			}
			if (!droppedUnconected) {
				for (IPiping con : cons) {
					if (PipingUtil.renderPiping(player, con)) {
						te.removePiping(con);
						drop.add(con.createItem());
					}
				}
			}
		}
		else {
			IPiping con = te.getPiping(type);
			if (con != null) {
				te.removePiping(con);
				drop.add(con.createItem());
			}
		}

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5,
			int par6)
	{
		IPipingBundle te = (IPipingBundle) world.getTileEntity(x, y, z);
		if (te != null) {
			te.onBlockRemoved();
		}
		world.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9)
	{

		IPipingBundle bundle = (IPipingBundle) world.getTileEntity(x, y, z);
		if (bundle == null) {
			return false;
		}

		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() == ModItems.itemPipingFacade
				&& !bundle.hasFacade()) {
			// Add facade
			if (player.isSneaking()) {
				return false;
			}
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize--;
			}
			world.markBlockForUpdate(x, y, z);
			bundle.getEntity().markDirty();
			return true;

		}
		else if (PipingUtil.isPipingEquipped(player)) {
			// Add conduit
			if (player.isSneaking()) {
				return false;
			}

			IPipingItem equipped = (IPipingItem) stack.getItem();
			if (!bundle.hasType(equipped.getBasePipingType())) {
				bundle.addPiping(equipped.createPiping(stack));
				if (!player.capabilities.isCreativeMode) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F,
							stepSound.func_150496_b(),
							(stepSound.getVolume() + 1.0F) / 2.0F,
							stepSound.getPitch() * 0.8F);
					player.getCurrentEquippedItem().stackSize--;
				}
				return true;
			}

		}

		if (PipingUtil.isToolEquipped(player) && player.isSneaking()) {
			if (player.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
				IToolWrench wrench = (IToolWrench) player
						.getCurrentEquippedItem().getItem();
				if (wrench.canWrench(player, x, y, z)) {
					if (!world.isRemote) {
						// Doesn't exist
						removedByPlayer(world, player, x, y, z);
						if (player.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
							((IToolWrench) player.getCurrentEquippedItem()
									.getItem()).wrenchUsed(player, x, y, z);
						}
					}
					return true;
				}
			}
		}

		// Check conduit defined actions
		RaytraceResult closest = doRayTrace(world, x, y, z, player);
		List<RaytraceResult> all = null;
		if (closest != null) {
			all = doRayTraceAll(world, x, y, z, player);
		}

		if (closest != null && closest.component != null
				&& closest.component.data instanceof PipingConnectorType) {

			PipingConnectorType conType = (PipingConnectorType) closest.component.data;
			if (conType == PipingConnectorType.INTERNAL) {
				boolean result = false;
				// if its a connector pass the event on to all conduits
				for (IPiping con : bundle.getPiping()) {
					if (PipingUtil
							.renderPiping(player, con.getCollidableType())
							&& con.onBlockActivated(
									player,
									getHitForPipingType(all,
											con.getCollidableType()), all)) {
						bundle.getEntity().markDirty();
						result = true;
					}

				}
				if (result) {
					return true;
				}
			}
			else {
				player.openGui(CityTech.instance,
						// Doesn't exist
						GuiIds.GUI_ID_EXTERNAL_CONNECTION_BASE + closest.component.dir.ordinal(), world, x, y,
						z);
				return true;
			}
		}

		if (closest == null || closest.component == null
				|| closest.component.pipingType == null && all == null) {
			return false;
		}

		if (all != null) {
			RaytraceResult.sort(getEyePosition(world, player), all);
			for (RaytraceResult rr : all) {
				if (PipingUtil.renderPiping(player, rr.component.pipingType)
						&& !(rr.component.data instanceof PipingConnectorType)) {

					IPiping con = bundle.getPiping(rr.component.pipingType);
					if (con != null && con.onBlockActivated(player, rr, all)) {
						bundle.getEntity().markDirty();
						return true;
					}
				}
			}
		}
		else {
			IPiping closestPiping = bundle
					.getPiping(closest.component.pipingType);
			if (closestPiping != null
					&& PipingUtil.renderPiping(player, closestPiping)
					&& closestPiping.onBlockActivated(player, closest, all)) {
				bundle.getEntity().markDirty();
				return true;
			}
		}
		return false;

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPipingBundle) {
			// Doesn't exist
			return new ExternalConnectionContainer(player.inventory,(IPipingBundle) te, ForgeDirection.values()[ID - GuiIds.GUI_ID_EXTERNAL_CONNECTION_BASE]);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IPipingBundle) {
			// Doesn't exist
			return new GuiExternalConnection(player.inventory, (IPipingBundle) te, ForgeDirection.values()[ID - GuiIds.GUI_ID_EXTERNAL_CONNECTION_BASE]);
		}
		return null;
	}

	private RaytraceResult getHitForPipingType(List<RaytraceResult> all,
			Class<? extends IPiping> collidableType)
	{
		for (RaytraceResult rr : all) {
			if (rr.component != null
					&& rr.component.pipingType == collidableType) {
				return rr;
			}
		}
		return null;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block blockId)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if ((tile instanceof IPipingBundle)) {
			((IPipingBundle) tile).onNeighborBlockChange(blockId);
		}
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
	{

		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return;
		}
		IPipingBundle con = (IPipingBundle) te;
		if (con.getFacadeID() > 0) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb,
					arraylist, par7Entity);
		}
		else {

			Collection<CollidableComponent> bounds = con
					.getCollidableComponents();
			for (CollidableComponent bnd : bounds) {
				setBlockBounds(bnd.bound.minX, bnd.bound.minY, bnd.bound.minZ,
						bnd.bound.maxX, bnd.bound.maxY, bnd.bound.maxZ);
				super.addCollisionBoxesToList(world, x, y, z, axisalignedbb,
						arraylist, par7Entity);
			}

			if (con.getPiping().isEmpty()) { // just in case
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				super.addCollisionBoxesToList(world, x, y, z, axisalignedbb,
						arraylist, par7Entity);
			}
		}

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x,
			int y, int z)
	{

		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return null;
		}
		IPipingBundle con = (IPipingBundle) te;

		BoundingBox minBB = new BoundingBox(1, 1, 1, 0, 0, 0);
		if (!PipingUtil.isSolidFacadeRendered(con,
				CityTech.proxy.getClientPlayer())) {

			Collection<CollidableComponent> bounds = con
					.getCollidableComponents();
			for (CollidableComponent bnd : bounds) {
				minBB = minBB.expandBy(bnd.bound);
			}

		}
		else {
			minBB = new BoundingBox(0, 0, 0, 1, 1, 1);
		}

		if (!minBB.isValid()) {
			minBB = new BoundingBox(0, 0, 0, 1, 1, 1);
		}

		return AxisAlignedBB.getBoundingBox(x + minBB.minX, y + minBB.minY, z
				+ minBB.minZ, x + minBB.maxX, y + minBB.maxY, z + minBB.maxZ);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y,
			int z, Vec3 origin, Vec3 direction)
	{
		RaytraceResult raytraceResult = doRayTrace(world, x, y, z, origin,
				direction, null);
		if (raytraceResult == null) {
			return null;
		}

		if (raytraceResult.movingObjectPosition != null) {
			raytraceResult.movingObjectPosition.hitInfo = raytraceResult.component;

		}
		return raytraceResult.movingObjectPosition;
	}

	public RaytraceResult doRayTrace(World world, int x, int y, int z,
			EntityPlayer entityPlayer)
	{
		List<RaytraceResult> allHits = doRayTraceAll(world, x, y, z,
				entityPlayer);
		if (allHits == null) {
			return null;
		}
		Vec3 origin = getEyePosition(world, entityPlayer);
		return RaytraceResult.getClosestHit(origin, allHits);
	}

	private Vec3 getEyePosition(World world, EntityPlayer entityPlayer)
	{
		double posY = entityPlayer.posY + 1.62 - entityPlayer.yOffset;
		if (!world.isRemote && entityPlayer.isSneaking()) {
			posY -= 0.08;
		}
		Vec3 origin = Vec3.fakePool.getVecFromPool(entityPlayer.posX, posY,
				entityPlayer.posZ);
		return origin;
	}

	public List<RaytraceResult> doRayTraceAll(World world, int x, int y, int z,
			EntityPlayer entityPlayer)
	{
		double pitch = Math.toRadians(entityPlayer.rotationPitch);
		double yaw = Math.toRadians(entityPlayer.rotationYaw);

		double dirX = -Math.sin(yaw) * Math.cos(pitch);
		double dirY = -Math.sin(pitch);
		double dirZ = Math.cos(yaw) * Math.cos(pitch);

		double reachDistance = CityTech.proxy
				.getReachDistanceForPlayer(entityPlayer);

		Vec3 origin = getEyePosition(world, entityPlayer);
		Vec3 direction = origin.addVector(dirX * reachDistance, dirY
				* reachDistance, dirZ * reachDistance);
		return doRayTraceAll(world, x, y, z, origin, direction, entityPlayer);
	}

	private RaytraceResult doRayTrace(World world, int x, int y, int z,
			Vec3 origin, Vec3 direction, EntityPlayer entityPlayer)
	{
		List<RaytraceResult> allHits = doRayTraceAll(world, x, y, z, origin,
				direction, entityPlayer);
		if (allHits == null) {
			return null;
		}
		return RaytraceResult.getClosestHit(origin, allHits);
	}

	protected List<RaytraceResult> doRayTraceAll(World world, int x, int y,
			int z, Vec3 origin, Vec3 direction, EntityPlayer player)
	{

		TileEntity te = world.getTileEntity(x, y, z);
		if (!(te instanceof IPipingBundle)) {
			return null;
		}
		IPipingBundle bundle = (IPipingBundle) te;
		List<RaytraceResult> hits = new ArrayList<RaytraceResult>();

		if (PipingUtil.isSolidFacadeRendered(bundle, player)) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			MovingObjectPosition hitPos = super.collisionRayTrace(world, x, y,
					z, origin, direction);
			if (hitPos != null) {
				hits.add(new RaytraceResult(new CollidableComponent(null,
						BoundingBox.UNIT_CUBE, ForgeDirection.UNKNOWN, null),
						hitPos));
			}
		}
		else {

			Collection<CollidableComponent> components = new ArrayList<CollidableComponent>(
					bundle.getCollidableComponents());
			for (CollidableComponent component : components) {
				setBlockBounds(component.bound.minX, component.bound.minY,
						component.bound.minZ, component.bound.maxX,
						component.bound.maxY, component.bound.maxZ);
				MovingObjectPosition hitPos = super.collisionRayTrace(world, x,
						y, z, origin, direction);
				if (hitPos != null) {
					hits.add(new RaytraceResult(component, hitPos));
				}
			}
			if (bundle.getPiping().isEmpty()
					&& !PipingUtil.isFacadeHidden(bundle, player)) {
				setBlockBounds(0, 0, 0, 1, 1, 1);
				MovingObjectPosition hitPos = super.collisionRayTrace(world, x,
						y, z, origin, direction);
				if (hitPos != null) {
					hits.add(new RaytraceResult(null, hitPos));
				}
			}
		}

		setBlockBounds(0, 0, 0, 1, 1, 1);

		return hits;
	}

}