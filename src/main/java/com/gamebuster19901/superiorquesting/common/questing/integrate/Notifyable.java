package com.gamebuster19901.superiorquesting.common.questing.integrate;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.entity.player.EntityPlayer;

public interface Notifyable extends UpdatableSerializable, Unique{
	
	/**
	 * Checks if the player has been notified that this has been completed.
	 * @param p the player to check
	 * @return true if the player has been notified, false otherwise.
	 */
	public boolean hasNotified(EntityPlayer p);
	
	/**
	 * Checks if the player has been notified that this has been completed.
	 * @param p the uuid of the player to check
	 * @return true if the player has been notified, false otherwise.
	 */
	public boolean hasNotified(UUID p);
	
	/**
	 * Notifies the player that this has been completed
	 * @param p the player to notify
	 */
	public void notify(EntityPlayer p);
	
	/**
	 * Marks this as notified for the player
	 * @param p the player to mark this as notified for
	 */
	public void markNotified(EntityPlayer p);
	
	/**
	 * Marks this as notified for the player
	 * @param p the uuid of the player to mark this as notified for
	 */
	public void markNotified(UUID p);
	
	/**
	 * Marks this as unnotified for the player
	 * @param p the player to mark this as unnotified for
	 */
	public void markUnnotified(EntityPlayer p);
	
	/**
	 * Marks this as unnotified for the player
	 * @param p the uuid of the player to mark this as unnotified for
	 */
	public void markUnnotified(UUID p);
}
