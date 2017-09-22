package com.gamebuster19901.superiorquesting.common.questing;

import java.io.Serializable;

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
interface Assignment extends Comparable, Serializable{
	/**
	 * Checks if the player has completed this Assignment
	 * @param p the player to check
	 * @return true if the player has completed this assignment, false otherwise.
	 */
	public boolean hasFinished(EntityPlayer p);
	
	/**
	 * Checks if the player has been notified that this Assignment has been completed.
	 * @param p the player to check
	 * @return true if the player has been notified, false otherwise.
	 */
	public boolean hasNotified(EntityPlayer p);
	
	/**
	 * Completes this assignment for the player. This should complete any prerequisites, if applicable.
	 * @param p the player to complete
	 */
	public void finish(EntityPlayer p);
	
	/**
	 * @return the title of this assignment
	 */
	public String getTitle();
	
	/**
	 * @return the description of this assignment
	 */
	public String getDescription();
	
	/**
	 * Used to order different types of assignments.
	 * 
	 * Quests should always be less than everything else, so they come first in lists. Then Tasks come second.
	 * 
	 * @see Comparable.compareTo
	 */
	@Override
	public int compareTo(Object o);
	
	@Override
	public String toString();
}
