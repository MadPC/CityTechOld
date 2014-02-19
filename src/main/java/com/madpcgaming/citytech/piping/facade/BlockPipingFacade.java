package com.madpcgaming.citytech.piping.facade;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.piping.IPipingBundle;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPipingFacade extends Block {

  public static BlockPipingFacade create() {
    BlockPipingFacade result = new BlockPipingFacade();
    result.init();
    return result;
  }

  private Block blockOverride;

  private BlockPipingFacade() {
    super(new Material(MapColor.stoneColor));
    setHardness(0.5F);
    setStepSound(Block.soundTypeStone);
    setBlockName(Strings.FACADE_PIPING_NAME);
    setCreativeTab(null);
  }

  private void init() {
    GameRegistry.registerBlock(this, Strings.FACADE_PIPING_NAME);
  }

  public void registerIcons(IIconRegister iconRegister) {
    blockIcon = iconRegister.registerIcon("enderio:conduitFacade");
  }


  @SideOnly(Side.CLIENT)
  public IIcon getBlockTexture(IBlockAccess ba, int x, int y, int z, int side) {
    TileEntity te = ba.getTileEntity(x, y, z);
    if(!(te instanceof IPipingBundle)) {
      return blockIcon;
    }
    IPipingBundle pb = (IPipingBundle) te;
    int id = pb.getFacadeID();
    int meta = pb.getFacadeMetadata();
    if(id <= 0 || id == meta) {
      return blockIcon;
    }
    Block block = Block.getBlockById(id);
    if(block != null) {
      return block.getIcon(side, meta);
    }
    return blockIcon;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if(blockOverride != null) {
      return blockOverride.getIcon(par1, par2);
    }
    return blockIcon;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getBlockColor() {
    if(blockOverride != null) {
      return blockOverride.getBlockColor();
    } else {
      return super.getBlockColor();
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    if(blockOverride != null) {
      return blockOverride.colorMultiplier(par1IBlockAccess, par2, par3, par4);
    } else {
      return super.colorMultiplier(par1IBlockAccess, par2, par3, par4);
    }
  }

  public Block getIconOverrideBlock() {
    return blockOverride;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getRenderColor(int par1) {
    if(blockOverride != null) {
      return blockOverride.getRenderColor(par1);
    } else {
      return super.getRenderColor(par1);
    }
  }

  public void setBlockOverride(IPipingBundle cb) {
    if(cb == null) {
      blockOverride = null;
      return;
    }

    int id = cb.getFacadeID();
    int meta = cb.getFacadeMetadata();
    if(id <= 0 || id == meta) {
      return;
    }
    blockOverride = Block.getBlockById(id);
  }

  @Override
  public int getDamageValue(World par1World, int x, int y, int z) {
    Mimic m = getMimic(par1World, x, y, z);
    if(m != null) {
      return m.meta;
    }
    return 0;
  }

  private Mimic getMimic(IBlockAccess ba, int x, int y, int z) {
    TileEntity te = ba.getTileEntity(x, y, z);
    if(!(te instanceof IPipingBundle)) {
      // System.out.println("BlockConduitFacade.getMimic: Not a conduit bundle");
      return null;
    }
    IPipingBundle cb = (IPipingBundle) te;
    int id = cb.getFacadeID();
    int meta = cb.getFacadeMetadata();

    if(id <= 0) {
      return null;
    }

    return new Mimic(id, meta);
  }

  class Mimic {
    int id;
    int meta;
    Block block;

    private Mimic(int id, int meta) {
      super();
      this.id = id;
      this.meta = meta;
      this.block = Block.getBlockById(id);
    }

  }

}