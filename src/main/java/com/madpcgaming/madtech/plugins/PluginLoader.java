package com.madpcgaming.madtech.plugins;

import com.madpcgaming.madtech.helpers.LogHelper;

public class PluginLoader
{
	
	public static final PluginLoader	instance		= new PluginLoader();
	
	/**
	 * Array Holding small Humanreadable Identifiers
	 */
	private static String[]				plugins			= new String[] { "" };
	
	/**
	 * Array Holding the Corresponding Classes
	 */
	private static String[]				pluginClasses	= new String[] { "" };
	
	private static Class<?>[]			pluginClass		= new Class<?>[pluginClasses.length];
	
	// Keep it a Singleton!
	private PluginLoader() {}
	
	public void loadPlugins()
	{
		for (int i = 0; i < plugins.length; i++)
		{
			String ID = plugins[i];
			String PluginClass = pluginClasses[i];
			LogHelper.info("&&Loading Plugin %s for MadTech", ID);
			try
			{
				Class<?> c = Class.forName(PluginClass);
				pluginClass[i] = c;
			}
			catch (ClassNotFoundException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
