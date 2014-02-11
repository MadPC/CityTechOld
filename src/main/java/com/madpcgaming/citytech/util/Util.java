package com.madpcgaming.citytech.util;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
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

import java.io.File;
import java.io.IOException;

public class Util {
    public static Block getBlock(String name) {
        if (name.isEmpty())
            return null;

        return GameData.blockRegistry.get(name);
    }

    public static int getBlockIDFromName(String name) {
        return GameData.blockRegistry.getId(name);
    }

    public static Item getItem(String name) {
        if (name.isEmpty())
            return null;

        return GameData.itemRegistry.get(name);
    }

    public static int getItemIDFromName(String name) {
        return GameData.itemRegistry.getId(name);
    }

    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            } else {
                return null;
            }
        } else {
            stack.splitStack(1);
            return stack;
        }
    }

    public static void giveExperience(EntityPlayer player, float xp) {
        if (xp < 1.0F) {
            int randRound = MathHelper.floor_float(xp);
            if (randRound < MathHelper.ceiling_float_int(xp) && (float)Math.random() < xp) {
                ++randRound;
            }

            xp = randRound;
        }

        int exp = Math.round(xp);

        while (exp > 0) {
            int i = EntityXPOrb.getXPSplit(exp);
            exp -= i;
            player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, i));
        }
    }

    public static BlockCoord canPlaceItem(ItemStack stack, int id, EntityPlayer player, World world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);
        String blockName = block.getUnlocalizedName();

        if (block == Blocks.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && (GameData.blockRegistry.get(blockName) == null || !GameData.blockRegistry.get(blockName).canPlaceBlockAt(world, x, y, z)) ) {
            if (side == 0) {
                --y;
            } else if (side == 1) {
                ++y;
            } else if (side == 2) {
                --z;
            } else if (side == 3) {
                ++z;
            } else if (side == 4) {
                --x;
            } else if (side == 5) {
                ++x;
            }
        }

        if (stack.stackSize == 0)
            return null;
        else if (!player.canPlayerEdit(x, y, z, side, stack))
            return null;
        else if (y == 255 && GameData.blockRegistry.get(blockName).getMaterial().isSolid())
            return null;
        else if (!world.canPlaceEntityOnSide(block, x, y, z, false, side, player, stack))
            return null;

        return new BlockCoord(x, y, z);
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
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        } else {
            EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack);
            entityitem.motionX = 0;
            entityitem.motionY = 0;
            entityitem.motionZ = 0;
            entityitem.delayBeforeCanPickup = 0;
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
        for (Object block : GameData.blockRegistry) {
            if(((Block)block) != null) {
                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(((Block)block));
                if(uid != null) {
                    sb.append(uid.modId);
                    sb.append(" ");
                    sb.append(uid.name);
                    sb.append("\n");
                }
            }
        }
        for (Object item : GameData.itemRegistry) {
            if(((Item)item) != null && !(((Item)item) instanceof ItemBlock)) {
                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(((Item)item));
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
            FMLLog.fine("Error dumping ore dictionary entries: " + e.getMessage());
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
            FMLLog.fine("Error dumping ore dictionary entries: " + e.getMessage());
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
                inventory.markDirty();
                return result;
            }
            ItemStack split = item.splitStack(size);
            if(item.stackSize == 0) {
                inventory.setInventorySlotContents(slot, null);
            }
            inventory.markDirty();
            return split;
        }
        return null;
    }
}