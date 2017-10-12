package com.gamebuster19901.superiorquesting.proxy;

import java.io.File;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.LoginHandler;
import com.gamebuster19901.superiorquesting.common.command.CommandLives;
import com.gamebuster19901.superiorquesting.common.command.CommandQuest;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.PlayerQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.reward.CommonExperienceReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.CommonItemReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.RewardType;

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
	private static File questDirectory;
	
	private static LifeHandler LIFE_HANDLER;
	private static LoginHandler LOGIN_HANDLER;
	private static GlobalQuestHandler GLOBAL_QUEST_HANDLER;
	private static PlayerQuestHandler PLAYER_QUEST_HANDLER;
	
	public void preInit(FMLPreInitializationEvent e){
		questDirectory = new File(e.getModConfigurationDirectory().getAbsolutePath() + "/questdata");
		LIFE_HANDLER = new LifeHandler();
		LOGIN_HANDLER = new LoginHandler();
		GLOBAL_QUEST_HANDLER = new GlobalQuestHandler();
		PLAYER_QUEST_HANDLER = new PlayerQuestHandler();
		MinecraftForge.EVENT_BUS.register(LIFE_HANDLER);
		MinecraftForge.EVENT_BUS.register(LOGIN_HANDLER);
		MinecraftForge.EVENT_BUS.register(GLOBAL_QUEST_HANDLER);
		MinecraftForge.EVENT_BUS.register(PLAYER_QUEST_HANDLER);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent e){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
		OreDictionary.registerOre("book", Items.BOOK);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		RewardType.registerRewardType(CommonItemReward.class, "Item Reward", "Rewards the player with some items");
		RewardType.registerRewardType(CommonExperienceReward.class, "xP reward", "Rewards the player with some experience");
	}
	
	public LifeHandler getLifeHandler(){
		return LIFE_HANDLER;
	}
	
	public LoginHandler getLoginHandler() {
		return LOGIN_HANDLER;
	}
	
	public GlobalQuestHandler getGlobalQuestHandler() {
		return GLOBAL_QUEST_HANDLER;
	}
	
	public PlayerQuestHandler getPlayerQuestHandler(){
		return PLAYER_QUEST_HANDLER;
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(ItemQuestBook.ITEM);
	    event.getRegistry().register(ItemHeartCanister.ITEM);
	}
	
	public void serverInit(FMLServerStartingEvent e){
		e.registerServerCommand(new CommandLives());
		e.registerServerCommand(new CommandQuest());
	}
	
	public final File getQuestDirectory() {
		return questDirectory;
	}
}
