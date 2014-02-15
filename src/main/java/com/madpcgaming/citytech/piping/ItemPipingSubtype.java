package com.madpcgaming.citytech.piping;

public class ItemPipingSubtype
{
	public final String unlocalizedName;
	
	public final String iconKey;
	
	public ItemPipingSubtype(String unlocalizedName, String iconKey)
	{
		this.unlocalizedName = "citytech." + unlocalizedName;
		this.iconKey = iconKey;
	}
}
