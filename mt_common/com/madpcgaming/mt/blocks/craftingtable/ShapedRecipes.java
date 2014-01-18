package com.madpcgaming.mt.blocks.craftingtable;

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
                                if( this.checkMatch( ic, i, j, gridWidth, gridHeight, true ) )
                                        return true;

                                if( this.checkMatch( ic, i, j, gridWidth, gridHeight, false ) )
                                        return true;
                        }
                }
                return false;
        }

        private boolean checkMatch( InventoryCrafting ic, int par2, int par3, int gridWidth, int gridHeight, boolean par4 ) {
                for( int k = 0; k < gridWidth; ++k ) {
                        for (int l = 0; l < gridHeight; ++l ) {
                                int i1 = k - par2;
                                int j1 = l - par3;
                                ItemStack itemstack = null;

                                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height)
                                {
                                        if (par4)
                                                itemstack = this.components[this.width - i1 - 1 + j1 * this.width];
                                        else
                                                itemstack = this.components[i1 + j1 * this.width];
                                }

                                ItemStack itemstack1 = ic.getStackInRowAndColumn(k, l);

                                if (itemstack1 != null || itemstack != null)
                                {
                                        if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
                                                return false;
                                        if (itemstack.itemID != itemstack1.itemID)
                                                return false;
                                        if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
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