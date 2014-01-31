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
	}
	
	public static File cfglocation;
}
