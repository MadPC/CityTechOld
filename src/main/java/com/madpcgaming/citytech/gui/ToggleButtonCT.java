package com.madpcgaming.citytech.gui;

import net.minecraft.client.Minecraft;

public class ToggleButtonCT extends IconButtonCT {

	  private boolean selected;
	  private IconCT unselectedIcon;
	  private IconCT selectedIcon;

	  private String[] selectedTooltip;
	  private String[] unselectedTooltip;
	  private boolean paintSelectionBorder;

	  public ToggleButtonCT(IGuiScreen gui, int id, int x, int y, IconCT unselectedIcon, IconCT selectedIcon) {
	    super(gui, id, x, y, unselectedIcon);
	    this.unselectedIcon = unselectedIcon;
	    this.selectedIcon = selectedIcon;
	    selected = false;
	    paintSelectionBorder = true;
	  }

	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean selected) {
	    this.selected = selected;
	    icon = selected ? selectedIcon : unselectedIcon;
	    if(selected && selectedTooltip != null) {
	      setToolTip(selectedTooltip);
	    } else if(!selected && unselectedTooltip != null) {
	      setToolTip(unselectedTooltip);
	    }
	  }

	  @Override
	  protected IconCT getIconForHoverState(int hoverState) {
	    if(!selected || !paintSelectionBorder) {
	      return super.getIconForHoverState(hoverState);
	    }
	    if(hoverState == 0) {
	      return IconCT.BUTTON_DISABLED;
	    }
	    if(hoverState == 2) {
	      return IconCT.BUTTON_DOWN_HIGHLIGHT;
	    }
	    return IconCT.BUTTON_DOWN;
	  }

	  @Override
	  public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
	    boolean result = super.mousePressed(par1Minecraft, par2, par3);
	    if(result) {
	      setSelected(!selected);
	    }
	    return result;

	  }

	  public void setSelectedToolTip(String... tt) {
	    this.selectedTooltip = tt;
	    setSelected(selected);
	  }

	  public void setUnselectedToolTip(String... tt) {
	    this.unselectedTooltip = tt;
	    setSelected(selected);
	  }

	  public void setPaintSelectedBorder(boolean b) {
	    this.paintSelectionBorder = b;
	  }

	}
