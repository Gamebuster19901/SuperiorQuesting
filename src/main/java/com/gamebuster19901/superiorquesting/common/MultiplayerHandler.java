package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public abstract class MultiplayerHandler implements Assertable{
	/**
	 * Gets the NBTTagCompound that persists with a player after death
	 * @param p the player whose tag to retrieve
	 * @return the NBTTagCompound that persists with a player after death
	 */
	protected final NBTTagCompound getPersistantTag(EntityPlayer p){
		NBTTagCompound entityData = p.getEntityData();
		NBTTagCompound persist;
		Assert(hasPersistantTag(p),("No persistent tag found for player " + p.getName()));
		return persist = entityData.getCompoundTag(p.PERSISTED_NBT_TAG);
	}
	
	/**
	 * Checks if the NBTTagCompound that persists with a player after death exists
	 * @param p the player to check
	 * @return true if it exists, false otherwise
	 */
	protected final boolean hasPersistantTag(EntityPlayer p){
		return p.getEntityData().hasKey(p.PERSISTED_NBT_TAG);
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
	final void playerLoggedInEvent(PlayerLoggedInEvent e){
		EntityPlayerMP p = (EntityPlayerMP)e.player;
		if (!hasPersistantTag(p)){
			p.getEntityData().setTag(p.PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		NBTTagCompound nbt = getPersistantTag(p);
		playerLoggedIn(e);
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
