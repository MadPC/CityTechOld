package com.madpcgaming.citytech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
	public static Block	itemItemPiping;
	public static Block	itemBasicTelsaBat;
	public static Block	blockCapacitorBank;
	public static Block	itemPowerPiping;
	public static Block	blockPipingBundle;
	
	//Piping

	public static void init()
	{

		/* Initialize each mod item individually */
		AluminiumOre = new BlockCT(Strings.ALUMINIUM_ORE_NAME, Material.rock, 5.0F);
		CopperOre = new BlockCT(Strings.COPPER_ORE_NAME);
		PalladiumOre = new BlockCT(Strings.PALLADIUM_ORE_NAME);
		PlatinumOre = new BlockCT(Strings.PLATINUM_ORE_NAME);
		SilverOre = new BlockCT(Strings.SILVER_ORE_NAME);
		TinOre = new BlockCT(Strings.TIN_ORE_NAME);
		SiliconStill = new SiliconLiquidStill();
		SiliconFlowing = new SiliconLiquidFlowing();
		CableCopper = new SimpleCable();
		CraftingTable = new TechocratsWorkbench();
		AluminiumBlock = new BlockCT(Strings.ALUMINIUM_BLOCK_NAME, Material.iron, 5.0F);
		CopperBlock = new BlockCT(Strings.COPPER_BLOCK_NAME, Material.iron, 5.0F);
		PalladiumBlock = new BlockCT(Strings.PALLADIUM_BLOCK_NAME, Material.iron, 5.0F);
		PlatinumBlock = new BlockCT(Strings.PLATINUM_BLOCK_NAME, Material.iron, 5.0F);
		SilverBlock = new BlockCT(Strings.SILVER_BLOCK_NAME, Material.iron, 5.0F);
		TinBlock = new BlockCT(Strings.TIN_BLOCK_NAME, Material.iron, 5.0F);
		ReinforcedGlass = new BlockCT(Strings.REINFORCED_GLASS_NAME, Material.glass, 1.0F, 2000.0F);
		ReinforcedStone = new BlockCT(Strings.REINFORCED_STONE_NAME, Material.rock, 1.0F, 2000.0F);
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
		GameRegistry.registerBlock(SiliconFlowing, Strings.SILICON_LIQUID_FLOWING);
		GameRegistry.registerBlock(SiliconStill, Strings.SILICON_LIQUID_STILL);
		
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
