package com.madpcgaming.mt.handlers;

import net.minecraft.item.EnumToolMaterial;

import net.minecraftforge.common.EnumHelper;

import com.madpcgaming.mt.lib.Materials;

public class MaterialHandler
{
	
	public static void addToolMaterials()
	{
		Materials.cyberEnergeticMaterial = EnumHelper.addToolMaterial("CyberEnergetic", 3, 2000, 9.0f, 3.0f, 20);
	}
}
