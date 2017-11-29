package com.gamebuster19901.superiorquesting.common.questing.integrate;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public interface Lockable {
	
	/**
	 * Locks this for the player. this will immediately unlock next tick if all
	 * prerequisites are completed.
	 * @param p the player to lock this for
	 */
	public void lock(EntityPlayer p);
	
	/**
	 * Locks this for the player. this will immediately unlock next tick if all
	 * prerequisites are completed.
	 * @param p the uuid of the player to lock this for
	 */
	public void lock(UUID p);
	
	/**
	 * Unlocks this for the player.
	 * @param p the player to unlock this for
	 */
	public void unlock(EntityPlayer p);
	
	/**
	 * Unlocks this for the player.
	 * @param p the uuid of the player to unlock this for
	 */
	public void unlock(UUID p);
	
	/**
	 * Checks if this is marked as unlocked for the player
	 * @param p the player to check
	 * @return true if this is unlocked, false otherwise
	 */
	public boolean isUnlocked(EntityPlayer p);
	
	/**
	 * Checks if this is marked as unlocked for the player
	 * @param p the uuid of the player to check
	 * @return true if this is unlocked, false otherwise
	 */
	public boolean isUnlocked(UUID p);
	
	/**
	 * @return true if this is locked by default, false otherwise
	 */
	public boolean isLockedByDefault();
}
