package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Assertable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public abstract class Task implements Assignment, Assertable{
	
	private Quest parent;
	private String title;
	private String description;
	private int order;
	private boolean lockedByDefault = true;
	private boolean hiddenByDefault = false;
	
	Task(Quest parent, String title, String description, int order){
		this.parent = parent;
		this.title = title;
		this.description = description;
		this.order = order;
	}
	
	Task(Quest parent, String title, String description, int order, boolean lockedByDefault, boolean hiddenByDefault){
		this(parent, title, description, order);
		this.lockedByDefault = lockedByDefault;
		this.hiddenByDefault = hiddenByDefault;
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
	public void convert(long prevVersion, long nextVersion, ObjectInputStream in) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void writeObject(ObjectOutputStream out) throws IOException {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		Assert(false, "Not yet implemented");
	}

	@Override
	public final void updateStatus(WorldTickEvent e) {
		for(EntityPlayer p : e.world.playerEntities) {
			if(!isFinished(p)) {
				if(isUnlocked(p)) {
					if(areConditionsSatisfied(p)) {
						markFinished(p);
					}
				}
				else {
					for(Task t : parent.tasks()) {
						if(t == this) {
							unlock(p);
							return;
						}
						else if (!t.isFinished(p)) {
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public abstract boolean areConditionsSatisfied(EntityPlayer p);

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
}
