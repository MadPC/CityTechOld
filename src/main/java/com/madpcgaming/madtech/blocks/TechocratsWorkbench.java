package com.madpcgaming.madtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.madpcgaming.madtech.MadTech;
import com.madpcgaming.madtech.lib.GuiIds;
import com.madpcgaming.madtech.lib.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TechocratsWorkbench extends Block
{
	@SideOnly(Side.CLIENT)
    private IIcon workbenchIconTop;
    @SideOnly(Side.CLIENT)
    private IIcon workbenchIconFront;
    
    public TechocratsWorkbench(int i)
    {
        super(Material.field_151578_c);
        this.func_149647_a(MadTech.tabsMT);
        func_149663_c(Strings.WORKBENCH_NAME);
    }
    
    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.workbenchIconTop : (par1 == 0 ? Blocks.planks.func_149733_h(par1) : (par1 != 2 && par1 != 4 ? this.field_149761_L : this.workbenchIconFront));
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void func_94581_a(IIconRegister par1IconRegister)
    {
		this.field_149761_L = par1IconRegister.registerIcon("BetterTable:better_side");
		this.workbenchIconTop = par1IconRegister.registerIcon("BetterTable:better_top");
		this.workbenchIconFront = par1IconRegister.registerIcon("BetterTable:better_front");
    }
	
	@Override
	public boolean func_149727_a(World var1, int var2, int var3, int var4, EntityPlayer player, int var6, float var7, float var8, float var9)
    {
        if (!player.isSneaking())
			{
				player.openGui(MadTech.instance, GuiIds.TABLE, var1, var2, var3, var4);
				return true;
			}
			else
			{
				return false;
			}		
    }
}