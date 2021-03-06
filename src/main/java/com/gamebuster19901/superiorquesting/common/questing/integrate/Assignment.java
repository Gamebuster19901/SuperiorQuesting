package com.gamebuster19901.superiorquesting.common.questing.integrate;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
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
public interface Assignment extends Lockable, Hideable, Notifyable{
	public String COMPLETED = "COMPLETED";
	
	
	
	/**
	 * Check if this assignment is finished by checking if it's prerequisites are finished and 
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
	public void markUnfinished(EntityPlayer p);
	
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
	
	public default GlobalQuestHandler getGlobalQuestHandler() {
		return Main.proxy.getGlobalQuestHandler();
	}
	
	public default PlayerQuestHandler getPlayerQuestHandler() {
		return Main.proxy.getPlayerQuestHandler();
	}
}
