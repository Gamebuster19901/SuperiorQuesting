package com.gamebuster19901.superiorquesting.proxy;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.LoginHandler;
import com.gamebuster19901.superiorquesting.common.command.CommandDeath;
import com.gamebuster19901.superiorquesting.common.command.CommandLives;
import com.gamebuster19901.superiorquesting.common.command.CommandQuest;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.common.packet.handle.ClientPacketReceiver;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.PlayerQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.reward.ExperienceReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.ItemReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.RewardType;
import com.gamebuster19901.superiorquesting.common.questing.world.QuestWorldData;
import com.gamebuster19901.superiorquesting.server.packet.handle.ServerPacketReceiver;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraftforge.oredict.OreDictionary;

public abstract class Proxy implements Debuggable{
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	protected static LifeHandler LIFE_HANDLER;
	protected static LoginHandler LOGIN_HANDLER;
	protected static GlobalQuestHandler GLOBAL_QUEST_HANDLER;
	protected static PlayerQuestHandler PLAYER_QUEST_HANDLER;
	
	public void preInit(FMLPreInitializationEvent e){
		LOGIN_HANDLER = new LoginHandler();
		LIFE_HANDLER = new LifeHandler();
		GLOBAL_QUEST_HANDLER = new GlobalQuestHandler();
		PLAYER_QUEST_HANDLER = new PlayerQuestHandler();
		MinecraftForge.EVENT_BUS.register(LOGIN_HANDLER);
		MinecraftForge.EVENT_BUS.register(LIFE_HANDLER);
		MinecraftForge.EVENT_BUS.register(GLOBAL_QUEST_HANDLER);
		MinecraftForge.EVENT_BUS.register(PLAYER_QUEST_HANDLER);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		
	}
	
	public void init(FMLInitializationEvent e){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
		OreDictionary.registerOre("book", Items.BOOK);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		RewardType.registerRewardType(ItemReward.class, "Item Reward", "Rewards the player with some items");
		RewardType.registerRewardType(ExperienceReward.class, "xP reward", "Rewards the player with some experience");
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
	
	public final MinecraftServer getServer() {
		if(this instanceof ClientProxy) {
			if(!((ClientProxy)this).isServerRemote()) {
				return FMLCommonHandler.instance().getMinecraftServerInstance();
			}
		}
		else if(this instanceof ServerProxy) {
			return FMLServerHandler.instance().getServer();
		}
		return null;
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(ItemQuestBook.ITEM);
	    event.getRegistry().register(ItemHeartCanister.ITEM);
	}
	
	public void serverInit(FMLServerStartingEvent e){
		e.registerServerCommand(new CommandLives());
		e.registerServerCommand(new CommandQuest());
		e.registerServerCommand(new CommandDeath());
	}
}
