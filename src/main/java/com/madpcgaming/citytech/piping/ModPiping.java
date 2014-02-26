package com.madpcgaming.citytech.piping;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.madpcgaming.citytech.piping.power.ItemPowerPiping;

public class ModPiping
{
	public static Block blockPipingFacade;
	public static BlockPipingBundle blockPipingBundle;
	public static ItemPowerPiping itemPowerPiping;
	public static Item itemPipingFacade;
	public static Item itemRedstonePiping;
	public static Item itemItemPiping;
	
	public static void init()
	{
		blockPipingBundle = BlockPipingBundle.create();
		itemPowerPiping = ItemPowerPiping.create();
	}

}
