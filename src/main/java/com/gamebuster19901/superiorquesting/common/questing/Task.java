package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Task implements Assignment{
	
	private String title;
	private String description;
	
	Task(String title, String description){
		this.title = title;
		this.description = description;
	}
	
	@Override
	public final void finish(EntityPlayer p) {
		//mark this task as finished for the player
	}

	@Override
	public final String getTitle() {
		return title;
	}

	@Override
	public final String getDescription() {
		return description;
	}

	/**
	 * @see Assignment.compareTo
	 * 
	 * @throws ClassCastException if o !instanceof Assignment
	 */
	@Override
	public final int compareTo(Object o) {
		if(o instanceof Task) {
			return 0;
		}
		if(o instanceof Quest) {
			return 1;
		}
		else{
			return(((Assignment) o).compareTo(this)) * -1;
		}
	}
	
}
