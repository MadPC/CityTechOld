package com.madpcgaming.mt.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.mt.blocks.liquids.SiliconLiquidFlowing;
import com.madpcgaming.mt.blocks.liquids.SiliconLiquidStill;
import com.madpcgaming.mt.lib.BlockIds;
import com.madpcgaming.mt.lib.Strings;
import com.madpcgaming.mt.tileentitys.CableTE;
import com.madpcgaming.mt.tileentitys.DrainTE;
import com.madpcgaming.mt.tileentitys.TileElectrolyser;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.mt.tileentitys.TileEntityIndustrialFurnaceDummy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModBlocks
{
	
	public static Block	AluminumOre;
	public static Block	CopperOre;
	public static Block	PalladiumOre;
	public static Block	PlatinumOre;
	public static Block	SilverOre;
	public static Block	TinOre;
	public static Block	SiliconStill;
	public static Block	SiliconFlowing;
	public static Block	CableCopper;
	public static Block	BlockElectrolyser;
	public static Block IndustrialFurnaceCore;
	public static Block IndustrialFurnaceDummy;
	
	/**
	 * Hard coded
	 * 
	 * @see com.madpcgaming.mt.items.WireTester
	 */
	public static Block	SuperDrain;
	
	public static void init()
	{
		
		/* Initialize each mod item individually */
		AluminumOre = new AluminumOre(BlockIds.ALUMINUM_ORE);
		CopperOre = new CopperOre(BlockIds.COPPER_ORE);
		PalladiumOre = new PalladiumOre(BlockIds.PALLADIUM_ORE);
		PlatinumOre = new PlatinumOre(BlockIds.PLATINUM_ORE);
		SilverOre = new SilverOre(BlockIds.SLIVER_ORE);
		TinOre = new TinOre(BlockIds.TIN_ORE);
		SiliconStill = new SiliconLiquidStill(BlockIds.SILICON_LIQUID_STILL);
		SiliconFlowing = new SiliconLiquidFlowing(BlockIds.SILICON_LIQUID_STILL - 1);
		CableCopper = new SimpleCable(BlockIds.COPPER_CABLE);
		
		SuperDrain = new SuperDrain(BlockIds.COPPER_CABLE + 1);
		
		BlockElectrolyser = new BlockElectrolyser(BlockIds.BLOCK_ELECTROLYSER);
		IndustrialFurnaceCore = new BlockIndustrialFurnaceCore(BlockIds.BLOCK_INDUSTRIAL_FURNACE_CORE);
		IndustrialFurnaceDummy = new BlockIndustrialFurnaceDummy(BlockIds.BLOCK_INDUSTRIAL_FURNACE_DUMMY);
		
		GameRegistry.registerBlock(AluminumOre, Strings.ALUMINUM_ORE_NAME);
		GameRegistry.registerBlock(CopperOre, Strings.COPPER_ORE_NAME);
		GameRegistry.registerBlock(PalladiumOre, Strings.PALLADIUM_ORE_NAME);
		GameRegistry.registerBlock(PlatinumOre, Strings.PLATINUM_ORE_NAME);
		GameRegistry.registerBlock(SilverOre, Strings.SILVER_ORE_NAME);
		GameRegistry.registerBlock(TinOre, Strings.TIN_ORE_NAME);
		GameRegistry.registerBlock(CableCopper, Strings.COPPER_CABLE_NAME);
		
		GameRegistry.registerBlock(SuperDrain, "DEBUG_SUPER_DRAIN");
		GameRegistry.registerBlock(BlockElectrolyser, Strings.ELECTROLYSER_NAME);
		GameRegistry.registerBlock(IndustrialFurnaceCore, Strings.FURNACECORE_NAME);
		GameRegistry.registerBlock(IndustrialFurnaceDummy, Strings.FURNACEDUMMY_NAME);
		// GameRegistry.registerBlock(SiliconFlowing,
		// Strings.SILICON_LIQUID_FLOWING);
		// GameRegistry.registerBlock(SiliconStill,
		// Strings.SILICON_LIQUID_STILL);
		
		GameRegistry.registerTileEntity(CableTE.class, "MTcableCopper");
		GameRegistry.registerTileEntity(DrainTE.class, "MTsuperDrain");
		GameRegistry.registerTileEntity(TileElectrolyser.class, Strings.ELECTROLYSER_NAME);
		GameRegistry.registerTileEntity(TileEntityIndustrialFurnaceCore.class, Strings.FURNACECORE_NAME);
		GameRegistry.registerTileEntity(TileEntityIndustrialFurnaceDummy.class, Strings.FURNACEDUMMY_NAME);
		LanguageRegistry.instance().addStringLocalization("itemGroup.mt", "en_US", "MadTech");
		
		initOreDictionaryRegister();
		initFluidDictionary();
		
	}
	
	private static void initOreDictionaryRegister()
	{
		OreDictionary.registerOre("oreAluminum", AluminumOre);
		OreDictionary.registerOre("oreCopper", CopperOre);
		OreDictionary.registerOre("orePalladium", PalladiumOre);
		OreDictionary.registerOre("orePlatinum", PlatinumOre);
		OreDictionary.registerOre("oreSilver", SilverOre);
		OreDictionary.registerOre("oreTin", TinOre);
	}
	
	private static void initFluidDictionary()
	{
		// Method to get or create a liquid Name A special itemStack
		// Block/Item/ID a special constant specifying the "base" volume
		// LiquidDictionary.getOrCreateLiquid("Silicon", new
		// FluidStack(SiliconStill, FluidContainerRegistry.BUCKET_VOLUME));
		/**
		 * TODO: Figure out if the FluidRegister is to be used instead since
		 * there is no FluidDictionary
		 */
		
	}
	
}
