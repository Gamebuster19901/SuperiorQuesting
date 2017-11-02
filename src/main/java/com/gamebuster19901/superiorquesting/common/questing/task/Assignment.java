package com.gamebuster19901.superiorquesting.common.questing.task;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;
import com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler;
import com.gamebuster19901.superiorquesting.common.questing.PlayerQuestHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * An assignment is anything that can be completed, it has a title and description.
 * 
 * Currently, all Assignments are instances of Quest or Task, support may be added for
 * different types of assignments in future updates
 */
public interface Assignment extends UpdatableSerializable, Unique{
	public String UNLOCKED = "UNLOCKED";
	public String COMPLETED = "COMPLETED";
	public String HIDDEN = "HIDDEN";
	public String NOTIFIED = "NOTIFIED";
	
	/*
	 * 
	 * 
	 * Finishing
	 * 
	 * 
	 */
	
	/**
	 * Checks if this assignment is finished by checking if it's prerequisites are finished and 
	 * if it's own conditions are complete, then marks it as such every tick.
	 * 
	 * @param e World Tick Event
	 */
	@SubscribeEvent
	public void updateStatus(WorldTickEvent e);
	
	public boolean areConditionsSatisfied(EntityPlayer p);
	
	/**
	 * Checks if this assignment is marked as completed for the player
	 * @param p the player to check
	 * @return true if the player has completed this assignment, false otherwise.
	 */
	public boolean isFinished(EntityPlayer p);
	
	/**
	 * Checks if this assignment is marked as completed for the player
	 * @param p the uuid of the player to check
	 * @return true if the player has completed this assignment, false otherwise.
	 */
	public boolean isFinished(UUID p);
	
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
	 * Marks this assignment as finished for the player, does not complete any prerequisites
	 * @param p the player to mark this assignment as complete for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markFinished(EntityPlayer p);
	
	/**
	 * Marks this assignment as finished for the player, does not complete any prerequisites
	 * @param p the UUID of the player to mark this assignment as complete for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markFinished(UUID p);
	
	/**
	 * Marks this assignment as incomplete for the player
	 * @param p the the player to mark this assignment as incomplete for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markUnFinished(EntityPlayer p);
	
	/**
	 * Marks this assignment as incomplete for the player
	 * @param p the UUID of the player to mark this assignment as incomplete for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markUnfinished(UUID p);
	
	/*
	 * 
	 * 
	 * Notifications
	 * 
	 * 
	 */
	
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
	 * Notifies the player that this assignment has been completed
	 * @param p the player to notify
	 */
	public void notify(EntityPlayer p);
	
	/**
	 * Marks this assignment as notified for the player
	 * @param p the player to mark this assignment as notified for
	 */
	public void markNotified(EntityPlayer p);
	
	/**
	 * Marks this assignment as notified for the player
	 * @param p the uuid of the player to mark this assignment as notified for
	 */
	public void markNotified(UUID p);
	
	/**
	 * Marks this assignment as unnotified for the player
	 * @param p the player to mark this assignment as unnotified for
	 */
	public void markUnnotified(EntityPlayer p);
	
	/**
	 * Marks this assignment as unnotified for the player
	 * @param p the uuid of the player to mark this assignment as unnotified for
	 */
	public void markUnnotified(UUID p);
	
	
	/*
	 * 
	 * 
	 * Hiding
	 * 
	 * 
	 */
	
	/**
	 * Checks if the assignment is marked as hidden for the player
	 * @param p the player to check
	 * @return true if this assignment is hidden for the player, false otherwise
	 */
	public boolean isHidden(EntityPlayer p);
	
	/**
	 * Checks if the assignment is marked as hidden for the player
	 * @param p the uuid of the player to check
	 * @return true if this assignment is hidden for the player, false otherwise
	 */
	public boolean isHidden(UUID p);
	
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
	 * @return true if this assignment is hidden by default, false otherwise
	 */
	public boolean isHiddenByDefault();
	
	/*
	 * 
	 * 
	 * Locking
	 * 
	 * 
	 */
	
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
	 * Checks if the Assignment is marked as unlocked for the player
	 * @param p the player to check
	 * @return true if the assignment is unlocked, false otherwise
	 */
	public boolean isUnlocked(EntityPlayer p);
	
	/**
	 * Checks if the Assignment is marked as unlocked for the player
	 * @param p the uuid of the player to check
	 * @return true if the assignment is unlocked, false otherwise
	 */
	public boolean isUnlocked(UUID p);
	
	/**
	 * @return true if this assignment is locked by default, false otherwise
	 */
	public boolean isLockedByDefault();
	
	/*
	 *
	 * 
	 * Data
	 * 
	 * 
	 */
	
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
	
	public default GlobalQuestHandler getGlobalQuestHandler() {
		return Main.proxy.getGlobalQuestHandler();
	}
	
	public default PlayerQuestHandler getPlayerQuestHandler() {
		return Main.proxy.getPlayerQuestHandler();
	}
	
	public UUID getUUID();
}
