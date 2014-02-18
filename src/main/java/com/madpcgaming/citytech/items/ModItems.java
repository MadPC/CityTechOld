package com.madpcgaming.citytech.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.Materials;
import com.madpcgaming.citytech.lib.Strings;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems
{
	public static Item AluminiumIngot;
	public static Item CopperIngot;
	public static Item TinIngot;
	public static Item PlatinumIngot;
	public static Item PalladiumIngot;
	public static Item SilverIngot;
	public static Item Wiring;
	public static Item Silicon;
	public static Item Rubber;
	public static Item wireTester;
	public static Item Wrench;
	public static Item EnergyReader;
	public static Item CyberSword;
	public static Item itemPipingFacade;
	

	public static void init()
	{
		/* Initialize each mod item individually */
		AluminiumIngot = new ItemMT(Strings.ALUMINUM_INGOT_NAME);
		CopperIngot = new ItemMT(Strings.COPPER_INGOT_NAME);
		TinIngot = new ItemMT(Strings.TIN_INGOT_NAME);
		PlatinumIngot = new ItemMT(Strings.PLATINUM_INGOT_NAME);
		PalladiumIngot = new ItemMT(Strings.PALLADIUM_INGOT_NAME);
		SilverIngot = new ItemMT(Strings.SILVER_INGOT_NAME);
		Wiring = new ItemMT(Strings.WIRING_ITEM_NAME);
		Silicon = new ItemMT(Strings.SILICON_ITEM_NAME);
		Rubber = new ItemMT(Strings.RUBBER_ITEM_NAME);
		Wrench = new ItemMT(Strings.WRENCH_ITEM_NAME);
		EnergyReader = new EnergyReader();
		CyberSword = new CySword(Materials.cyberEnergeticMaterial);

		//itemItemPiping = ItemItemPiping.create();
		//itemLiquidPiping = ItemLiquidPiping.create();
		//itemRedstonePiping = ItemRedstonePiping.create();
		

		// Hardcoded due to DEBUG purposes only!
		wireTester = new ItemMT("test");
		
		registerItems();
		addRecipes();
		initOreDictionaryRegister();
	}
	
	private static void registerItems()
	{
		GameRegistry.registerItem(AluminiumIngot, Strings.ALUMINUM_INGOT_NAME);
		GameRegistry.registerItem(CopperIngot, Strings.COPPER_INGOT_NAME);
		GameRegistry.registerItem(TinIngot, Strings.TIN_INGOT_NAME);
		GameRegistry.registerItem(PlatinumIngot, Strings.PLATINUM_INGOT_NAME);
		GameRegistry.registerItem(PalladiumIngot, Strings.PALLADIUM_INGOT_NAME);
		GameRegistry.registerItem(SilverIngot, Strings.SILVER_INGOT_NAME);
		GameRegistry.registerItem(Wiring, Strings.WIRING_ITEM_NAME);
		GameRegistry.registerItem(Silicon, Strings.SILICON_ITEM_NAME);
		GameRegistry.registerItem(Rubber, Strings.RUBBER_ITEM_NAME);
		GameRegistry.registerItem(Wrench, Strings.WRENCH_ITEM_NAME);
		GameRegistry.registerItem(EnergyReader, Strings.ENERGYREADER_ITEM_NAME);
		GameRegistry.registerItem(CyberSword, Strings.CYBERSWORD_ITEM_NAME);
		GameRegistry.registerItem(wireTester, "test");
	}
	
	private static void addRecipes()
	{
		GameRegistry.addSmelting(ModBlocks.SilverOre, new ItemStack(SilverIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.AluminiumOre, new ItemStack(AluminiumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.CopperOre, new ItemStack(CopperIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.PalladiumOre, new ItemStack(PalladiumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.PlatinumOre, new ItemStack(PlatinumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.TinOre, new ItemStack(TinIngot), 0.7F);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.AluminiumBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', AluminiumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(AluminiumIngot, 9), new Object[] { ModBlocks.AluminiumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.CopperBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', CopperIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(CopperIngot, 9), new Object[] { ModBlocks.CopperBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.PalladiumBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', PalladiumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(PalladiumIngot, 9), new Object[] { ModBlocks.PalladiumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.PlatinumBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', PlatinumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(PlatinumIngot, 9), new Object[] { ModBlocks.PlatinumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.SilverBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', SilverIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(SilverIngot, 9), new Object[] { ModBlocks.SilverBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.TinBlock, 1), new Object[] { "DDD", "DDD", "DDD", 'D', TinIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(TinIngot, 9), new Object[] { ModBlocks.TinBlock });
	}

	public static void initOreDictionaryRegister()
	{
		OreDictionary.registerOre("ingotAluminum", new ItemStack(AluminiumIngot));
		OreDictionary.registerOre("ingotCopper", new ItemStack(CopperIngot));
		OreDictionary.registerOre("ingotTin", new ItemStack(TinIngot));
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(PlatinumIngot));
		OreDictionary.registerOre("ingotPalladium", new ItemStack(PalladiumIngot));
		OreDictionary.registerOre("silverIngot", new ItemStack(SilverIngot));
		OreDictionary.registerOre("itemRubber", new ItemStack(Rubber));
	}
}
