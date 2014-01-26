package com.madpcgaming.madtech.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.madtech.blocks.ModBlocks;
import com.madpcgaming.madtech.lib.ItemIds;
import com.madpcgaming.madtech.lib.Materials;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems
{
	public static Item	AluminiumIngot;
	public static Item	CopperIngot;
	public static Item	TinIngot;
	public static Item	PlatinumIngot;
	public static Item	PalladiumIngot;
	public static Item	SilverIngot;
	public static Item	Wiring;
	public static Item	Silicon;
	public static Item	Rubber;
	public static Item	wireTester;
	public static Item	Wrench;
	public static Item	EnergyReader;
	public static Item	CyberSword;

	public static void init()
	{

		/* Initialize each mod item individually */
		AluminiumIngot = new AluminumIngot(ItemIds.ALUMINIUM_INGOT);
		CopperIngot = new CopperIngot(ItemIds.COPPER_INGOT);
		TinIngot = new TinIngot(ItemIds.TIN_INGOT);
		PlatinumIngot = new PlatinumIngot(ItemIds.PLATINUM_INGOT);
		PalladiumIngot = new PalladiumIngot(ItemIds.PALLADIUM_INGOT);
		SilverIngot = new SilverIngot(ItemIds.SILVER_INGOT);
		Wiring = new Wiring(ItemIds.WIRING_ITEM);
		Silicon = new Silicon(ItemIds.SILICON_ITEM);
		Rubber = new Rubber(ItemIds.RUBBER_ITEM);
		Wrench = new Wrench(ItemIds.WRENCH_ITEM);
		EnergyReader = new EnergyReader(ItemIds.ENERGYREADER);
		CyberSword = new CySword(ModItems.CyberSword, Materials.cyberEnergeticMaterial);

		// Hardcoded due to DEBUG purposes only!
		wireTester = new WireTester(ItemIds.RUBBER_ITEM + 1);
		GameRegistry.addSmelting(ModBlocks.SilverOre,
				new ItemStack(SilverIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.AluminiumOre, new ItemStack(
				AluminiumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.CopperOre,
				new ItemStack(CopperIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.PalladiumOre, new ItemStack(
				PalladiumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.PlatinumOre, new ItemStack(
				PlatinumIngot), 0.7F);
		GameRegistry.addSmelting(ModBlocks.TinOre, new ItemStack(TinIngot),
				0.7F);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.AluminiumBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', AluminiumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(AluminiumIngot, 9),
				new Object[] { ModBlocks.AluminiumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.CopperBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', CopperIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(CopperIngot, 9),
				new Object[] { ModBlocks.CopperBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.PalladiumBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', PalladiumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(PalladiumIngot, 9),
				new Object[] { ModBlocks.PalladiumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.PlatinumBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', PlatinumIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(PlatinumIngot, 9),
				new Object[] { ModBlocks.PlatinumBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.SilverBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', SilverIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(SilverIngot, 9),
				new Object[] { ModBlocks.SilverBlock });
		GameRegistry.addRecipe(new ItemStack(ModBlocks.TinBlock, 1),
				new Object[] { "DDD", "DDD", "DDD", 'D', TinIngot });
		GameRegistry.addShapelessRecipe(new ItemStack(TinIngot, 9),
				new Object[] { ModBlocks.TinBlock });

		initOreDictionaryRegister();
	}

	public static void initOreDictionaryRegister()
	{
		OreDictionary
				.registerOre("ingotAluminum", new ItemStack(AluminiumIngot));
		OreDictionary.registerOre("ingotCopper", new ItemStack(CopperIngot));
		OreDictionary.registerOre("ingotTin", new ItemStack(TinIngot));
		OreDictionary
				.registerOre("ingotPlatinum", new ItemStack(PlatinumIngot));
		OreDictionary.registerOre("ingotPalladium", new ItemStack(
				PalladiumIngot));
		OreDictionary.registerOre("silverIngot", new ItemStack(SilverIngot));
		OreDictionary.registerOre("itemRubber", new ItemStack(Rubber));
	}
}
