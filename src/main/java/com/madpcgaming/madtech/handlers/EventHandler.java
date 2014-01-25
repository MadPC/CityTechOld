package com.madpcgaming.madtech.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.madpcgaming.madtech.helpers.LogHelper;
import com.madpcgaming.madtech.items.ModItems;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.IEventListener;

public class EventHandler implements IEventListener
{

	@Override
	public void invoke(Event event)
	{
		//LogHelper.info("EventCatched!");
		if (event instanceof LivingDeathEvent) EntityDeathHandler((LivingDeathEvent) event);
	}
	
	private void EntityDeathHandler(LivingDeathEvent event)
	{
		if (event.entity.worldObj.isRemote)
			return;
		EntityLivingBase target = event.entityLiving;
		DamageSource source = event.source;
		if (source.damageType == "player")
		{
			EntityPlayerMP player = (EntityPlayerMP) source.getSourceOfDamage();
			ItemStack item = player.getCurrentItemOrArmor(0);
			if (item.getItem() == ModItems.CyberSword)
			{
				NBTTagCompound tag = item.stackTagCompound;
				if (tag.hasKey("kills"))
					tag.setInteger("kills", tag.getInteger("kills") + 1);
				else
					tag.setInteger("kills", 1);
			}
		}
		LogHelper.info("&&Entity %s died due to Damage %s with %s as DamageSource", target, source.damageType, source.getSourceOfDamage());
	}
}
