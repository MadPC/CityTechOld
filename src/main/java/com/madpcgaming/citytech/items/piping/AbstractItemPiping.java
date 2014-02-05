package com.madpcgaming.citytech.items.piping;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.BlockCoord;
import com.madpcgaming.citytech.lib.Util;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class AbstractItemPiping extends Item implements IPipingItem {

  protected ModObject modObj;

  protected ItemPipingSubtype[] subtypes;

  protected Icon[] icons;

  protected AbstractItemPiping(ModObject modObj) {
    super(modObj.id);
    this.modObj = modObj;
    setCreativeTab(CityTech.tabsCT);
    setUnlocalizedName(modObj.unlocalisedName);
    setMaxStackSize(64);
    setHasSubtypes(true);
  }

  protected void init(ItemPipingSubtype[] subtypes) {
    this.subtypes = subtypes;
    icons = new Icon[subtypes.length];
    GameRegistry.registerItem(this, modObj.unlocalisedName);
    for (ItemPipingSubtype subtype : subtypes) {
      LanguageRegistry.instance().addStringLocalization(getUnlocalizedName() + "." + subtype.unlocalisedName + ".name", subtype.uiName);
    }

  }

  @Override
  public void registerIcons(IIconRegister iconRegister) {
    int index = 0;
    for (ItemPipingSubtype subtype : subtypes) {
      icons[index] = iconRegister.registerIcon(subtype.iconKey);
      index++;
    }
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

    BlockCoord placeAt = Util.canPlaceItem(stack, ModObject.blockConduitBundle.actualId, player, world, x, y, z, side);
    if(placeAt != null) {
      if(!world.isRemote) {
        if(world.setBlock(placeAt.x, placeAt.y, placeAt.z, ModObject.blockConduitBundle.actualId, 0, 1)) {
          IPipingBundle bundle = (IPipingBundle) world.func_147438_o(placeAt.x, placeAt.y, placeAt.z);
          if(bundle != null) {
            bundle.addConduit(createPiping(stack));
            Block b = ModBlocks.blockConduitBundle;
            world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, b.field_149762_H.func_150495_a(),
                (b.field_149762_H.func_150497_c() + 1.0F) / 2.0F, b.field_149762_H.func_150494_d() * 0.8F);
          }
        }
      }
      if(!player.capabilities.isCreativeMode) {
        stack.stackSize--;
      }
      return true;

    } else {

      ForgeDirection dir = ForgeDirection.values()[side];
      int placeX = x + dir.offsetX;
      int placeY = y + dir.offsetY;
      int placeZ = z + dir.offsetZ;

      if(world.getBlockId(placeX, placeY, placeZ) == ModObject.blockConduitBundle.actualId) {

        IPipingBundle bundle = (TilePipingBundle) world.func_147438_o(placeX, placeY, placeZ);
        if(bundle == null) {
          System.out.println("AbstractItemConduit.onItemUse: Bundle null");
          return false;
        }
        IPiping con = createPiping(stack);
        if(con == null) {
          System.out.println("AbstractItemConduit.onItemUse: Conduit null.");
          return false;
        }
        if(bundle.getPiping(con.getBasePipingType()) == null) {
          if(!world.isRemote) {
            bundle.addConduit(con);
            if(!player.capabilities.isCreativeMode) {
              stack.stackSize--;
            }
          }
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public Icon getIconFromDamage(int damage) {
    damage = MathHelper.clamp_int(damage, 0, subtypes.length - 1);
    return icons[damage];
  }

  @Override
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, subtypes.length - 1);
    return super.getUnlocalizedName() + "." + subtypes[i].unlocalisedName;

  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int j = 0; j < subtypes.length; ++j) {
      par3List.add(new ItemStack(par1, 1, j));
    }
  }

}