package com.madpcgaming.citytech.piping.render;

import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;

public interface PipingRenderer {

  boolean isRendererForPiping(IPiping piping);

  void renderEntity(PipingBundleRenderer pipingBundleRenderer, IPipingBundle te, IPiping con, double x, double y, double z, float partialTick,
      float worldLight);

  boolean isDynamic();

  void renderDynamicEntity(PipingBundleRenderer pipingBundleRenderer, IPipingBundle te, IPiping con, double x, double y, double z, float partialTick,
      float worldLight);

}