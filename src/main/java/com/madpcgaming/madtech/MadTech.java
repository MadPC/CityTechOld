package com.madpcgaming.madtech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import com.madpcgaming.madtech.blocks.ModBlocks;
import com.madpcgaming.madtech.core.proxy.CommonProxy;
import com.madpcgaming.madtech.handlers.MaterialHandler;
import com.madpcgaming.madtech.items.ModItems;
import com.madpcgaming.madtech.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class MadTech
{
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy	proxy;
	@Instance("MT")
	public static MadTech		instance;
	public static CreativeTabs	tabsMT	= new CreativeTabMT(CreativeTabs.getNextID(), Reference.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, MadTech.proxy);
		instance = this;
		MaterialHandler.addToolMaterials();
		
		ModBlocks.init();
		ModItems.init();
		
		MinecraftForge.EVENT_BUS.register(new com.madpcgaming.madtech.handlers.EventHandler());
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
