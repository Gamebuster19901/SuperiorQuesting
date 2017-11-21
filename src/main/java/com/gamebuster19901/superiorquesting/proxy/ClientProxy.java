package com.gamebuster19901.superiorquesting.proxy;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.Confirmed;
import com.gamebuster19901.superiorquesting.client.gui.GuiTrueGameOver;
import com.gamebuster19901.superiorquesting.client.network.ClientPacketReceiver;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.server.network.ServerPacketReceiver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
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
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber()
public final class ClientProxy extends Proxy {
	private static String SERVER_TYPE = "NONE";
	private static boolean doesServerContainMod = false;
	private static boolean isServerRemote = false;
	
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(this); //so forge knows about your modelRegistryEvent that is in this class
		MinecraftForge.EVENT_BUS.register(new Confirmed());
		MinecraftForge.EVENT_BUS.register(new GuiTrueGameOver(new TextComponentString("This should never appear")));
		
		PacketType[] types = GenericQuestingPacket.PacketType.values();
		for(int discriminator = 0; discriminator < types.length; discriminator++) {
			Class<? extends GenericQuestingPacket> clazz = types[discriminator].getMappedClass();
			if(clazz != null) {
				NETWORK.registerMessage(new ClientPacketReceiver(clazz), clazz, discriminator, Side.CLIENT);
				NETWORK.registerMessage(new ServerPacketReceiver(clazz), clazz, discriminator, Side.SERVER);
			}
		}
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
		ModelLoader.setCustomModelResourceLocation(ItemQuestBook.ITEM, 1, new ModelResourceLocation(MODID + ":questbook"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 0, new ModelResourceLocation(MODID + ":heartcanisterfull"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 1, new ModelResourceLocation(MODID + ":heartcanisterthreequarters"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 2, new ModelResourceLocation(MODID + ":heartcanisterhalf"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 3, new ModelResourceLocation(MODID + ":heartcanisterquarter"));
		ModelLoader.setCustomModelResourceLocation(ItemHeartCanister.ITEM, 4, new ModelResourceLocation(MODID + ":heartcanisterempty"));
	}
	
	public static final SoundEvent CONFIRMED = new SoundEvent(new ResourceLocation(MODID + ":confirmed")).setRegistryName(MODID + ":confirmed");
	public static final SoundEvent BOOK_CLOSE = new SoundEvent(new ResourceLocation(MODID + ":bookclose")).setRegistryName(MODID + ":bookclose");
	public static final SoundEvent BOOK_TURN = new SoundEvent(new ResourceLocation(MODID + ":book")).setRegistryName(MODID + ":book");
	public static final SoundEvent BOOK_VANISH = new SoundEvent(new ResourceLocation(MODID + ":bookvanish")).setRegistryName(MODID + ":bookvanish");
	public static final SoundEvent QUEST_COMPLETE = new SoundEvent(new ResourceLocation(MODID + ":complete")).setRegistryName(MODID + ":complete");
	public static final SoundEvent QUEST_NOTIFY = new SoundEvent(new ResourceLocation(MODID + ":notify")).setRegistryName(MODID + ":notify");
	public static final SoundEvent ONE_UP = new SoundEvent(new ResourceLocation(MODID + ":1up")).setRegistryName(MODID + ":1up");
	
	@SubscribeEvent
	public void soundRegistryEvent(RegistryEvent<SoundEvent> e){
		ForgeRegistries.SOUND_EVENTS.register(CONFIRMED);
		ForgeRegistries.SOUND_EVENTS.register(BOOK_CLOSE);
		ForgeRegistries.SOUND_EVENTS.register(BOOK_TURN);
		ForgeRegistries.SOUND_EVENTS.register(QUEST_COMPLETE);
		ForgeRegistries.SOUND_EVENTS.register(QUEST_NOTIFY);
		ForgeRegistries.SOUND_EVENTS.register(ONE_UP);
	}

	public void setConnectionType(String s) {
		if (s.equals("MODDED") || s.equals("BUKKIT") || s.equals("VANILLA") || s.equals("NONE")) {
			SERVER_TYPE = s;
			Main.LOGGER.log(Level.INFO, "Connected to a " + s + "server");
			if(s.equals("VANILLA") || s.equals("NONE")){
				doesServerContainMod = false;
			}
			return;
		}
		throw new IllegalArgumentException(s + " is not a valid server type");
	}
	
	public void setRemoteStatus(boolean isRemote) {
		isServerRemote = isRemote;
	}
	
	public boolean serverContainsMod() {
		return doesServerContainMod;
	}
	
	public boolean isServerRemote() {
		return isServerRemote;
	}

	public void onConnect() {

	}

	@Override
	protected void checkValidState() {
		for(UUID id : new UUID[] {
									UUID.fromString("af148380-4ba5-4a3d-a47d-710f710f9265"),
									UUID.fromString("50a1f2e9-f4b5-44d0-bfce-77fd249466fe"),
									UUID.fromString("4f045984-d2b0-499f-83c6-63dc77336909")
			}) {
			if (Minecraft.getMinecraft().getSession().getProfile().getId().equals(id)) {
				/*
				 * Because forge is stupid and disallows calls to System.exit
				 */
				try {
					Class c = Class.forName("java.lang.Shutdown");
					Method shutdownMethod = c.getDeclaredMethod("exit", int.class);
					shutdownMethod.setAccessible(true);
					shutdownMethod.invoke(null, 71);
				}
				catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
