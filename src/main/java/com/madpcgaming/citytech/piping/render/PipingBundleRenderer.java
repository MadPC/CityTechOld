package com.madpcgaming.citytech.piping.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.piping.BlockPipingBundle;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.IPipingBundle.FacadeRenderState;
import com.madpcgaming.citytech.piping.PipingDisplayMode;
import com.madpcgaming.citytech.piping.PipingUtil;
import com.madpcgaming.citytech.piping.facade.BlockPipingFacade;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.PipingGeometryUtil;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.render.CubeRenderer;
import com.madpcgaming.citytech.render.RenderUtil;
import com.madpcgaming.citytech.util.BlockCoord;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class PipingBundleRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

  public PipingBundleRenderer(float pipingScale) {
  }

  @Override
  public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
    IPipingBundle bundle = (IPipingBundle) te;
    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
    if(bundle.hasFacade() && !PipingUtil.isFacadeHidden(bundle, player)) {
      return;
    }

    float brightness = -1;
    for (IPiping con : bundle.getPiping()) {
      if(PipingUtil.renderPiping(player, con)) {
        PipingRenderer renderer = CityTech.proxy.getRendererForPiping(con);
        if(renderer.isDynamic()) {
          if(brightness == -1) {
            BlockCoord loc = bundle.getLocation();
            brightness = bundle.getEntity().getWorldObj().getLightBrightnessForSkyBlocks(loc.x, loc.y, loc.z, 0);

            RenderUtil.bindBlockTexture();

            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);

            Tessellator.instance.startDrawingQuads();
          }
          renderer.renderDynamicEntity(this, bundle, con, x, y, z, partialTick, brightness);

        }
      }
    }

    if(brightness != -1) {
      Tessellator.instance.draw();

      GL11.glShadeModel(GL11.GL_FLAT);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      GL11.glPopAttrib();
    }

  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rb) {

    IPipingBundle bundle = (IPipingBundle) world.getTileEntity(x, y, z);
    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

    boolean renderPiping = true;
    if(bundle.hasFacade()) {

      int facadeId = bundle.getFacadeID();
      if(PipingUtil.isFacadeHidden(bundle, player)) {
        bundle.setFacadeId(0, false);
        bundle.setFacadeRenderAs(FacadeRenderState.WIRE_FRAME);
      } else {
        bundle.setFacadeRenderAs(FacadeRenderState.FULL);
        renderPiping = false;
      }

      BlockPipingFacade facb = (BlockPipingFacade) Block.getBlockFromName(Strings.FACADE_PIPING_NAME);
      facb.setBlockOverride(bundle);
      facb.setBlockBounds(0, 0, 0, 1, 1, 1);
      rb.setRenderBoundsFromBlock(facb);
      rb.renderStandardBlock(facb, x, y, z);
      facb.setBlockOverride(null);

      bundle.setFacadeId(facadeId, false);

    } else {
      bundle.setFacadeRenderAs(FacadeRenderState.NONE);
    }

    if(renderPiping) {
      renderPiping(bundle, x, y, z, 0);
    }

    return true;
  }

  public void renderPiping(IPipingBundle bundle, double x, double y, double z, float partialTick) {
    BlockCoord loc = bundle.getLocation();
    float brightness = bundle.getEntity().getWorldObj().getLightBrightnessForSkyBlocks(loc.x, loc.y, loc.z, 0);

    Tessellator tessellator = Tessellator.instance;
    tessellator.setColorOpaque_F(1, 1, 1);
    tessellator.addTranslation((float) x, (float) y, (float) z);

    // Conduits
    Set<ForgeDirection> externals = new HashSet<ForgeDirection>();
    EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

    List<BoundingBox> wireBounds = new ArrayList<BoundingBox>();

    for (IPiping con : bundle.getPiping()) {

      if(PipingUtil.renderPiping(player, con)) {
        PipingRenderer renderer = CityTech.proxy.getRendererForPiping(con);
        renderer.renderEntity(this, bundle, con, x, y, z, partialTick, brightness);
        Set<ForgeDirection> extCons = con.getExternalConnections();
        for (ForgeDirection dir : extCons) {
          if(con.getConnectionMode(dir) != ConnectionMode.DISABLED && con.getConnectionMode(dir) != ConnectionMode.NOT_SET) {
            externals.add(dir);
          }
        }
      } else if(con != null) {
        Collection<CollidableComponent> components = con.getCollidableComponents();
        for (CollidableComponent component : components) {
          wireBounds.add(component.bound);
        }
      }

    }

    // Internal conectors between conduits
    List<CollidableComponent> connectors = bundle.getConnectors();
    for (CollidableComponent component : connectors) {
      if(component.pipingType != null) {
        IPiping piping = bundle.getPiping(component.pipingType);
        if(piping != null) {
          if(PipingUtil.renderPiping(player, component.pipingType)) {
            tessellator.setBrightness((int) (brightness));
            CubeRenderer.render(component.bound, piping.getTextureForState(component), true);
          } else {
            wireBounds.add(component.bound);
          }
        }

      } else if(PipingUtil.getDisplayMode(player) == PipingDisplayMode.ALL) {
        IIcon tex = ((BlockPipingBundle) ModBlocks.blockPipingBundle).getConnectorIcon();
        CubeRenderer.render(component.bound, tex);
      }
    }

    //render these after the 'normal' conduits so help with proper blending
    for (BoundingBox wireBound : wireBounds) {
      Tessellator.instance.setColorRGBA_F(1, 1, 1, 0.25f);
      CubeRenderer.render(wireBound, ModBlocks.blockPipingFacade.getIcon(0, 0));
    }

    Tessellator.instance.setColorRGBA_F(1, 1, 1, 1f);
    // External connection terminations
    for (ForgeDirection dir : externals) {
      renderExternalConnection(dir);
    }

    tessellator.addTranslation(-(float) x, -(float) y, -(float) z);

  }

  private void renderExternalConnection(ForgeDirection dir) {
    IIcon tex = ((BlockPipingBundle) ModBlocks.blockPipingBundle).getConnectorIcon();
    BoundingBox[] bbs = PipingGeometryUtil.instance.getExternalConnectorBoundingBoxes(dir);
    for (BoundingBox bb : bbs) {
      CubeRenderer.render(bb, tex, true);
    }
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
  }

  @Override
  public int getRenderId() {
    return BlockPipingBundle.rendererId;
  }

}