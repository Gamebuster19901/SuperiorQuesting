package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.entity.player.EntityPlayer;

interface Rewardable {
	public abstract boolean canAward(EntityPlayer p);
	
	/**
	 * @param p the player to award
	 * 
	 * if canAward(p), award the player
	 */
	public abstract void award(EntityPlayer p);
}
