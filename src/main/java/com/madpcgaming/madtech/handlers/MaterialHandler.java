package com.madpcgaming.madtech.handlers;

import net.minecraftforge.common.util.EnumHelper;

import com.madpcgaming.madtech.lib.Materials;

public class MaterialHandler
{
	
	public static void addToolMaterials()
	{
		Materials.cyberEnergeticMaterial = EnumHelper.addToolMaterial("CyberEnergetic", 3, 2000, 9.0f, 3.0f, 20);
	}
}
