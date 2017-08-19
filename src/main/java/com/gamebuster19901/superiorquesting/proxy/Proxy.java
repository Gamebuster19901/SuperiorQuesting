package com.gamebuster19901.superiorquesting.proxy;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.command.CommandLives;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public abstract class Proxy implements Debuggable{
	private static final LifeHandler LIFE_HANDLER = new LifeHandler();
	
	public void preInit(FMLPreInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(LIFE_HANDLER);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent e){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}
	
	public LifeHandler getLifeHandler(){
		return LIFE_HANDLER;
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(ItemQuestBook.ITEM);
	    event.getRegistry().register(ItemHeartCanister.ITEM);
	}
	
	public void serverInit(FMLServerStartingEvent e){
		e.registerServerCommand(new CommandLives());
	}
}
