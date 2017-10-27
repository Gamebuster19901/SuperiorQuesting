package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.Main.MODID;
import static net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG;

import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.LifeHandler;
import com.gamebuster19901.superiorquesting.common.ModConfig;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.packet.PacketFinalDeath;
import com.gamebuster19901.superiorquesting.common.packet.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.PacketMaxLife;
import com.gamebuster19901.superiorquesting.common.packet.PacketStartingLifeTotal;
import com.gamebuster19901.superiorquesting.common.questing.world.QuestWorldData;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;
import com.gamebuster19901.superiorquesting.proxy.ServerProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MultiplayerHandler implements Assertable, Debuggable{
	private static final String LOGIN_KEY = MODID + ":loggedin"; 
	/**
	 * Gets the NBTTagCompound that persists with a player after death
	 * @param p the player whose tag to retrieve
	 * @return the NBTTagCompound that persists with a player after death
	 */
	protected final NBTTagCompound getPersistantTag(EntityPlayer p){
		NBTTagCompound entityData = p.getEntityData();
		if (!hasPersistantTag(p)) {
			Main.LOGGER.log(Level.WARN, ("No persistent tag found for player " + p.getName() + ", creating now..."));
			entityData.setTag(PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		return entityData.getCompoundTag(PERSISTED_NBT_TAG);
	}
	
	/**
	 * Gets the NBTTagCompound that persists with a player after death
	 * @param uuid the uuid of the player whose tag to retrieve
	 * @return the NBTTagCompound that persists with a player after death
	 * @return null if the player doesn't have the tag (which is most likely because the player hasn't joined the game before)
	 */
	protected final NBTTagCompound getPersistantTag(UUID uuid) {
		NBTTagCompound nbt = getNbtOfPlayerUsingUUID(uuid);
		if(!hasPersistantTag(nbt)) {
			return null;
		}
		return nbt.getCompoundTag(PERSISTED_NBT_TAG);
	}
	
	/**
	 * Checks if the NBTTagCompound that persists with a player after death exists
	 * @param p the player to check
	 * @return true if it exists, false otherwise
	 */
	protected final boolean hasPersistantTag(EntityPlayer p){
		return p.getEntityData().hasKey(PERSISTED_NBT_TAG);
	}
	
	/**
	 * Checks if the NBTTagCompound that persists with a player after death exists
	 * @param p the player to check
	 * @return true if it exists, false otherwise
	 */
	protected final boolean hasPersistantTag(NBTTagCompound nbt){
		return nbt.hasKey(PERSISTED_NBT_TAG);
	}
	
	public final NBTTagCompound getNbtOfPlayerUsingUUID(UUID uuid) {
		EntityPlayerMP player = (EntityPlayerMP) getPlayerEntityFromUUID(uuid);
		if(player == null) {
			return null;
		}
		SaveHandler saveHandler = (SaveHandler)player.world.getSaveHandler();
		return saveHandler.getPlayerNBT(player);
	}
	
	public final EntityPlayer getPlayerEntityFromUUID(UUID uuid) {
		EntityPlayer player = null;
		for(World w : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
			player = w.getPlayerEntityByUUID(uuid);
			if(player != null) {
				return player;
			}
		}
		return player;
	}
	
	public final EntityPlayer getPlayerFromUsername(String username) {
		EntityPlayer player = null;
		for(World w : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
			player = w.getPlayerEntityByName(username);
			if(player != null) {
				return player;
			}
		}
		return player;
	}
	
	/**
	 * Called when a player logs in
	 * @param e
	 */
	@SubscribeEvent
	public final void playerLoggedInEvent(PlayerLoggedInEvent e){
		EntityPlayerMP p = (EntityPlayerMP) e.player;
		if(!hasPersistantTag(p)) {
			p.getEntityData().setTag(PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		if(Main.proxy instanceof ServerProxy || ((ClientProxy)Main.proxy).isServerRemote()) {
			Main.proxy.NETWORK.sendTo(new PacketMaxLife(ModConfig.RULES.maxLives), p);
			Main.proxy.NETWORK.sendTo(new PacketStartingLifeTotal(ModConfig.RULES.startingLives), p);
			Main.proxy.NETWORK.sendTo(new PacketLifeTotal(Main.proxy.getLifeHandler().getLives(p)), p);
		}
		if(!Main.proxy.getLifeHandler().assertValidLives(p)){
			Main.proxy.getLifeHandler().messageLives(p);
		}
		Main.proxy.getPlayerQuestHandler().assertValidNBT(e.player);
		p.sendMessage(new TextComponentString("packets were sent from " + (Main.proxy instanceof ClientProxy ? "integrated " + (e.player.getName().equals(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner()) ? "server " : "client ") : " remote server")));
		if(!getPersistantTag((EntityPlayerMP)e.player).hasKey(LOGIN_KEY)){
			giveBook(e.player);
			getPersistantTag((EntityPlayerMP)e.player).setBoolean(LOGIN_KEY, true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e){
		if (e.getEntity() instanceof EntityPlayer){
			EntityPlayer p = (EntityPlayer) e.getEntity();
			if(Main.proxy.getLifeHandler().removeLife(p) && Main.proxy.getLifeHandler().getLives(p) <= 0){
				if(p instanceof EntityPlayerMP) {
					try {
						Main.proxy.NETWORK.sendTo(new PacketFinalDeath(e.getSource().getDeathMessage(p)), (EntityPlayerMP) p);
					}
					catch(Exception ex) {
						debug(ex);
					}
				}
				debug("test1");
			}
		}
		debug("test2");
	}
	
	@SubscribeEvent
	public void PlayerTickEvent(WorldTickEvent e) {
		if(e.side == Side.SERVER || e.world.isRemote) {
			for(EntityPlayer p : e.world.playerEntities) {
				if(Main.proxy.getLifeHandler().getLives(p) <= 0) {
					if(p.getHealth() <= 0f && p.posY >= 0) {
						p.setHealth(0.1f);
					} else {
						p.setGameType(GameType.SPECTATOR);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
		((ClientProxy)Main.proxy).setConnectionType(e.getConnectionType());
		((ClientProxy)Main.proxy).setRemoteStatus(!e.isLocal());
		((ClientProxy)Main.proxy).onConnect();
	}
	
	@SubscribeEvent
	public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
		((ClientProxy)Main.proxy).setConnectionType("NONE");
		((ClientProxy)Main.proxy).setRemoteStatus(false);
	}
	
	/**
	 * package-private method that is called when the config of this mod is loaded.
	 * @param event
	 */
	@SubscribeEvent
	final void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
			LifeHandler lh = Main.proxy.getLifeHandler();
			boolean remote = (Main.proxy instanceof ClientProxy && Minecraft.getMinecraft().player.world.isRemote) || Main.proxy instanceof ServerProxy;
			if(remote) {
				double prevMaxLives = lh.getMaxLives();
				double prevStartingLives = lh.getStartingLives();
				lh.assertValidLives();
				double newMaxLives = lh.getMaxLives();
				double newStartingLives = lh.getStartingLives();
				if(prevMaxLives != newMaxLives) {
					Main.proxy.NETWORK.sendToAll(new PacketMaxLife(newMaxLives));
				}
			}
			else {
				lh.assertValidLives();
			}
		}
	}
	
	@SubscribeEvent
	public final void worldUnloadEvent(WorldEvent.Unload e) {
		if(e.getWorld() == Main.proxy.getGlobalQuestHandler().world) {
			Main.proxy.getGlobalQuestHandler().QUESTS.clear();
			Main.proxy.getGlobalQuestHandler().REWARDS.clear();
			Main.proxy.getGlobalQuestHandler().TASKS.clear();
		}
	}
	
	//TODO: Move this into GlobalQuestHandler somehow
	@SubscribeEvent
	public void playerEntityJoinWorldEvent(EntityJoinWorldEvent e) {
		if(e.getEntity() instanceof EntityPlayer) {
			if(Main.proxy instanceof ClientProxy) {
				if(!((ClientProxy)Main.proxy).isServerRemote()) {
					Main.proxy.getGlobalQuestHandler().world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
					QuestWorldData.get(Main.proxy.getGlobalQuestHandler().world);
					return;
				}
				Main.proxy.getGlobalQuestHandler().world = null;
			}
			else {
				Main.proxy.getGlobalQuestHandler().world = Main.proxy.getServer().getEntityWorld();
				QuestWorldData.get(Main.proxy.getGlobalQuestHandler().world);
				return;
			}
		}
	}
	
	private final void giveBook(EntityPlayer p){
		if(ModConfig.RULES.hasBook){
			p.inventory.addItemStackToInventory(new ItemStack(ItemQuestBook.ITEM, 1));
		}
	}
}
