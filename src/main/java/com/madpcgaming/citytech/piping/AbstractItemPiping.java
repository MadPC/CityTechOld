package com.madpcgaming.citytech.piping;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.madpcgaming.citytech.CityTech;
import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.BlockIds;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.util.BlockCoord;
import com.madpcgaming.citytech.util.Util;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class AbstractItemPiping extends Item implements IPipingItem
{
	protected ItemPipingSubtype[] subtypes;
	protected IIcon[] icons;
	
	
	protected AbstractItemPiping()
	{
		setCreativeTab(CityTech.tabsCT);
		setUnlocalizedName(Strings.ABSTRACT_ITEM_PIPING);
		setMaxStackSize(64);
		setHasSubtypes(true);
	}
	
	protected void init(ItemPipingSubtype[] subtypes)
	{
		this.subtypes = subtypes;
		icons = new IIcon[subtypes.length];
		GameRegistry.registerItem(this, Strings.ABSTRACT_ITEM_PIPING);
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		int index = 0;
		for(ItemPipingSubtype subtype : subtypes)
		{
			icons[index] = iconRegister.registerIcon(subtype.iconKey);
			index++;
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,int side, float hitX, float hitY, float hitZ)
	{
		BlockCoord placeAt = Util.canPlaceItem(stack, BlockIds.BLOCK_PIPING_BUNDLE, player, world, x, y, z, side);
		if(placeAt != null)
		{
			if(!world.isRemote)
			{
				if(world.setBlock(placeAt.x, placeAt.y, placeAt.z, ModBlocks.BlockPipingBundle, 0, 1))
				{
					IPipingBundle bundle = (IPipingBundle) world.getTileEntity(placeAt.x, placeAt.y, placeAt.z);
					if(bundle != null)
					{
						bundle.addPiping(createPiping(stack));
						Block b = ModBlocks.BlockPipingBundle;
						world.playSoundEffect(x +  0.5F, y + 0.5F, z + 0.5F, b.stepSound.func_150496_b(), (b.stepSound.getVolume() + 1.0F) / 2.0F, b.stepSound.getPitch() * 0.8F);
					}
				}
			}
			if(!player.capabilities.isCreativeMode)
			{
				stack.stackSize--;
			}
			return true;
		} else {
			ForgeDirection dir = ForgeDirection.values()[side];
			int placeX = x + dir.offsetX;
			int placeY = y + dir.offsetY;
			int placeZ = z + dir.offsetZ;
			
			if(world.getBlock(placeX, placeY, placeZ) == ModBlocks.BlockPipingBundle)
			{
				IPipingBundle bundle = (TilePipingBundle) world.getTileEntity(placeX, placeY, placeZ);
				if(bundle == null)
				{
					System.out.println("AbstractItemPiping.onItemUse : Bundle null");
					return false;
				}
				IPiping pipe = createPiping(stack);
				if(pipe == null)
				{
					System.out.println("AbstractItemPiping.onItemUse : Piping null");
					return false;
				}
				if(bundle.getPiping(pipe.getBasePipingType()) == null)
				{
					if(!world.isRemote)
					{
						bundle.addPiping(pipe);
						if(!player.capabilities.isCreativeMode)
						{
							stack.stackSize--;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public IIcon getIconFromDamage(int damage)
	{
		damage = MathHelper.clamp_int(damage, 0, subtypes.length - 1);
		return icons[damage];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int i = MathHelper.clamp_int(stack.getItemDamage(), 0, subtypes.length - 1);
		return subtypes[i].unlocalizedName;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubItems(Block par1, CreativeTabs tab, List list)
	{
		for(int j = 0; j < subtypes.length; j++)
		{
			list.add(new ItemStack(par1, 1, j));
		}
	}

}
