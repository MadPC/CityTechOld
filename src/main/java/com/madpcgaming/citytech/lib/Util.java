package com.madpcgaming.citytech.lib;

import java.io.File;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class Util {


	  public static void giveExperience(EntityPlayer thePlayer, float experience) {
	    if(experience < 1.0F) {
	      int rndRound = MathHelper.floor_float(experience);
	      if(rndRound < MathHelper.ceiling_float_int(experience) && (float) Math.random() < experience) {
	        ++rndRound;
	      }
	      experience = rndRound;
	    }
	    int intExp = Math.round(experience);
	    while (intExp > 0) {
	      int j = EntityXPOrb.getXPSplit(intExp);
	      intExp -= j;
	      thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(thePlayer.worldObj, thePlayer.posX, thePlayer.posY + 0.5D, thePlayer.posZ + 0.5D, j));
	    }
	  }

	  // derived from ItemBlock.onItemUse
	  public static BlockCoord canPlaceItem(ItemStack itemUsed, int blockIdToBePlaced, EntityPlayer player, World world, int x, int y, int z, int side) {
	    Block i1 = world.getBlockId(x, y, z);

	    if(i1 == Blocks.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
	      side = 1;
	    } else if(i1 != Blocks.vine && i1 != Blocks.tallgrass && i1 != Blocks.deadbush
	        && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(world, x, y, z))) {

	      if(side == 0) {
	        --y;
	      } else if(side == 1) {
	        ++y;
	      } else if(side == 2) {
	        --z;
	      } else if(side == 3) {
	        ++z;
	      } else if(side == 4) {
	        --x;
	      } else if(side == 5) {
	        ++x;
	      }
	    }

	    if(itemUsed.stackSize == 0) {
	      return null;
	    } else if(!player.canPlayerEdit(x, y, z, side, itemUsed)) {
	      return null;
	    } else if(y == 255 && Block.blocksList[blockIdToBePlaced].blockMaterial.isSolid()) {
	      return null;
	    } else if(world.canPlaceEntityOnSide(blockIdToBePlaced, x, y, z, false, side, player, itemUsed)) {
	      return new BlockCoord(x, y, z);
	    }
	    return null;
	  }

	  public static void dropItems(World world, ItemStack stack, int x, int y, int z, boolean doRandomSpread) {
	    if(stack.stackSize <= 0) {
	      return;
	    }

	    if(doRandomSpread) {
	      float f1 = 0.7F;
	      double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
	      double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
	      double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
	      EntityItem entityitem = new EntityItem(world, x + d, y + d1, z + d2, stack);
	      entityitem.field_145804_b = 10;
	      world.spawnEntityInWorld(entityitem);
	    } else {
	      EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack);
	      entityitem.motionX = 0;
	      entityitem.motionY = 0;
	      entityitem.motionZ = 0;
	      entityitem.field_145804_b = 0;
	      world.spawnEntityInWorld(entityitem);
	    }

	  }

	  public static void dropItems(World world, IInventory inventory, int x, int y, int z, boolean doRandomSpread) {
	    for (int l = 0; l < inventory.getSizeInventory(); ++l) {
	      ItemStack items = inventory.getStackInSlot(l);

	      if(items != null && items.stackSize > 0) {
	        dropItems(world, inventory.getStackInSlot(l).copy(), x, y, z, doRandomSpread);
	      }
	    }
	  }

	  public static boolean dumpModObjects(File file) {
	    StringBuilder sb = new StringBuilder();
	    for (Block block : Block.blocksList) {
	      if(block != null) {
	        UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(block);
	        if(uid != null) {
	          sb.append(uid.modId);
	          sb.append(" ");
	          sb.append(uid.name);
	          sb.append("\n");
	        }
	      }
	    }
	    for (Item item : Item.itemsList) {
	      if(item != null && !(item instanceof ItemBlock)) {
	        UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(item);
	        if(uid != null) {
	          sb.append(uid.modId);
	          sb.append(" ");
	          sb.append(uid.name);
	          sb.append("\n");
	        }
	      }
	    }

	    try {
	      Files.write(sb, file, Charsets.UTF_8);
	      return true;
	    } catch (IOException e) {
	      Log.warn("Error dumping ore dictionary entries: " + e.getMessage());
	      e.printStackTrace();
	      return false;
	    }
	  }

	  public static boolean dumpOreNames(File file) {

	    try {
	      String[] oreNames = OreDictionary.getOreNames();
	      Files.write(Joiner.on("\n").join(oreNames), file, Charsets.UTF_8);
	      return true;
	    } catch (IOException e) {
	      Log.warn("Error dumping ore dictionary entries: " + e.getMessage());
	      e.printStackTrace();
	      return false;
	    }
	  }

	  public static ItemStack decrStackSize(IInventory inventory, int slot, int size) {
	    ItemStack item = inventory.getStackInSlot(slot);
	    if(item != null) {
	      if(item.stackSize <= size) {
	        ItemStack result = item;
	        inventory.setInventorySlotContents(slot, null);
	        inventory.onInventoryChanged();
	        return result;
	      }
	      ItemStack split = item.splitStack(size);
	      if(item.stackSize == 0) {
	        inventory.setInventorySlotContents(slot, null);
	      }
	      inventory.onInventoryChanged();
	      return split;
	    }
	    return null;
	  }

	}