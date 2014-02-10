package com.madpcgaming.citytech.lib;

import java.io.File;
import java.io.IOException;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Daniel
 *
 */
public class CityTechConfig
{
	@SuppressWarnings({ "unused", "rawtypes" })
	public static void initProps(File location)
	{
		File newFile = new File(location + "/CityTech.config");
		
		try
		{
			newFile.createNewFile();
		}
		catch(IOException e)
		{
			
		}
		
		Configuration config = new Configuration(newFile);
		cfglocation = location;
		config.load();
		
		boolean ic2 = true;
		boolean xycraft = true;
		boolean adventureplus = true;
		try
		{
			Class c = Class.forName("ic2.core.IC2");
			ic2 = false;
		}
		catch (Exception e)
		{
		}
		try
		{
			Class c = Class.forName("soaryn.xycraft.core.XyCraft");
			xycraft = false;
		}
		catch(Exception e)
		{
		}
		try
		{
			Class c = Class.forName("adventureplus.core.AdventurePlus");
			adventureplus = false;
		}
		catch (Exception e)
		{
		}
		
		generateAluminium = config.get("Worldgen Disabler", "Generate Aluminum", true).getBoolean(true);
		generateCopper = config.get("Worldgen Disabler", "Generate Copper", true).getBoolean(true);
		generatePalladium = config.get("Worldgen Disabler", "Generate Palladium", true).getBoolean(true);
		generatePlatinum = config.get("Worldgen Disabler", "Generate Platinum", true).getBoolean(true);
		generateSilver = config.get("Worldgen Disabler", "Generate Silver", true).getBoolean(true);
		generateTin = config.get("Worldgen Disabler", "Generate Tin", true).getBoolean(true);
		
		aluminiumuDensity = config.get("WorldGen", "Aluminium Underground Density", 3, "Density: Chances per Chunk").getInt(3);
		copperuDensity = config.get("WorldGen", "Copper Underground Density", 3).getInt(3);
		palladiumuDensity = config.get("WorldGen", "Palladium Underground Density", 3).getInt(3);
		platinumuDensity = config.get("WorldGen", "Platinum Underground Density", 3).getInt(3);
		silveruDensity = config.get("WorldGen", "Silver Underground Density", 3).getInt(3);
		tinuDensity = config.get("WorldGen", "Tin Underground Density", 3).getInt(3);
		
		aluminiumuMinY = config.get("WorldGen", "Aluminium Underground Min Y", 10).getInt(10);
		aluminiumuMaxY = config.get("WorldGen", "Aluminium Underground Max Y", 55).getInt(55);
		copperuMinY = config.get("WorldGen", "Copper Underground Min Y", 10).getInt(10);
		copperuMaxY = config.get("WorldGen", "Copper Underground Max Y", 55).getInt(55);
		palladiumuMinY = config.get("WorldGen", "Palladium Underground Min Y", 10).getInt(10);
		palladiumuMaxY = config.get("WorldGen", "Palladium Underground Max Y", 55).getInt(55);
		platinumuMinY = config.get("WorldGen", "Platinum Underground Min Y", 10).getInt(10);
		platinumuMaxY = config.get("WorldGen", "Platinum Underground Max Y", 55).getInt(55);
		silveruMinY = config.get("WorldGen", "Silver Underground Min Y", 10).getInt(10);
		silveruMaxY = config.get("WorldGen", "Silver Underground Max Y", 55).getInt(55);
		tinuMinY = config.get("WorldGen", "Tin Underground Min Y", 10).getInt(10);
		tinuMaxY = config.get("WorldGen", "Tin Underground Max Y", 55).getInt(55);
		
		fluidPipingExtractRate = config.get("Settings", "fluidpipingExtractRate", fluidPipingExtractRate,
		        "Number of millibuckects per tick extract by a fluid pipings auto extract..").getInt(fluidPipingExtractRate);

		fluidPipingMaxIoRate = config.get("Settings", "fluidpipingMaxIoRate", fluidPipingMaxIoRate,
		        "Number of millibuckects per tick that can pass through a single connection to a fluid piping.").getInt(fluidPipingMaxIoRate);
		
		
		
		config.save();
	}
	
	public static File cfglocation;
	
	public static boolean generateAluminium;
	public static boolean generateCopper;
	public static boolean generatePalladium;
	public static boolean generatePlatinum;
	public static boolean generateSilver;
	public static boolean generateTin;
	
	public static int aluminiumuDensity;
	public static int copperuDensity;
	public static int palladiumuDensity;
	public static int platinumuDensity;
	public static int silveruDensity;
	public static int tinuDensity;
	
	public static int aluminiumuMinY;
	public static int aluminiumuMaxY;
	public static int copperuMinY;
	public static int copperuMaxY;
	public static int palladiumuMinY;
	public static int palladiumuMaxY;
	public static int platinumuMinY;
	public static int platinumuMaxY;
	public static int silveruMinY;
	public static int silveruMaxY;
	public static int tinuMinY;
	public static int tinuMaxY;

	public static int fluidPipingExtractRate;

	public static int fluidPipingMaxIoRate;

	public static boolean itemPipingUsePhysicalDistance;

	public static boolean	detailedPowerTrackingEnabled;
}
