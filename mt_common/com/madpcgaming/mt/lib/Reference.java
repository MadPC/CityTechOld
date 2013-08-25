package com.madpcgaming.mt.lib;

public class Reference
{
	
	public static final String	MOD_ID				= "mt";
	public static final String	MOD_NAME			= "MadTech";
	public static final String	VERSION				= "pre-release-0.0.1";
	public static final String	CHANNEL_NAME		= MOD_ID;
	public static final String	CLIENT_PROXY_CLASS	= "com.madpcgaming.mt.core.proxy.ClientProxy";
	public static final String	SERVER_PROXY_CLASS	= "com.madpcgaming.mt.core.proxy.CommonProxy";
	
	// different masks to be applied with bitwise-and
	public static final byte	TOP					= 2;
	public static final byte	BOTTOM				= 1;
	/** -X aka West */
	public static final byte	LEFT				= 16;
	/** +X aka East */
	public static final byte	RIGHT				= 32;
	/** -Z aka North */
	public static final byte	FRONT				= 4;
	/** +Z aka South */
	public static final byte	BACK				= 8;
}
