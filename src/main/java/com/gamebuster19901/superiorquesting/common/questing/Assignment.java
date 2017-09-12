package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.entity.player.EntityPlayer;

interface Assignment {
	public abstract boolean isFinished(EntityPlayer p);
	
	public abstract void finish(EntityPlayer p);
	
	public abstract String getTitle();
	
	public abstract String getDescription();
}
