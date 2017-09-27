package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Task implements Assignment{
	
	private String title;
	private String description;
	private final boolean lockedByDefault = true;
	private final boolean hiddenByDefault = false;
	
	Task(String title, String description){
		this.title = title;
		this.description = description;
	}
	
	@Override
	public final void finish(EntityPlayer p) {
		//mark this task as finished for the player
	}
	
	@Override
	public final void finish(UUID p) {
		//mark this task as finished for the player
	}
	
	@Override
	public final void lock(EntityPlayer p) {
		
	}
	
	@Override
	public final void lock(UUID p) {
		
	}
	
	@Override
	public final void unlock(EntityPlayer p) {
		
	}
	
	@Override
	public final void unlock(UUID p) {
		
	}
	
	@Override
	public final boolean isUnlocked(EntityPlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final boolean isUnlocked(UUID p) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean hasFinished(EntityPlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean hasFinished(UUID p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final boolean hasNotified(EntityPlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final boolean hasNotified(UUID p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final void hide(EntityPlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final void hide(UUID p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final void unhide(EntityPlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final void unhide(UUID p) {
		// TODO Auto-generated method stub
	}

	@Override
	public final boolean isLockedByDefault() {
		return lockedByDefault;
	}

	@Override
	public final boolean isHiddenByDefault() {
		return hiddenByDefault;
	}

	@Override
	public final String getTitle() {
		return title;
	}

	@Override
	public final String getDescription() {
		return description;
	}
}
