package com.madpcgaming.citytech.piping.facade;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.madpcgaming.citytech.piping.ModPiping;

public class FacadeRenderer implements IItemRenderer {

  @Override
  public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED || type == ItemRenderType.INVENTORY || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
    return true;
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    if(type == ItemRenderType.INVENTORY) {
      RenderBlocks renderBlocks = (RenderBlocks) data[0];
      renderToInventory(item, renderBlocks);
    } else if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
      renderEquipped(item, (RenderBlocks) data[0]);
    } else if(type == ItemRenderType.ENTITY) {
      renderEntity(item, (RenderBlocks) data[0]);
    } else {
      System.out.println("FacadeRenderer.renderItem: Unsupported render type");
    }
  }

  private void renderEntity(ItemStack item, RenderBlocks renderBlocks) {
    GL11.glPushMatrix();
    GL11.glScalef(0.5f, 0.5f, 0.5f);
    renderToInventory(item, renderBlocks);
    GL11.glPopMatrix();
  }

  private void renderEquipped(ItemStack item, RenderBlocks renderBlocks) {
    renderToInventory(item, renderBlocks);
  }

  private void renderToInventory(ItemStack item, RenderBlocks renderBlocks) 
  {
      renderBlocks.setOverrideBlockTexture(ModPiping.itemPipingFacade.getIconFromDamage(0));
      renderBlocks.renderBlockAsItem(Blocks.stone, 0, 1.0F);
      renderBlocks.clearOverrideBlockTexture();
    }

 }