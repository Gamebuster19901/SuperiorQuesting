package com.gamebuster19901.superiorquesting.common.questing.task;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.questing.Quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public abstract class Task implements Assignment, Assertable{
	
	private UUID id;
	private UUID parent;
	private String title;
	private String description;
	private boolean lockedByDefault = true;
	private boolean hiddenByDefault = false;
	
	Task(MinecraftServer server, Quest parent, String title, String description, int order){
		this.id = UUID.randomUUID();
		this.parent = parent.getUUID();
		this.title = title;
		this.description = description;
	}
	
	Task(MinecraftServer server, Quest parent, String title, String description, int order, boolean lockedByDefault, boolean hiddenByDefault){
		this(server, parent, title, description, order);
		this.lockedByDefault = lockedByDefault;
		this.hiddenByDefault = hiddenByDefault;
	}
	
	Task(MinecraftServer server, NBTTagCompound data){
		this.deserializeNBT(data);
	}

	@Override
	public final String getTitle() {
		return title;
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public abstract boolean areConditionsSatisfied(EntityPlayer p);

	/*
	 *FINISHED 
	 */
	@Override
	public boolean isFinished(EntityPlayer p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isFinished(UUID p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public void finish(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void finish(UUID p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void markFinished(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void markFinished(UUID p) {
		Assert(false, "Not yet implemented");
	}
	
	@Override
	public void markUnfinished(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}
	
	@Override
	public void markUnfinished(UUID p) {
		Assert(false, "Not yet implemented");
	}

	/*
	 * NOTIFY
	 */
	@Override
	public boolean hasNotified(EntityPlayer p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public boolean hasNotified(UUID p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public void notify(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void markNotified(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void markNotified(UUID p) {
		Assert(false, "Not yet implemented");
	}

	/*
	 * HIDE
	 */
	
	@Override
	public boolean isHidden(EntityPlayer p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isHidden(UUID p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public void hide(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void hide(UUID p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void unhide(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void unhide(UUID p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isHiddenByDefault() {
		return hiddenByDefault;
	}

	/*
	 * LOCK
	 */
	
	@Override
	public void lock(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void lock(UUID p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void unlock(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void unlock(UUID p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isUnlocked(EntityPlayer p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isUnlocked(UUID p) {
		return Assert(false, "Not yet implemented");
	}

	@Override
	public boolean isLockedByDefault() {
		// TODO Auto-generated method stub
		return lockedByDefault;
	}
	
	/*
	 * OTHER TASK DATA
	 */

	@Override
	public final UUID getUUID() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public final UUID getParent() {
		return parent;
	}
	
	public final String toString() {
		return getUUID().toString();
	}
}
