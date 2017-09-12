package com.gamebuster19901.superiorquesting.proxy;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.LoginHandler;
import com.gamebuster19901.superiorquesting.common.command.CommandLives;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.ExperienceReward;
import com.gamebuster19901.superiorquesting.common.questing.ItemReward;
import com.gamebuster19901.superiorquesting.common.questing.RewardType;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public abstract class Proxy implements Debuggable{
	private static final LifeHandler LIFE_HANDLER = new LifeHandler();
	private static final LoginHandler LOGIN_HANDLER = new LoginHandler();
	
	public void preInit(FMLPreInitializationEvent e){
		MinecraftForge.EVENT_BUS.register(LIFE_HANDLER);
		MinecraftForge.EVENT_BUS.register(LOGIN_HANDLER);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent e){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
		OreDictionary.registerOre("book", Items.BOOK);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		RewardType.registerRewardType(ItemReward.class, TextureMap.LOCATION_MISSING_TEXTURE, "Item Reward", "Rewards the player with some items");
		RewardType.registerRewardType(ExperienceReward.class, new ResourceLocation("textures/entity/experience_orb.png"), "xP reward", "Rewards the player with some experience");
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
