package com.madpcgaming.madtech.blocks.craftingtable;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

/**
 * An extension to InventoryCrafting that allows getting the size of the
 * crafting grid without reflection.
 */
public class InventoryCraftingGrid extends InventoryCrafting {

	private int width;
	private int height;

	public InventoryCraftingGrid(Container c, int width, int height) {
		super(c, width, height);
		this.width = width;
		this.height = height;
	}

	public int getGridWidth() {
		return this.width;
	}

	public int getGridHeight() {
		return this.height;
	}
}