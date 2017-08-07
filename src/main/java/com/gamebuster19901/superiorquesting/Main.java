package com.gamebuster19901.superiorquesting;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main
{
	public static final String MODID = "examplemod";
	public static final String VERSION = "1.0";
	
	@EventHandler
	public void preInig(FMLPreInitializationEvent e){
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// some example code
		System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
	}
}
