package com.madpcgaming.mt.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.madpcgaming.mt.lib.ItemIds;

public class ModItems
{
	public static AluminumIngot		AluminumIngot;
	public static CopperIngot		CopperIngot;
	public static TinIngot			TinIngot;
	public static PlatinumIngot		PlatinumIngot;
	public static PalladiumIngot	PalladiumIngot;
	public static SilverIngot		SilverIngot;
	public static Wiring			Wiring;
	public static Silicon			Silicon;
	public static Rubber			Rubber;
	public static Item				wireTester;
	public static Wrench			Wrench;
	
	
	public static void init()
	{
		
		/* Initialize each mod item individually */
		AluminumIngot = new AluminumIngot(ItemIds.ALUMINIUM_INGOT);
		CopperIngot = new CopperIngot(ItemIds.COPPER_INGOT);
		TinIngot = new TinIngot(ItemIds.TIN_INGOT);
		PlatinumIngot = new PlatinumIngot(ItemIds.PLATINUM_INGOT);
		PalladiumIngot = new PalladiumIngot(ItemIds.PALLADIUM_INGOT);
		SilverIngot = new SilverIngot(ItemIds.SILVER_INGOT);
		Wiring = new Wiring(ItemIds.WIRING_ITEM);
		Silicon = new Silicon(ItemIds.SILICON_ITEM);
		Rubber = new Rubber(ItemIds.RUBBER_ITEM);
		Wrench = new Wrench(ItemIds.WRENCH_ITEM);
			
		// Hardcoded due to DEBUG purposes only!
		wireTester = new WireTester(ItemIds.RUBBER_ITEM + 1);
		
		initOreDictionaryRegister();
	}
	
	public static void initOreDictionaryRegister()
	{
		OreDictionary.registerOre("ingotAluminum", new ItemStack(AluminumIngot));
		OreDictionary.registerOre("ingotCopper", new ItemStack(CopperIngot));
		OreDictionary.registerOre("ingotTin", new ItemStack(TinIngot));
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(PlatinumIngot));
		OreDictionary.registerOre("ingotPalladium", new ItemStack(PalladiumIngot));
		OreDictionary.registerOre("silverIngot", new ItemStack(SilverIngot));
		OreDictionary.registerOre("itemRubber", new ItemStack(Rubber));
	}
}
