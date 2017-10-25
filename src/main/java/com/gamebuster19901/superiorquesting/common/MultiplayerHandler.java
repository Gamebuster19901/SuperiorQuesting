package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;
import static net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.packet.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.PacketMaxLife;
import com.gamebuster19901.superiorquesting.common.packet.PacketStartingLifeTotal;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public abstract class MultiplayerHandler implements Assertable{
	/**
	 * Gets the NBTTagCompound that persists with a player after death
	 * @param p the player whose tag to retrieve
	 * @return the NBTTagCompound that persists with a player after death
	 */
	protected final NBTTagCompound getPersistantTag(EntityPlayer p){
		NBTTagCompound entityData = p.getEntityData();
		Assert(hasPersistantTag(p),("No persistent tag found for player " + p.getName()));
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
	 * Used to make sure that values are still within bounds when changed
	 */
	protected abstract void onConfigFinishChanged();
	
	protected abstract void playerLoggedIn(PlayerLoggedInEvent e);
	
	/**
	 * Called when a player logs in
	 * @deprecated for internal use only
	 * @param e
	 */
	@SubscribeEvent
	public final void playerLoggedInEvent(PlayerLoggedInEvent e){
		playerLoggedIn(e);
	}
	
	@SubscribeEvent
	public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
		((ClientProxy)Main.proxy).setConnectionType(e.getConnectionType());
		((ClientProxy)Main.proxy).setRemoteStatus(!e.isLocal());
		((ClientProxy)Main.proxy).onConnect();
	}
	
	@SubscribeEvent
	public void playerEntityJoinWorldEvent(EntityJoinWorldEvent e) {
		if(e.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP p = (EntityPlayerMP)e.getEntity();
			if(!hasPersistantTag(p)) {
				p.getEntityData().setTag(PERSISTED_NBT_TAG, new NBTTagCompound());
			}
			Main.proxy.NETWORK.sendTo(new PacketMaxLife(ModConfig.RULES.maxLives), p);
			Main.proxy.NETWORK.sendTo(new PacketStartingLifeTotal(ModConfig.RULES.startingLives), p);
			Main.proxy.NETWORK.sendTo(new PacketLifeTotal(Main.proxy.getLifeHandler().getLives(p)), p);
			p.sendMessage(new TextComponentString("sent packets"));
			p.sendMessage(new TextComponentString("what the fuck"));
		}
	}
	
	@SubscribeEvent
	public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
		((ClientProxy)Main.proxy).setConnectionType("NONE");
		((ClientProxy)Main.proxy).setRemoteStatus(false);
	}
	
	/**
	 * package-private method that is called when the config of this mod is loaded.
	 * @deprecated for internal use only
	 * @param event
	 */
	@SubscribeEvent
	final void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
			onConfigFinishChanged();
		}
	}
}
