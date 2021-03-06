package com.gamebuster19901.superiorquesting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.proxy.Proxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public final class Main implements Assertable
{
	private static Main instance;
	
	public static final String MODID = "questing";
	public static final String MODNAME = "Superior Questing";
	public static final String VERSION = "0.0.0.0";
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);
	@SidedProxy(serverSide = "com.gamebuster19901.superiorquesting.proxy.ServerProxy", clientSide = "com.gamebuster19901.superiorquesting.proxy.ClientProxy")
	public static Proxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		instance = this;
		LOGGER.info("PreInitialization Begin");
		proxy.preInit(e);
		LOGGER.info("PreInitialization End");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		LOGGER.info("Initialization Begin");
		proxy.init(e);
		LOGGER.info("Initialization End");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e){
		LOGGER.info("PostInitialization Begin");
		proxy.postInit(e);
		LOGGER.info("PostInitialization End");
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent e){
		LOGGER.info("ServerStarting Begin");
		proxy.serverInit(e);
		LOGGER.info("ServerStarting End");
	}
	
	public static Main getInstance(){
		return instance;
	}
}
