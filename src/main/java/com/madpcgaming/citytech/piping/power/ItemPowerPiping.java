package com.madpcgaming.citytech.piping.power;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.Lang;
import com.madpcgaming.citytech.lib.Strings;
import com.madpcgaming.citytech.machine.power.PowerDisplayUtil;
import com.madpcgaming.citytech.piping.AbstractItemPiping;
import com.madpcgaming.citytech.piping.IPiping;
import com.madpcgaming.citytech.piping.ItemPipingSubtype;
import com.madpcgaming.citytech.power.ITeslaBat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPowerPiping extends AbstractItemPiping {

  private static String PREFIX;
  private static String POSTFIX;

  static ItemPipingSubtype[] subtypes = new ItemPipingSubtype[] {
      new ItemPipingSubtype(Strings.ITEM_POWER_PIPING_NAME, "enderio:itemPowerConduit"),
      new ItemPipingSubtype(Strings.ITEM_POWER_PIPING_NAME + "Enhanced", "enderio:itemPowerPipingEnhanced"),
      new ItemPipingSubtype(Strings.ITEM_POWER_PIPING_NAME + "Advanced", "enderio:itemPowerPipingAdvanced")
  };

  public static ItemPowerPiping create() {
    ItemPowerPiping result = new ItemPowerPiping();
    result.init(subtypes);
    return result;
  }

  protected ItemPowerPiping() {
    super(ModBlocks.itemPowerPiping);
  }

  @Override
  public Class<? extends IPiping> getBasePipingType() {
    return IPowerPiping.class;
  }

  @Override
  public IPiping createPiping(ItemStack stack) {
    return new PowerPiping(stack.getItemDamage());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
    if(PREFIX == null) {
      POSTFIX = " " + PowerDisplayUtil.abrevation() + PowerDisplayUtil.perTick();
      PREFIX = Lang.localize("power.maxOutput") + " ";
    }
    super.addInformation(itemStack, par2EntityPlayer, list, par4);
    ITeslaBat bat = PowerPiping.TESLA_BAT[itemStack.getItemDamage()];
    list.add(PREFIX + PowerDisplayUtil.formatPower(bat.getMaxEnergyExtracted()) + POSTFIX);
  }

}