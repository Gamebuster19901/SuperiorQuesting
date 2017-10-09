package com.gamebuster19901.superiorquesting.common.questing;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A Rewardable is anything that can give a reward.
 */

interface Rewardable extends UpdatableSerializable, Unique{
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
	
	/**
	 * Marks this rewardable as collected for the player
	 * @param p the player to mark this rewardable as collected for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markCollected(EntityPlayer p);
	
	/**
	 * Marks this rewardable as collected for the player
	 * @param p the uuid of the player to mark this rewardable as collected for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markCollected(UUID p);
	
	/**
	 * Marks this rewardable as uncollected for the player
	 * @param p the player to mark this rewardable as uncollected for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markUncollected(EntityPlayer p);
	
	/**
	 * Marks this rewardable as uncollected for the player
	 * @param p the uuid of the player to mark this rewardable as uncollected for
	 * @deprecated for internal use only
	 */
	@Deprecated
	public void markUncollected(UUID p);
	
	public UUID getUUID();
	
	public default QuestHandler getQuestHandler() {
		return Main.proxy.getQuestHandler();
	}
}
