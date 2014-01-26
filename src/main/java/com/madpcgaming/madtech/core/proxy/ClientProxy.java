package com.madpcgaming.madtech.core.proxy;

import com.madpcgaming.madtech.tileentitys.CableTE;
import com.madpcgaming.madtech.tileentitys.renderers.CableRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{

	public void registerRenderings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(CableTE.class,
				new CableRenderer());
	}
	
}
