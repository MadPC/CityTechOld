package com.madpcgaming.citytech.piping.render;

import static com.madpcgaming.citytech.render.CubeRenderer.addVecWithUV;
import static com.madpcgaming.citytech.render.CubeRenderer.setupVertices;
import static com.madpcgaming.citytech.render.CubeRenderer.verts;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.piping.BlockPipingBundle;
import com.madpcgaming.citytech.piping.ConnectionMode;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.IPipingBundle;
import com.madpcgaming.citytech.piping.geom.CollidableComponent;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.render.RenderUtil;
import com.madpcgaming.citytech.vecmath.Vertex;


public class DefaultPipingRenderer implements PipingRenderer {

  protected float transmissionScaleFactor;

  @Override
  public boolean isRendererForPiping(IPiping piping) {
    return true;
  }

  @Override
  public void renderEntity(PipingBundleRenderer conduitBundleRenderer, IPipingBundle te, IPiping piping, double x, double y, double z, float partialTick,
      float worldLight) {

    Collection<CollidableComponent> components = piping.getCollidableComponents();
    Tessellator tessellator = Tessellator.instance;

    transmissionScaleFactor = piping.getTransmitionGeometryScale();

    IIcon tex;
    for (CollidableComponent component : components) {
      if(renderComponent(component)) {
        float selfIllum = Math.max(worldLight, piping.getSelfIlluminationForState(component));
        if(isNSEWUD(component.dir) &&
            piping.getTransmitionTextureForState(component) != null) {
          tessellator.setBrightness((int) (worldLight));
          tex = piping.getTransmitionTextureForState(component);
          renderTransmission(piping, tex, component, selfIllum);
        }

        tex = piping.getTextureForState(component);
        if(tex != null) {
          tessellator.setBrightness((int) (worldLight));
          renderConduit(tex, piping, component, selfIllum);
        }
      }

    }

  }

  @Override
  public void renderDynamicEntity(PipingBundleRenderer conduitBundleRenderer, IPipingBundle te, IPiping con, double x, double y, double z,
      float partialTick, float worldLight) {

  }

  protected void renderConduit(IIcon tex, IPiping piping, CollidableComponent component, float brightness) {

    if(isNSEWUD(component.dir)) {

      float scaleFactor = 0.75f;
      float xLen = Math.abs(component.dir.offsetX) == 1 ? 1 : scaleFactor;
      float yLen = Math.abs(component.dir.offsetY) == 1 ? 1 : scaleFactor;
      float zLen = Math.abs(component.dir.offsetZ) == 1 ? 1 : scaleFactor;

      BoundingBox cube = component.bound;
      BoundingBox bb = cube.scale(xLen, yLen, zLen);
      drawSection(bb, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV(), component.dir, false);

      if(piping.getConnectionMode(component.dir) == ConnectionMode.DISABLED) {
        tex = ((BlockPipingBundle) ModBlocks.blockPipingBundle).getConnectorIcon();
        List<Vertex> corners = component.bound.getCornersWithUvForFace(component.dir, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV());
        Tessellator tessellator = Tessellator.instance;
        for (Vertex c : corners) {
          addVecWithUV(c.xyz, c.uv.x, c.uv.y);
        }
      }

    } else {
      drawSection(component.bound, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV(), component.dir, true);
    }

  }

  protected void renderTransmission(IPiping conduit, IIcon tex, CollidableComponent component, float selfIllum) {
    //    RoundedSegmentRenderer.renderSegment(component.dir, component.bound, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV(),
    //        conduit.getConectionMode(component.dir) == ConnectionMode.DISABLED);

    float scaleFactor = 0.6f;
    float xLen = Math.abs(component.dir.offsetX) == 1 ? 1 : scaleFactor;
    float yLen = Math.abs(component.dir.offsetY) == 1 ? 1 : scaleFactor;
    float zLen = Math.abs(component.dir.offsetZ) == 1 ? 1 : scaleFactor;

    BoundingBox cube = component.bound;
    BoundingBox bb = cube.scale(xLen, yLen, zLen);
    drawSection(bb, tex.getMinU(), tex.getMaxU(), tex.getMinV(), tex.getMaxV(), component.dir, false);
  }

  protected boolean renderComponent(CollidableComponent component) {
    return true;
  }

  protected boolean isNSEWUD(ForgeDirection dir) {
    return dir == NORTH || dir == SOUTH || dir == EAST || dir == WEST || dir == UP || dir == DOWN;
  }

  protected void drawSection(BoundingBox bound, float minU, float maxU, float minV, float maxV, ForgeDirection dir, boolean isTransmission) {

    Tessellator tessellator = Tessellator.instance;

    if(isTransmission) {
      setVerticesForTransmission(bound, dir);
    } else {
      setupVertices(bound);
    }

    if(dir == NORTH || dir == UP || dir == EAST) { // maintain consistent
                                                   // texture
      // dir relative to the cneter
      // of the conduit
      float tmp = minU;
      minU = maxU;
      maxU = tmp;
    }

    boolean rotateSides = dir == UP || dir == DOWN;
    boolean rotateTopBottom = dir == NORTH || dir == SOUTH;
    float cm;
    if(dir != NORTH && dir != SOUTH) {
      tessellator.setNormal(0, 0, -1);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.NORTH);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateSides) {
        addVecWithUV(verts[1], maxU, maxV);
        addVecWithUV(verts[0], maxU, minV);
        addVecWithUV(verts[3], minU, minV);
        addVecWithUV(verts[2], minU, maxV);
      } else {
        addVecWithUV(verts[1], minU, minV);
        addVecWithUV(verts[0], maxU, minV);
        addVecWithUV(verts[3], maxU, maxV);
        addVecWithUV(verts[2], minU, maxV);
      }
      if(dir == WEST || dir == EAST) {
        float tmp = minU;
        minU = maxU;
        maxU = tmp;
      }
      tessellator.setNormal(0, 0, 1);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.SOUTH);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateSides) {
        addVecWithUV(verts[4], maxU, maxV);
        addVecWithUV(verts[5], maxU, minV);
        addVecWithUV(verts[6], minU, minV);
        addVecWithUV(verts[7], minU, maxV);
      } else {
        addVecWithUV(verts[4], minU, minV);
        addVecWithUV(verts[5], maxU, minV);
        addVecWithUV(verts[6], maxU, maxV);
        addVecWithUV(verts[7], minU, maxV);
      }
      if(dir == WEST || dir == EAST) {
        float tmp = minU;
        minU = maxU;
        maxU = tmp;
      }
    }

    if(dir != UP && dir != DOWN) {

      tessellator.setNormal(0, 1, 0);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.UP);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateTopBottom) {
        addVecWithUV(verts[6], maxU, maxV);
        addVecWithUV(verts[2], minU, maxV);
        addVecWithUV(verts[3], minU, minV);
        addVecWithUV(verts[7], maxU, minV);
      } else {
        addVecWithUV(verts[6], minU, minV);
        addVecWithUV(verts[2], minU, maxV);
        addVecWithUV(verts[3], maxU, maxV);
        addVecWithUV(verts[7], maxU, minV);
      }

      tessellator.setNormal(0, -1, 0);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.DOWN);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateTopBottom) {
        addVecWithUV(verts[0], minU, minV);
        addVecWithUV(verts[1], minU, maxV);
        addVecWithUV(verts[5], maxU, maxV);
        addVecWithUV(verts[4], maxU, minV);
      } else {
        addVecWithUV(verts[0], maxU, maxV);
        addVecWithUV(verts[1], minU, maxV);
        addVecWithUV(verts[5], minU, minV);
        addVecWithUV(verts[4], maxU, minV);
      }
    }

    if(dir != EAST && dir != WEST) {

      tessellator.setNormal(1, 0, 0);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.EAST);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateSides) {
        addVecWithUV(verts[2], minU, maxV);
        addVecWithUV(verts[6], minU, minV);
        addVecWithUV(verts[5], maxU, minV);
        addVecWithUV(verts[1], maxU, maxV);
      } else {
        addVecWithUV(verts[2], minU, maxV);
        addVecWithUV(verts[6], maxU, maxV);
        addVecWithUV(verts[5], maxU, minV);
        addVecWithUV(verts[1], minU, minV);
      }

      tessellator.setNormal(-1, 0, 0);
      if(!isTransmission) {
        cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.WEST);
        tessellator.setColorOpaque_F(cm, cm, cm);
      }
      if(rotateSides) {
        addVecWithUV(verts[0], maxU, maxV);
        addVecWithUV(verts[4], maxU, minV);
        addVecWithUV(verts[7], minU, minV);
        addVecWithUV(verts[3], minU, maxV);
      } else {
        addVecWithUV(verts[0], minU, minV);
        addVecWithUV(verts[4], maxU, minV);
        addVecWithUV(verts[7], maxU, maxV);
        addVecWithUV(verts[3], minU, maxV);
      }
    }
    tessellator.setColorOpaque_F(1, 1, 1);
  }

  protected void setVerticesForTransmission(BoundingBox bound, ForgeDirection dir) {
    float xs = dir.offsetX == 0 ? transmissionScaleFactor : 1;
    float ys = dir.offsetY == 0 ? transmissionScaleFactor : 1;
    float zs = dir.offsetZ == 0 ? transmissionScaleFactor : 1;
    setupVertices(bound.scale(xs, ys, zs));
  }
  public BoundingBox[] toCubes(BoundingBox bb) {


    float width = bb.maxX - bb.minX;
    float height = bb.maxY - bb.minY;
    float depth = bb.maxZ - bb.minZ;

    if(width > 0 && height > 0 && depth > 0) {
      if(width / depth > 1.5f || depth / width > 1.5f) {
        if(width > depth) {
          int numSplits = Math.round(width / depth);
          float newWidth = width / numSplits;
          BoundingBox[] result = new BoundingBox[numSplits];
          float lastMax = bb.minX;
          for (int i = 0; i < numSplits; i++) {
            float max = lastMax + newWidth;
            result[i] = new BoundingBox(lastMax, bb.minY, bb.minZ, max, bb.maxY, bb.maxZ);
            lastMax = max;
          }
          return result;

        } else {

          int numSplits = Math.round(depth / width);
          float newWidth = depth / numSplits;
          BoundingBox[] result = new BoundingBox[numSplits];
          float lastMax = bb.minZ;
          for (int i = 0; i < numSplits; i++) {
            float max = lastMax + newWidth;
            result[i] = new BoundingBox(bb.minX, bb.minY, lastMax, bb.maxX, bb.maxY, max);
            lastMax = max;
          }
          return result;

        }

      } else if(height / width > 1.5) {

        int numSplits = Math.round(height / width);
        float newWidth = height / numSplits;
        BoundingBox[] result = new BoundingBox[numSplits];
        float lastMax = bb.minY;
        for (int i = 0; i < numSplits; i++) {
          float max = lastMax + newWidth;
          result[i] = new BoundingBox(bb.minX, lastMax, bb.minZ, bb.maxX, max, bb.maxZ);
          lastMax = max;
        }
        return result;

      }
    }

    return new BoundingBox[] { bb };
  }

  @Override
  public boolean isDynamic() {
    return false;
  }

}