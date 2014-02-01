package com.madpcgaming.citytech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.core.proxy.CommonProxy;
import com.madpcgaming.citytech.handlers.MaterialHandler;
import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.lib.CityTechConfig;
import com.madpcgaming.citytech.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class CityTech
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy	proxy;
	@Instance("MT")
	public static CityTech		instance;
	public static CreativeTabs	tabsMT	= new CreativeTabMT(CreativeTabs.getNextID(), Reference.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, CityTech.proxy);
		instance = this;
		MaterialHandler.addToolMaterials();
		
		CityTechConfig.initProps(event.getModConfigurationDirectory());
		ModBlocks.init();
		ModItems.init();
		
		MinecraftForge.EVENT_BUS.register(new com.madpcgaming.citytech.handlers.EventHandler());
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{

	}

	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event)
	{

	}
}
