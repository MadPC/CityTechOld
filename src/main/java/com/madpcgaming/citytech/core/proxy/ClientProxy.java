package com.madpcgaming.citytech.core.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.MinecraftForgeClient;

import com.madpcgaming.citytech.items.ModItems;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.power.PowerPiping;
import com.madpcgaming.citytech.piping.render.DefaultPipingRenderer;
import com.madpcgaming.citytech.piping.render.ItemPipingRenderer;
import com.madpcgaming.citytech.piping.render.PipingBundleRenderer;
import com.madpcgaming.citytech.piping.render.PipingRenderer;
import com.madpcgaming.citytech.tileentitys.CableTE;
import com.madpcgaming.citytech.tileentitys.renderers.CableRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	
	 private List<PipingRenderer> pipingRenderers = new ArrayList<PipingRenderer>();
	 private DefaultPipingRenderer dcr = new DefaultPipingRenderer();
	 private PipingBundleRenderer pbr;
	 
	 static
	 {
		 PowerPiping.initIcons();
	 }

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
	
	public PipingBundleRenderer getPipingBundleRenderer()
	{
		return pbr;
	}
	
	public void setPbr(PipingBundleRenderer pbr)
	{
		this.pbr = pbr;
	}
	
	@Override
	public void load()
	{
		super.load();
		ItemPipingRenderer itemPipeRenderer = new ItemPipingRenderer();
		//MinecraftForgeClient.registerItemRenderer(EnderIO.itemLiquidConduit.itemID, itemPipeRenderer);
	    MinecraftForgeClient.registerItemRenderer(ModItems.itemPowerPiping, itemPipeRenderer);
	    //MinecraftForgeClient.registerItemRenderer(EnderIO.itemRedstoneConduit.itemID, itemPipeRenderer);
	    //MinecraftForgeClient.registerItemRenderer(EnderIO.itemItemConduit.itemID, itemPipeRenderer);
	}
	
	
	
	 @Override
	  public double getReachDistanceForPlayer(EntityPlayer entityPlayer) {
	    if(entityPlayer instanceof EntityPlayerMP) {
	      return ((EntityPlayerMP) entityPlayer).theItemInWorldManager.getBlockReachDistance();
	    }
	    return super.getReachDistanceForPlayer(entityPlayer);
	  }
	 
	 
	 
	 
	 @Override
	 public PipingRenderer getRendererForPiping(IPiping piping)
	 {
		 for(PipingRenderer renderer : pipingRenderers)
		 {
			 if(renderer.isRendererForPiping(piping))
			 {
				 return renderer;
			 }
		 }
		 return dcr;
	 }
	
}
