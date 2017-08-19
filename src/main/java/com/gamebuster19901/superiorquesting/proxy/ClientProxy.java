package com.gamebuster19901.superiorquesting.proxy;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.Confirmed;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber()
public class ClientProxy extends Proxy {
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(this); //so forge knows about your modelRegistryEvent that is in this class
		MinecraftForge.EVENT_BUS.register(new Confirmed());
	}
	
	public void init(FMLInitializationEvent e){
		super.init(e);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		super.postInit(e);
	}
	
	@SubscribeEvent
	public void modelRegistryEvent(ModelRegistryEvent e){
		ModelLoader.setCustomModelResourceLocation(ItemQuestBook.ITEM, 0, new ModelResourceLocation(MODID + ":questbook"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 0, new ModelResourceLocation(MODID + ":heartcanisterfull"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 1, new ModelResourceLocation(MODID + ":heartcanisterthreequarters"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 2, new ModelResourceLocation(MODID + ":heartcanisterhalf"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 3, new ModelResourceLocation(MODID + ":heartcanisterquarter"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 4, new ModelResourceLocation(MODID + ":heartcanisterempty"));
	}
	
	public static final SoundEvent CONFIRMED = new SoundEvent(new ResourceLocation(Main.MODID + ":confirmed")).setRegistryName(MODID + ":confirmed");
	
	@SubscribeEvent
	public void soundRegistryEvent(RegistryEvent<SoundEvent> e){
		ForgeRegistries.SOUND_EVENTS.register(CONFIRMED);
	}
}
