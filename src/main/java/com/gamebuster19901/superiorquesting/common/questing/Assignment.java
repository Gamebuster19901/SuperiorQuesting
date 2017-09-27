package com.gamebuster19901.superiorquesting.common.questing;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.entity.player.EntityPlayer;

/**
 * An assignment is anything that can be completed, it has a title and description.
 * 
 * Some assignments may need to be ordered a specific way, so all assignments are Comparables.
 * 
 * Quests come first, then Tasks.
 * 
 * Currently, all Assignments are instances of Quest or Task, support may be added for
 * different types of assignments in future updates
 */
interface Assignment extends UpdatableSerializable{
	public String UNLOCKED = "UNLOCKED";
	public String COMPLETED = "COMPLETED";
	public String NOTIFIED = "NOTIFIED";
	
	/**
	 * Checks if the Assignment is unlocked for the player
	 * @param p the player to check
	 * @return true if the assignment is unlocked, false otherwise
	 */
	public boolean isUnlocked(EntityPlayer p);
	
	/**
	 * Checks if the Assignment is unlocked for the player
	 * @param p the uuid of the player to check
	 * @return true if the assignment is unlocked, false otherwise
	 */
	public boolean isUnlocked(UUID p);
	
	/**
	 * Checks if the player has completed this Assignment
	 * @param p the player to check
	 * @return true if the player has completed this assignment, false otherwise.
	 */
	public boolean hasFinished(EntityPlayer p);
	
	/**
	 * Checks if the player has completed this Assignment
	 * @param p the uuid of the player to check
	 * @return true if the player has completed this assignment, false otherwise.
	 */
	public boolean hasFinished(UUID p);
	
	/**
	 * Checks if the player has been notified that this Assignment has been completed.
	 * @param p the player to check
	 * @return true if the player has been notified, false otherwise.
	 */
	public boolean hasNotified(EntityPlayer p);
	
	/**
	 * Checks if the player has been notified that this Assignment has been completed.
	 * @param p the uuid of the player to check
	 * @return true if the player has been notified, false otherwise.
	 */
	public boolean hasNotified(UUID p);
	
	/**
	 * Completes this assignment for the player. This should complete any prerequisites, if applicable.
	 * @param p the player to complete
	 */
	public void finish(EntityPlayer p);
	
	/**
	 * Completes this assignment for the player. This should complete any prerequisites, if applicable.
	 * @param p uuid of the player to complete
	 */
	public void finish(UUID p);
	
	/**
	 * Locks this assignment for the player. The assignment will immediately unlock next tick if all
	 * prerequisites are completed.
	 * @param p the player to lock this assignment for
	 */
	public void lock(EntityPlayer p);
	
	/**
	 * Locks this assignment for the player. The assignment will immediately unlock next tick if all
	 * prerequisites are completed.
	 * @param p the uuid of the player to lock this assignment for
	 */
	public void lock(UUID p);
	
	/**
	 * Unlocks this assignment for the player.
	 * @param p the player to unlock this assignment for
	 */
	public void unlock(EntityPlayer p);
	
	/**
	 * Unlocks this assignment for the player.
	 * @param p the uuid of the player to unlock this assignment for
	 */
	public void unlock(UUID p);
	
	/**
	 * Hides this assignment from the player.
	 * @param p the player to hide this assignment from
	 */
	public void hide(EntityPlayer p);
	
	/**
	 * Hides this assignment from the player.
	 * @param p the uuid of the player to hide this assignment from
	 */
	public void hide(UUID p);
	
	/**
	 * Unides this assignment from the player.
	 * @param p the player to hide this assignment from
	 */
	public void unhide(EntityPlayer p);
	
	/**
	 * Unides this assignment from the player.
	 * @param p the uuid of the player to hide this assignment from
	 */
	public void unhide(UUID p);
	
	/**
	 * @return true if this assignment is locked by default, false otherwise
	 */
	public boolean isLockedByDefault();
	
	/**
	 * @return true if this assignment is hidden by default, false otherwise
	 */
	public boolean isHiddenByDefault();
	
	/**
	 * @return the title of this assignment
	 */
	public String getTitle();
	
	/**
	 * @return the description of this assignment
	 */
	public String getDescription();
	
	@Override
	public String toString();
}
