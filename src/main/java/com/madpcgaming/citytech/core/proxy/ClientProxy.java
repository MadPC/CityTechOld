package com.madpcgaming.citytech.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.madpcgaming.citytech.tileentitys.CableTE;
import com.madpcgaming.citytech.tileentitys.renderers.CableRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{

	public void registerRenderings()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(CableTE.class,
				new CableRenderer());
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	
	 @Override
	  public double getReachDistanceForPlayer(EntityPlayer entityPlayer) {
	    if(entityPlayer instanceof EntityPlayerMP) {
	      return ((EntityPlayerMP) entityPlayer).theItemInWorldManager.getBlockReachDistance();
	    }
	    return super.getReachDistanceForPlayer(entityPlayer);
	  }
	
}
