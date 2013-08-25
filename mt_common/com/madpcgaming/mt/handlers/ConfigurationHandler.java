package com.madpcgaming.mt.handlers;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;

import com.madpcgaming.mt.lib.BlockIds;
import com.madpcgaming.mt.lib.ItemIds;
import com.madpcgaming.mt.lib.Reference;
import com.madpcgaming.mt.lib.Strings;

import cpw.mods.fml.common.FMLLog;

public class ConfigurationHandler
{
	
	public static Configuration	configuration;
	
	public static void init(File configFile)
	{
		configuration = new Configuration(configFile);
		
		try
		{
			configuration.load();
			
			/* Block Configs */
			BlockIds.ALUMINUM_ORE = configuration.getBlock(Strings.ALUMINUM_ORE_NAME, BlockIds.ALUMINUM_ORE_DEFAULT).getInt(
					BlockIds.ALUMINUM_ORE_DEFAULT);
			BlockIds.COPPER_ORE = configuration.getBlock(Strings.COPPER_ORE_NAME, BlockIds.COPPER_ORE_DEFAULT).getInt(
					BlockIds.COPPER_ORE_DEFAULT);
			BlockIds.PALLADIUM_ORE = configuration.getBlock(Strings.PALLADIUM_ORE_NAME, BlockIds.PALLADIUM_ORE_DEFAULT).getInt(
					BlockIds.PALLADIUM_ORE_DEFAULT);
			BlockIds.PLATINUM_ORE = configuration.getBlock(Strings.PLATINUM_ORE_NAME, BlockIds.PLATINUM_ORE_DEFAULT).getInt(
					BlockIds.PLATINUM_ORE_DEFAULT);
			BlockIds.SLIVER_ORE = configuration.getBlock(Strings.SILVER_ORE_NAME, BlockIds.SILVER_ORE_DEFAULT).getInt(
					BlockIds.SILVER_ORE_DEFAULT);
			BlockIds.TIN_ORE = configuration.getBlock(Strings.TIN_ORE_NAME, BlockIds.TIN_ORE_DEFAULT).getInt(BlockIds.TIN_ORE_DEFAULT);
			BlockIds.SILICON_LIQUID_STILL = configuration.getBlock(Strings.SILICON_LIQUID_STILL, BlockIds.SILICON_LIQUID_STILL_DEFAULT)
					.getInt(BlockIds.SILICON_LIQUID_STILL_DEFAULT);
			BlockIds.COPPER_CABLE = configuration.getBlock(Strings.COPPER_CABLE_NAME, BlockIds.COPPER_CABLE_DEFAULT).getInt(
					BlockIds.COPPER_CABLE_DEFAULT);
			BlockIds.BLOCK_ELECTROLYSER = configuration.getBlock(Strings.ELECTROLYSER_NAME, BlockIds.BLOCK_ELECTROLYSER_DEFAULT).getInt(
					BlockIds.BLOCK_ELECTROLYSER_DEFAULT);
			
			/* Item Configs */
			ItemIds.ALUMINIUM_INGOT = configuration.getItem(Strings.ALUMINUM_INGOT_NAME, ItemIds.ALUMINIUM_INGOT_DEFAULT).getInt(
					ItemIds.ALUMINIUM_INGOT_DEFAULT);
			ItemIds.COPPER_INGOT = configuration.getItem(Strings.COPPER_INGOT_NAME, ItemIds.COPPER_INGOT_DEFAULT).getInt(
					ItemIds.COPPER_INGOT_DEFAULT);
			ItemIds.PALLADIUM_INGOT = configuration.getItem(Strings.PALLADIUM_INGOT_NAME, ItemIds.PALLADIUM_INGOT_DEFAULT).getInt(
					ItemIds.PALLADIUM_INGOT_DEFAULT);
			ItemIds.PLATINUM_INGOT = configuration.getItem(Strings.PLATINUM_INGOT_NAME, ItemIds.PLATINUM_INGOT_DEFAULT).getInt(
					ItemIds.PLATINUM_INGOT_DEFAULT);
			ItemIds.SILVER_INGOT = configuration.getItem(Strings.SILVER_INGOT_NAME, ItemIds.SILVER_INGOT_DEFAULT).getInt(
					ItemIds.SILVER_INGOT_DEFAULT);
			ItemIds.TIN_INGOT = configuration.getItem(Strings.TIN_INGOT_NAME, ItemIds.TIN_INGOT_DEFAULT).getInt(ItemIds.TIN_INGOT_DEFAULT);
			ItemIds.WIRING_ITEM = configuration.getItem(Strings.WIRING_ITEM_NAME, ItemIds.WIRING_ITEM_DEFAULT).getInt(
					ItemIds.WIRING_ITEM_DEFAULT);
			ItemIds.SILICON_ITEM = configuration.getItem(Strings.SILICON_ITEM_NAME, ItemIds.SILICON_ITEM_DEFAULT).getInt(
					ItemIds.SILICON_ITEM_DEFAULT);
			ItemIds.RUBBER_ITEM = configuration.getItem(Strings.RUBBER_ITEM_NAME, ItemIds.RUBBER_ITEM_DEFAULT).getInt(
					ItemIds.RUBBER_ITEM_DEFAULT);
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem loading its configuration");
		}
		finally
		{
			configuration.save();
		}
	}
	
	public static void set(String categoryName, String propertyName, String newValue)
	{
		
		configuration.load();
		if (configuration.getCategoryNames().contains(categoryName))
		{
			if (configuration.getCategory(categoryName).containsKey(propertyName))
			{
				configuration.getCategory(categoryName).get(propertyName).set(newValue);
			}
		}
		configuration.save();
	}
	
}
