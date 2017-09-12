package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Task implements Assignment{

	@Override
	public final void finish(EntityPlayer p) {
		//mark this task as finished for the player
	}

	@Override
	public final String getTitle() {
		//get the title of this task 
	}

	@Override
	public final String getDescription() {
		//get the description of this task
	}
	
}
