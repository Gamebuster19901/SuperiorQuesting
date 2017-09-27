package com.gamebuster19901.superiorquesting.common.questing;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.entity.player.EntityPlayer;

/**
 * A Rewardable is anything that can give a reward.
 */

interface Rewardable extends UpdatableSerializable{
	public String COLLECTED = "COLLECTED";
	
	/**
	 * If the reward can be given. Should check that all prerequisites are complete
	 * and that the reward has not been given out yet. May also need to check if
	 * there is enough inventory space in the player's inventory, and that the
	 * player is not dead.
	 * 
	 * @param p the player check
	 * @return true if this can be rewarded, false otherwise.
	 */
	public boolean canCollect(EntityPlayer p);
	
	/**
	 * @param p the player to award
	 * 
	 * if canAward(p), award the player with a reward.
	 */
	public void collect(EntityPlayer p);
	
	/**
	 * @param p the player to check
	 * @return true if the player has collected this reward, false otherwise
	 */
	public boolean hasCollected(EntityPlayer p);
	
	/**
	 * @param p the uuid of the player to check
	 * @return true if the player has collected this reward, false otherwise
	 */
	public boolean hasCollected(UUID p);
}
