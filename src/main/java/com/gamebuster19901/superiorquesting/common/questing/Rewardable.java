package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.entity.player.EntityPlayer;

/**
 * A Rewardable is anything that can give a reward.
 */

interface Rewardable {
	/**
	 * If the reward can be given. Should check that all prerequisites are complete
	 * and that the reward has not been given out yet. May also need to check if
	 * there is enough inventory space in the player's inventory, and that the
	 * player is not dead.
	 * 
	 * @param p the player check
	 * @return true if this can be rewarded, false otherwise.
	 */
	public abstract boolean canAward(EntityPlayer p);
	
	/**
	 * @param p the player to award
	 * 
	 * if canAward(p), award the player with a reward.
	 */
	public abstract void award(EntityPlayer p);
	
	/**
	 * 
	 * @param p the player to check
	 * @return true if the player has collected this reward, false otherwise
	 */
	public abstract boolean hasCollected(EntityPlayer p);
}
