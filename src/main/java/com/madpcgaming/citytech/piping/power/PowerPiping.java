package com.madpcgaming.citytech.piping.power;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.piping.AbstractPiping;
import com.madpcgaming.citytech.power.BasicTeslaBat;
import com.madpcgaming.citytech.power.ITeslaBat;
import com.madpcgaming.citytech.render.BoundingBox;
import com.madpcgaming.citytech.util.IconUtil;
import com.madpcgaming.citytech.vecmath.Vector3d;

public class PowerPiping extends AbstractPiping implements IPowerPiping
{
	static final Map<String, IIcon> ICONS = new HashMap<String, IIcon>();
	
	static final ITeslaBat[] TESLA_BAT = new BasicTeslaBat[]
	{
		new BasicTeslaBat(500, 1500, 128),
		new BasicTeslaBat(512, 3000, 512),
		new BasicTeslaBat(2048, 5000, 2048)
	};
	
	static final String[] POSTFIX = new String[] {"", "Enchanced", "Advanced"};
	
	static ItemStack createItemStackForSubtype(int subtype)
	{
		ItemStack result = new ItemStack(ModBlocks.itemPowerPiping, 1, subtype);
		return result;
	}
	
	public static void initIcons()
	{
		IconUtil.addIconProvider(new IconUtil.IIconProvider()
		{
			
			@Override
			public void registerIcons(IIconRegister register)
			{
				for(String pf : POSTFIX)
				{
					ICONS.put(ICON_KEY + pf, register.registerIcon(ICON_KEY + pf));
					ICONS.put(ICON_KEY_INPUT + pf, register.registerIcon(ICON_KEY_INPUT + pf));
			        ICONS.put(ICON_KEY_OUTPUT + pf, register.registerIcon(ICON_KEY_OUTPUT + pf));
			        ICONS.put(ICON_CORE_KEY + pf, register.registerIcon(ICON_CORE_KEY + pf));
				}
				ICONS.put(ICON_TRANSMISSION_KEY, register.registerIcon(ICON_TRANSMISSION_KEY));
			}
			
			@Override
			public int getTextureType()
			{
				// TODO Auto-generated method stub
				return 0;
			}
		});
	}
	
	public static final float WIDTH = 0.075f;
	public static final float HEIGHT = 0.075f;
	
	public static final Vector3d MIN = new Vector3d(0.5f - WIDTH, 0.5 - HEIGHT, 0.5 - WIDTH);
	public static final Vector3d MAX = new Vector3d(MIN.x + WIDTH, MIN.y + HEIGHT, MIN.z + WIDTH);
	
	public static final BoundingBox BOUNDS = new BoundingBox(MIN, MAX);
	
	protected PowerPipingNetwork network;
	
}
