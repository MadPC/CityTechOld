package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.citytech.blocks.liquids.SiliconLiquidFlowing;
import com.madpcgaming.citytech.blocks.liquids.SiliconLiquidStill;
import com.madpcgaming.citytech.blocks.multiblocks.IndustrialFurnaceCore;
import com.madpcgaming.citytech.blocks.multiblocks.IndustrialFurnaceDummy;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.tileentitys.CableTE;
import com.madpcgaming.citytech.tileentitys.DrainTE;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceCore;
import com.madpcgaming.citytech.tileentitys.TileEntityIndustrialFurnaceDummy;
import com.madpcgaming.citytech.tileentitys.TileSimpleEFurnace;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{

	public static Block	AluminiumOre;
	public static Block	CopperOre;
	public static Block	PalladiumOre;
	public static Block	PlatinumOre;
	public static Block	SilverOre;
	public static Block	TinOre;
	public static Block	SiliconStill;
	public static Block	SiliconFlowing;
	public static Block	CableCopper;
	public static Block	BlockElectrolyser;
	public static Block	IndustrialFurnaceCore;
	public static Block	IndustrialFurnaceDummy;
	public static Block	AluminiumBlock;
	public static Block	CopperBlock;
	public static Block	PalladiumBlock;
	public static Block	PlatinumBlock;
	public static Block	SilverBlock;
	public static Block	TinBlock;
	public static Block	ReinforcedStone;
	public static Block	ReinforcedGlass;
	public static Block Lamp;
	public static Block CraftingTable;
	public static Block SimpleFurnace;
	public static Block SimpleMacerator;
	/**
	 * Hard coded
	 * 
	 * @see com.madpcgaming.mt.items.WireTester
	 */
	public static Block	SuperDrain;
	public static Block	blockConduitBundle;

	public static void init()
	{

		/* Initialize each mod item individually */
		AluminiumOre = new AluminiumOre(BlockIds.ALUMINUM_ORE);
		CopperOre = new CopperOre(BlockIds.COPPER_ORE);
		PalladiumOre = new PalladiumOre(BlockIds.PALLADIUM_ORE);
		PlatinumOre = new PlatinumOre(BlockIds.PLATINUM_ORE);
		SilverOre = new SilverOre(BlockIds.SILVER_ORE);
		TinOre = new TinOre(BlockIds.TIN_ORE);
		SiliconStill = new SiliconLiquidStill(BlockIds.SILICON_LIQUID_STILL);
		SiliconFlowing = new SiliconLiquidFlowing(BlockIds.SILICON_LIQUID_STILL - 1);
		CableCopper = new SimpleCable(BlockIds.COPPER_CABLE);
		CraftingTable = new TechocratsWorkbench(BlockIds.CRAFTING_TABLE);
		AluminiumBlock = new AluminiumBlock(BlockIds.ALUMINUM_BLOCK);
		CopperBlock = new CopperBlock(BlockIds.COPPER_BLOCK);
		PalladiumBlock = new PalladiumBlock(BlockIds.PALLADIUM_BLOCK);
		PlatinumBlock = new PlatinumBlock(BlockIds.PLATINUM_BLOCK);
		SilverBlock = new SilverBlock(BlockIds.SILVER_BLOCK);
		TinBlock = new TinBlock(BlockIds.TIN_BLOCK);
		ReinforcedGlass = new ReinforcedGlass(BlockIds.REINFORCED_GLASS);
		ReinforcedStone = new ReinforcedStone(BlockIds.REINFORCED_STONE);
		SuperDrain = new SuperDrain(BlockIds.COPPER_CABLE + 1);
		//BlockElectrolyser = new BlockElectrolyser(BlockIds.BLOCK_ELECTROLYSER);
		IndustrialFurnaceCore = new IndustrialFurnaceCore(BlockIds.BLOCK_INDUSTRIAL_FURNACE_CORE);
		IndustrialFurnaceDummy = new IndustrialFurnaceDummy(BlockIds.BLOCK_INDUSTRIAL_FURNACE_DUMMY);
		SimpleFurnace = new SimpleEFurnace(BlockIds.BLOCK_SE_FURNACE);
		
		//GameRegistery stuff
		GameRegistry.registerBlock(AluminiumOre, Strings.ALUMINIUM_ORE_NAME);
		GameRegistry.registerBlock(CopperOre, Strings.COPPER_ORE_NAME);
		GameRegistry.registerBlock(PalladiumOre, Strings.PALLADIUM_ORE_NAME);
		GameRegistry.registerBlock(PlatinumOre, Strings.PLATINUM_ORE_NAME);
		GameRegistry.registerBlock(SilverOre, Strings.SILVER_ORE_NAME);
		GameRegistry.registerBlock(TinOre, Strings.TIN_ORE_NAME);
		GameRegistry.registerBlock(ReinforcedGlass,	Strings.REINFORCED_GLASS_NAME);
		GameRegistry.registerBlock(ReinforcedStone,	Strings.REINFORCED_STONE_NAME);
		GameRegistry.registerBlock(CableCopper, Strings.COPPER_CABLE_NAME);
		GameRegistry.registerBlock(SuperDrain, "DEBUG_SUPER_DRAIN");
		//GameRegistry.registerBlock(BlockElectrolyser, Strings.ELECTROLYSER_NAME);
		GameRegistry.registerBlock(IndustrialFurnaceCore, Strings.FURNACECORE_NAME);
		GameRegistry.registerBlock(IndustrialFurnaceDummy, Strings.FURNACEDUMMY_NAME);
		GameRegistry.registerBlock(AluminiumBlock, Strings.ALUMINIUM_BLOCK_NAME);
		GameRegistry.registerBlock(CopperBlock, Strings.COPPER_BLOCK_NAME);
		GameRegistry.registerBlock(PalladiumBlock, Strings.PALLADIUM_BLOCK_NAME);
		GameRegistry.registerBlock(PlatinumBlock, Strings.PLATINUM_BLOCK_NAME);
		GameRegistry.registerBlock(SilverBlock, Strings.SILVER_BLOCK_NAME);
		GameRegistry.registerBlock(TinBlock, Strings.TIN_BLOCK_NAME);
		GameRegistry.registerBlock(SimpleFurnace, Strings.SIMPLE_SE_FURNACE_NAME);
		GameRegistry.registerBlock(CraftingTable, Strings.WORKBENCH_NAME);
		
		//Tile Entities
		GameRegistry.registerTileEntity(CableTE.class, "MTcableCopper");
		GameRegistry.registerTileEntity(DrainTE.class, "MTsuperDrain");
		//GameRegistry.registerTileEntity(TileElectrolyser.class, Strings.ELECTROLYSER_NAME);
		GameRegistry.registerTileEntity(TileEntityIndustrialFurnaceCore.class, Strings.FURNACECORE_NAME);
		GameRegistry.registerTileEntity(TileEntityIndustrialFurnaceDummy.class, Strings.FURNACEDUMMY_NAME);
		GameRegistry.registerTileEntity(TileSimpleEFurnace.class, Strings.SIMPLE_SE_FURNACE_NAME);

		initOreDictionaryRegister();
		initFluidDictionary();

	}

	private static void initOreDictionaryRegister()
	{
		OreDictionary.registerOre("oreAluminum", AluminiumOre);
		OreDictionary.registerOre("oreCopper", CopperOre);
		OreDictionary.registerOre("orePalladium", PalladiumOre);
		OreDictionary.registerOre("orePlatinum", PlatinumOre);
		OreDictionary.registerOre("oreSilver", SilverOre);
		OreDictionary.registerOre("oreTin", TinOre);
	}

	private static void initFluidDictionary()
	{
		// Method to get or create a liquid     Name    A special itemStack Block/Item/ID   a special constant specifying the "base" volume
		// LiquidDictionary.getOrCreateLiquid("Silicon", new FluidStack(SiliconStill, FluidContainerRegistry.BUCKET_VOLUME));

	}

}
