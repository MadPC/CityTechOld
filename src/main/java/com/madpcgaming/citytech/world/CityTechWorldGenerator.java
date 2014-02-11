package com.madpcgaming.citytech.world;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.madpcgaming.citytech.blocks.ModBlocks;
import com.madpcgaming.citytech.lib.CityTechConfig;

import cpw.mods.fml.common.IWorldGenerator;

public class CityTechWorldGenerator implements IWorldGenerator
{
	public CityTechWorldGenerator()
	{
		aluminium = new WorldGenMinable(ModBlocks.AluminiumOre, 4, 8, Blocks.stone);
		copper = new WorldGenMinable(ModBlocks.CopperOre, 4, 8, Blocks.stone);
		palladium = new WorldGenMinable(ModBlocks.PalladiumOre, 4, 8, Blocks.stone);
		platinum = new WorldGenMinable(ModBlocks.PlatinumOre, 4, 8, Blocks.stone);
		silver = new WorldGenMinable(ModBlocks.SilverOre, 4, 8, Blocks.stone);
		tin = new WorldGenMinable(ModBlocks.TinOre, 4, 8, Blocks.stone);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.terrainType != WorldType.FLAT)
		{
			generateSurface(random, chunkX * 16, chunkZ * 16, world);
		}
		
	}
	
	void generateSurface(Random random, int xChunk, int zChunk, World world)
	{
		String  biomeName = world.getWorldChunkManager().getBiomeGenAt(xChunk, zChunk).biomeName;
		
		generateUndergroundOres(random, xChunk, zChunk, world);
		
		if(biomeName == "Extreme Hills Edge" || biomeName == "Extreme Hills")
		{
			generateUndergroundOres(random, xChunk, zChunk, world);
		}
	}
	
	void generateUndergroundOres(Random random, int xChunk, int zChunk, World world)
	{
		int xPos, yPos, zPos;
		if (CityTechConfig.generateAluminium)
		{
			for(int q = 0; q <= CityTechConfig.aluminiumuDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.aluminiumuMinY + random.nextInt(CityTechConfig.aluminiumuMaxY - CityTechConfig.aluminiumuMinY);
				zPos = zChunk + random.nextInt(16);
				aluminium.generate(world, random, xPos, yPos, zPos);
			}
		}
		if (CityTechConfig.generateCopper)
		{
			for(int q = 0; q <= CityTechConfig.copperuDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.copperuMinY + random.nextInt(CityTechConfig.copperuMaxY - CityTechConfig.copperuMinY);
				zPos = zChunk + random.nextInt(16);
				copper.generate(world, random, xPos, yPos, zPos);
			}
		}
		if (CityTechConfig.generatePalladium)
		{
			for(int q = 0; q <= CityTechConfig.palladiumuDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.palladiumuMinY + random.nextInt(CityTechConfig.palladiumuMaxY - CityTechConfig.palladiumuMinY);
				zPos = zChunk + random.nextInt(16);
				palladium.generate(world, random, xPos, yPos, zPos);
			}
		}
		if (CityTechConfig.generatePlatinum)
		{
			for(int q = 0; q <= CityTechConfig.platinumuDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.platinumuMinY + random.nextInt(CityTechConfig.platinumuMaxY - CityTechConfig.platinumuMinY);
				zPos = zChunk + random.nextInt(16);
				platinum.generate(world, random, xPos, yPos, zPos);
			}
		}
		if (CityTechConfig.generateSilver)
		{
			for(int q = 0; q <= CityTechConfig.silveruDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.silveruMinY + random.nextInt(CityTechConfig.silveruMaxY - CityTechConfig.silveruMinY);
				zPos = zChunk + random.nextInt(16);
				silver.generate(world, random, xPos, yPos, zPos);
			}
		}
		if (CityTechConfig.generateTin)
		{
			for(int q = 0; q <= CityTechConfig.tinuDensity; q++)
			{
				xPos = xChunk + random.nextInt(16);
				yPos = CityTechConfig.tinuMinY + random.nextInt(CityTechConfig.tinuMaxY - CityTechConfig.tinuMinY);
				zPos = zChunk + random.nextInt(16);
				tin.generate(world, random, xPos, yPos, zPos);
			}
		}
	}

	WorldGenMinable aluminium;
	WorldGenMinable copper;
	WorldGenMinable palladium;
	WorldGenMinable platinum;
	WorldGenMinable silver;
	WorldGenMinable tin;
}
