package com.madpcgaming.citytech.util;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod.EventHandler;

public class IconUtil
{
	public static interface IIconProvider
	{
		public void registerIcons(IIconRegister register);
		
		public int getTextureType();
	}
	
	private static ArrayList<IIconProvider> iconProviders = new ArrayList<IIconProvider>();
	
	public static IIcon whiteTexture;
	
	static{
		MinecraftForge.EVENT_BUS.register(new IconUtil());
		addIconProvider(new IIconProvider()
		{
			@Override
			public void registerIcons(IIconRegister register)
			{
				whiteTexture = register.registerIcon("citytech:white");
			}
			
			@Override
			public int getTextureType()
			{
				return 0;
			}
		});
	}
	
	public static void addIconProvider(IIconProvider registrar)
	{
		iconProviders.add(registrar);
	}
	
	@EventHandler
	public void onIconLoad(TextureStitchEvent.Pre event)
	{
		for(IIconProvider reg : iconProviders)
		{
			if(reg.getTextureType() == event.map.getTextureType())
			{
				reg.registerIcons(event.map);
			}
		}
	}
	
}