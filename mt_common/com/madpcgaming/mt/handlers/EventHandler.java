package com.madpcgaming.mt.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

import com.madpcgaming.mt.helpers.LogHelper;
import com.madpcgaming.mt.lib.ItemIds;

import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.IEventListener;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class EventHandler implements IEventListener
{

	@Override
	@ForgeSubscribe
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
			if (item.itemID == ItemIds.CYBERSOWRD)
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
