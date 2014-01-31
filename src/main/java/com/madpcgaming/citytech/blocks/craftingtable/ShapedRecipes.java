package com.madpcgaming.citytech.blocks.craftingtable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


/**
 * A variant of ShapedRecipe that doesn't hardcode the size of the crafting grid.
 * This is to be used in conjunction with InventoryCraftingGrid but falls back to
 * vanilla's defaults if used otherwise.
 */
public class ShapedRecipes implements IRecipe {

        /** How many horizontal slots this recipe is wide. */
        public final int width;

        /** How many vertical slots this recipe uses. */
        public final int height;

        /** Is a array of ItemStack that composes the recipe. */
        public final ItemStack[] components;

        /** Is the ItemStack that you get when craft the recipe. */
        public final ItemStack result;
        
        /** I still don't know what this even is. */
        private boolean field_92101_f = false;

        public ShapedRecipes(int width, int height, ItemStack[] components, ItemStack result ) {
                this.width = width;
                this.height = height;
                this.components = components;
                this.result = result;
        }

        public ItemStack getRecipeOutput() {
                return this.result;
        }

        public boolean matches( InventoryCrafting ic, World w ) {
                int gridWidth;
                int gridHeight;
                
                if( ic instanceof InventoryCraftingGrid ) {
                        gridWidth = ((InventoryCraftingGrid)ic).getGridWidth( );
                        gridHeight = ((InventoryCraftingGrid)ic).getGridHeight( );
                }
                else
                        gridWidth = gridHeight = 3;
        
                for( int i = 0; i <= gridWidth - this.width; ++i ) {
                        for( int j = 0; j <= gridHeight - this.height; ++j ) {
                                if( this.checkMatch( ic, i, j, true ) )
                                        return true;

                                if( this.checkMatch( ic, i, j, false ) )
                                        return true;
                        }
                }
                return false;
        }

        private boolean checkMatch(InventoryCrafting paramInventoryCrafting, int paramInt1, int paramInt2, boolean paramBoolean) {
            for (int i = 0; i < 3; i++) {
              for (int j = 0; j < 3; j++) {
                int k = i - paramInt1;
                int m = j - paramInt2;
                ItemStack localItemStack1 = null;
                if ((k >= 0) && (m >= 0) && (k < this.width) && (m < this.height)) {
                  if (paramBoolean) localItemStack1 = this.components[(this.width - k - 1 + m * this.width)]; else
                    localItemStack1 = this.components[(k + m * this.width)];
                }
                ItemStack localItemStack2 = paramInventoryCrafting.getStackInRowAndColumn(i, j);
                if ((localItemStack2 != null) || (localItemStack1 != null))
                {
                  if (((localItemStack2 == null) && (localItemStack1 != null)) || ((localItemStack2 != null) && (localItemStack1 == null))) {
                    return false;
                  }
                  if (localItemStack1.getItem() != localItemStack2.getItem()) {
                    return false;
                  }
                  if ((localItemStack1.getItemDamage() != 32767) && (localItemStack1.getItemDamage() != localItemStack2.getItemDamage()))
                    return false;
                }
              }
            }
            return true;
          }


        public ItemStack getCraftingResult( InventoryCrafting ic )
        {
                ItemStack itemstack = this.getRecipeOutput().copy();

                if (this.field_92101_f)
                {
                        for (int i = 0; i < ic.getSizeInventory(); ++i)
                        {
                                ItemStack itemstack1 = ic.getStackInSlot(i);

                                if (itemstack1 != null && itemstack1.hasTagCompound())
                                {
                                        itemstack.setTagCompound((NBTTagCompound)itemstack1.stackTagCompound.copy());
                                }
                        }
                }

                return itemstack;
        }

        public int getRecipeSize( ) {
                return this.width * this.height;
        }

        public IRecipe func_92100_c( ) {
                this.field_92101_f = true;
                return this;
        }
}