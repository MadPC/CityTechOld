package com.madpcgaming.citytech.piping.power;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.machine.RedstoneControlMode;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.piping.geom.ConnectionModeGeometry;
import com.madpcgaming.citytech.piping.geom.Offset;
import com.madpcgaming.citytech.piping.render.DefaultPipingRenderer;
import com.madpcgaming.citytech.piping.render.PipingBundleRenderer;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.render.CubeRenderer;
import com.madpcgaming.citytech.util.ForgeDirectionOffsets;
import com.madpcgaming.citytech.vecmath.Vector3d;

public class PowerPipingRenderer extends DefaultPipingRenderer {

  @Override
  public boolean isRendererForPiping(IPiping piping) {
    return piping instanceof IPowerPiping;
  }

  @Override
  public void renderEntity(PipingBundleRenderer pipingBundleRenderer, IPipingBundle te, IPiping piping, double x, double y, double z, float partialTick,
      float worldLight) {
    super.renderEntity(pipingBundleRenderer, te, piping, x, y, z, partialTick, worldLight);

    if(!piping.hasConnectionMode(ConnectionMode.INPUT) && !piping.hasConnectionMode(ConnectionMode.OUTPUT)) {
      return;
    }
    IPowerPiping pp = (IPowerPiping) piping;
    for (ForgeDirection dir : piping.getExternalConnections()) {
      IIcon tex = null;
      if(piping.getConnectionMode(dir) == ConnectionMode.INPUT) {
        tex = pp.getTextureForInputMode();
      } else if(piping.getConnectionMode(dir) == ConnectionMode.OUTPUT) {
        tex = pp.getTextureForOutputMode();
      }
      if(tex != null) {
        Offset offset = te.getOffset(IPowerPiping.class, dir);
        ConnectionModeGeometry.renderModeConnector(dir, offset, tex, true);
      }
    }

  }

  @Override
  protected void renderPiping(IIcon tex, IPiping piping, CollidableComponent component, float selfIllum) {
    if(IPowerPiping.COLOR_CONTROLLER_ID.equals(component.data)) {
      IPowerPiping pp = (IPowerPiping) piping;
      ConnectionMode conMode = pp.getConnectionMode(component.dir);
      if(piping.containsExternalConnection(component.dir) && pp.getRedstoneMode(component.dir) != RedstoneControlMode.IGNORE
          && conMode != ConnectionMode.DISABLED) {
        int c = ((IPowerPiping) piping).getSignalColor(component.dir).getColor();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_I(c);

        Offset offset = piping.getBundle().getOffset(IPowerPiping.class, component.dir);
        BoundingBox bound = component.bound;
        if(conMode != ConnectionMode.IN_OUT) {
          Vector3d trans = ForgeDirectionOffsets.offsetScaled(component.dir, -0.075);
          bound = bound.translate(trans);
        }
        CubeRenderer.render(bound, tex);
        tessellator.setColorOpaque(255, 255, 255);
      }
    } else {
      super.renderPiping(tex, piping, component, selfIllum);
    }
  }
}