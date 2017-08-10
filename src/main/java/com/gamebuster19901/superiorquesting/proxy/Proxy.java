package com.gamebuster19901.superiorquesting.proxy;

import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Proxy implements Debuggable{
	private static final LifeHandler LIFE_HANDLER = new LifeHandler();
	
	public void preInit(FMLPreInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(LIFE_HANDLER);
	}
	
	public void init(FMLInitializationEvent e){
		
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}
	
	public LifeHandler getLifeHandler(){
		return LIFE_HANDLER;
	}
}
