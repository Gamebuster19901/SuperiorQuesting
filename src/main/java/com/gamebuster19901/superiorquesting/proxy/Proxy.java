package com.gamebuster19901.superiorquesting.proxy;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.command.CommandDeath;
import com.gamebuster19901.superiorquesting.common.command.CommandLives;
import com.gamebuster19901.superiorquesting.common.command.CommandQuest;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.MultiplayerHandler;
import com.gamebuster19901.superiorquesting.common.questing.PlayerQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.reward.ExperienceReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.ItemReward;
import com.gamebuster19901.superiorquesting.common.questing.reward.RewardType;
import com.gamebuster19901.superiorquesting.common.shape.Circle;
import com.gamebuster19901.superiorquesting.common.shape.RegularGon;
import com.gamebuster19901.superiorquesting.common.shape.Shape;
import com.gamebuster19901.superiorquesting.common.shape.ShapeType;
import com.gamebuster19901.superiorquesting.common.shape.Square;
import com.gamebuster19901.superiorquesting.common.shape.TriangleDown;
import com.gamebuster19901.superiorquesting.common.shape.TriangleLeft;
import com.gamebuster19901.superiorquesting.common.shape.TriangleRight;
import com.gamebuster19901.superiorquesting.common.shape.TriangleUp;

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
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraftforge.oredict.OreDictionary;

public abstract class Proxy implements Debuggable{
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	protected static MultiplayerHandler MULTIPLAYER_HANDLER;
	protected static LifeHandler LIFE_HANDLER;
	protected static GlobalQuestHandler GLOBAL_QUEST_HANDLER;
	protected static PlayerQuestHandler PLAYER_QUEST_HANDLER;
	
	public void preInit(FMLPreInitializationEvent e){
		MULTIPLAYER_HANDLER = new MultiplayerHandler();
		LIFE_HANDLER = new LifeHandler();
		GLOBAL_QUEST_HANDLER = new GlobalQuestHandler();
		PLAYER_QUEST_HANDLER = new PlayerQuestHandler();
		MinecraftForge.EVENT_BUS.register(MULTIPLAYER_HANDLER);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void init(FMLInitializationEvent e){
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
		OreDictionary.registerOre("book", Items.BOOK);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		RewardType.registerRewardType(ItemReward.class, "Item Reward", "Rewards the player with some items");
		RewardType.registerRewardType(ExperienceReward.class, "xP reward", "Rewards the player with some experience");
		ShapeType.registerShapeType(Circle.class, "Circle", "A basic circle");
		ShapeType.registerShapeType(TriangleUp.class, "Triangle (Up)", "An equilateral triangle pointing up");
		ShapeType.registerShapeType(TriangleDown.class, "Triangle (Down)", "An equilateral triangle pointing down");
		ShapeType.registerShapeType(TriangleLeft.class, "Triangle(Left)", "An equilateral triangle pointing left");
		ShapeType.registerShapeType(TriangleRight.class, "Triangle (Right)", "An equilateral triangle pointing right");
		ShapeType.registerShapeType(Square.class, "Square", "A special type of rectangle where all side lengths are equal");
		ShapeType.registerShapeType(RegularGon.class, "Polygon", "A regular polygon with 5 or more sides", int.class, double.class);
		checkValidState();
	}
	
	public LifeHandler getLifeHandler(){
		return LIFE_HANDLER;
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
	
	protected abstract void checkValidState();
}
