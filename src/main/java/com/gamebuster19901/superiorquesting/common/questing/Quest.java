package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class Quest implements Rewardable, Assignment, Debuggable, Assertable{
	private static final long VERSION = 0;
	private UUID id;
	private String title;
	private String description;
	private int page;
	private int x;
	private int y;
	private byte important = 1;
	private boolean lockedByDefault = false;
	private boolean hiddenByDefault = false; //a quest that is hidden is not visible unless all prerequisite quests are completed.
	private ArrayList<UUID> rewards = new ArrayList<UUID>();
	private ArrayList<UUID> prerequisites = new ArrayList<UUID>();
	private ArrayList<UUID> tasks = new ArrayList<UUID>();
	
	/**
	 * @param title the title of the quest
	 * @param description the description of the quest
	 * @param page the page the quest is located on
	 * @param x the x-coordinate the quest is located at on the page
	 * @param y the y-coordinate the quest is located at on the page
	 * @param rewards the rewards the quest gives once completed
	 * @param assignments the tasks a player must complete to collect rewards or continue
	 * 
	 * @throws IndexOutOfBoundsException if x or y < 0
	 */
	public Quest(String title, String description, int page, int x, int y, byte important, Collection<UUID> rewards, Collection<UUID> prerequisites, Collection<UUID> tasks) {
		try {
			if(title == null) {
				throw new NullPointerException("String title");
			}
			if(description == null) {
				throw new NullPointerException("String description");
			}
			if (x < 0) {
				throw new IndexOutOfBoundsException(x + " < 0. (x)");
			}
			if(y < 0) {
				throw new IndexOutOfBoundsException(y + " < 0. (y)");
			}
			if(page < 0) {
				throw new IndexOutOfBoundsException(page + " < 0. (page)");
			}
			if(rewards == null) {
				throw new NullPointerException("Collection<UUID> rewards");
			}
			if(prerequisites == null) {
				throw new NullPointerException("Collection<UUID> prerequisites");
			}
			if(tasks == null) {
				throw new NullPointerException("Collection<UUID> tasks");
			}
		}
		catch(RuntimeException e) {
			throw new IllegalArgumentException("Programming error, report to Gamebuster1990", e);
		}
		this.id = UUID.randomUUID();
		this.title = title;
		this.description = description;
		this.page = page;
		this.x = x;
		this.y = y;
		this.important = important;
		this.rewards = new ArrayList<UUID>(rewards);
		this.prerequisites = new ArrayList<UUID>(prerequisites);
		this.tasks = new ArrayList<UUID>(tasks);
		getQuestHandler().add(this);
	}
	
	private Quest(UUID id, String title, String description, int page, int x, int y, byte important, Collection<UUID> rewards, Collection<UUID> prerequisites, Collection<UUID> tasks) {
		try {
			if(id == null) {
				throw new NullPointerException("UUID id");
			}
			if(title == null) {
				throw new NullPointerException("String title");
			}
			if(description == null) {
				throw new NullPointerException("String description");
			}
			if (x < 0) {
				throw new IndexOutOfBoundsException(x + " < 0. (x)");
			}
			if(y < 0) {
				throw new IndexOutOfBoundsException(y + " < 0. (y)");
			}
			if(page < 0) {
				throw new IndexOutOfBoundsException(page + " < 0. (page)");
			}
			if(rewards == null) {
				throw new NullPointerException("Collection<UUID> rewards");
			}
			if(prerequisites == null) {
				throw new NullPointerException("Collection<UUID> prerequisites");
			}
			if(tasks == null) {
				throw new NullPointerException("Collection<UUID> tasks");
			}
		}
		catch(RuntimeException e) {
			throw new IllegalArgumentException("Programming error, report to Gamebuster1990", e);
		}
		this.id = UUID.randomUUID();
		this.title = title;
		this.description = description;
		this.page = page;
		this.x = x;
		this.y = y;
		this.important = important;
		this.rewards = new ArrayList<UUID>(rewards);
		this.prerequisites = new ArrayList<UUID>(prerequisites);
		this.tasks = new ArrayList<UUID>(tasks);
		getQuestHandler().add(this);
	}
	
	public Quest(String title, String description, int page, int x, int y, byte important) {
		try {
			if(title == null) {
				throw new NullPointerException("String title");
			}
			if(description == null) {
				throw new NullPointerException("String description");
			}
			if (x < 0) {
				throw new IndexOutOfBoundsException(x + " < 0. (x)");
			}
			if(y < 0) {
				throw new IndexOutOfBoundsException(y + " < 0. (y)");
			}
			if(page < 0) {
				throw new IndexOutOfBoundsException(page + " < 0. (page)");
			}
		}
		catch(RuntimeException e) {
			throw new IllegalArgumentException("Programming error, report to Gamebuster1990", e);
		}
		this.id = UUID.randomUUID();
		this.title = title;
		this.description = description;
		this.page = page;
		this.x = x;
		this.y = y;
		this.important = important;
		getQuestHandler().add(this);
	}
	
	public Quest(NBTTagCompound data) {
		this.deserializeNBT(data);
		getQuestHandler().add(this);
	}
	
	/*
	 * 
	 * 
	 * Finishing
	 * 
	 * 
	 */
	@Override
	public void updateStatus(WorldTickEvent e) {
		for(EntityPlayer p : e.world.playerEntities) {
			if(!isFinished(p)) {
				if(isUnlocked(p)) {
					if(areConditionsSatisfied(p)) {
						markFinished(p);
						if(this.rewards.size() == 0) {
							this.markCollected(p);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean areConditionsSatisfied(EntityPlayer p) {
		if(this.isUnlocked(p)) {
			for(UUID q : prerequisites) {
				if(!getQuestHandler().getQuest(q).isFinished(p)) {
					return false;
				}
			}
			for(UUID t : tasks) {
				if(!getQuestHandler().getTask(t).isFinished(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFinished(EntityPlayer p) {
		return getQuestHandler().getQuestNBT(getUUID(), p).getBoolean(COMPLETED);
	}
	
	@Override
	public boolean isFinished(UUID p) {
		if(getQuestHandler().getNbtOfPlayerUsingUUID(p) != null) {
			return getQuestHandler().getQuestNBT(getUUID(), p).getBoolean(COMPLETED);
		}
		return false;
	}
	
	/**
	 * causes a player to complete all prerequisites for this quest (including other quests), then completes this quest
	 * 
	 * @param p the player to complete this quest on
	 */
	@Override
	public void finish(EntityPlayer p) {
		for(UUID q : prerequisites) {
			getQuestHandler().getQuest(q).finish(p);
		}
		for(UUID t : tasks) {
			getQuestHandler().getTask(t).finish(p);
		}
		unlock(p);
		markFinished(p);
	}
	
	/**
	 * causes a player to complete all prerequisites for this quest (including other quests), then completes this quest
	 * 
	 * @param p the uuid of the player to complete this quest on
	 */
	@Override
	public void finish(UUID p) {
		for(UUID q : prerequisites) {
			getQuestHandler().getQuest(q).finish(p);
		}
		for(UUID t : tasks) {
			getQuestHandler().getTask(t).finish(p);
		}
		unlock(p);
		markFinished(p);
	}

	@Override
	@Deprecated
	public void markFinished(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void markFinished(UUID p) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * 
	 * 
	 * Collecting
	 * 
	 * 
	 */
	
	/**
	 * returns true if the player has collected the reward for this quest, false otherwise
	 * @param p the player to check
	 */
	@Override
	public boolean hasCollected(EntityPlayer p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(COLLECTED);
	}
	
	@Override
	public boolean hasCollected(UUID p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(COLLECTED);
	}

	/**
	 * if the player can collect rewards for this quest
	 * 
	 * @param p the player to check
	 * 
	 * @return false if they have already collected the reward or they have not finished the quest, true otherwise
	 */
	@Override
	public boolean canCollect(EntityPlayer p) {
		if(hasCollected(p)) {
			return false;
		}
		return isFinished(p);
	}

	/**
	 * Awards the player
	 * 
	 * @param p the player to Award
	 */
	@Override
	public void collect(EntityPlayer p) {
		for(UUID r : rewards) {
			this.getQuestHandler().getReward(r).collect(p);
		}
		markCollected(p);
	}
	
	@Override
	@Deprecated
	public void markCollected(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, true);
	}

	@Override
	@Deprecated
	public void markCollected(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, true);
	}
	
	@Override
	@Deprecated
	public void markUncollected(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, false);
	}

	@Override
	@Deprecated
	public void markUncollected(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, false);
	}
	
	/*
	 *
	 * 
	 * Notifications
	 * 
	 * 
	 */
	
	@Override
	public boolean hasNotified(EntityPlayer p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(NOTIFIED);
	}
	
	@Override
	public boolean hasNotified(UUID p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(NOTIFIED);
	}
	
	@Override
	public void notify(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	public void markNotified(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markNotified(UUID p) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * 
	 * 
	 * Locking
	 * 
	 * 
	 */
	
	@Override
	public boolean isUnlocked(EntityPlayer p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public boolean isUnlocked(UUID p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public void lock(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void lock(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void unlock(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, true);
	}

	@Override
	public void unlock(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, true);
	}
	
	@Override
	public boolean isLockedByDefault() {
		return lockedByDefault;
	}
	
	/*
	 * 
	 * 
	 * Hiding
	 * 
	 * 
	 */
	
	@Override
	public boolean isHidden(EntityPlayer p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}

	@Override
	public boolean isHidden(UUID p) {
		return getQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}
	
	@Override
	public void hide(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void hide(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void unhide(EntityPlayer p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
	}

	@Override
	public void unhide(UUID p) {
		getQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
	}
	
	@Override
	public boolean isHiddenByDefault() {
		return hiddenByDefault;
	}
	
	/*
	 * 
	 * 
	 * Quest data
	 * 
	 * 
	 */
	
	/**
	 * @return the title of this quest
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description of this quest
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * adds a prerequisite to this quest, a prerequisite is another Quest or a Task
	 * @param a the assignment to add
	 */
	void addPrerequisite(UUID q) {
		prerequisites.add(q);
	}
	
	void addTask(UUID t) {
		prerequisites.add(t);
	}
	
	/**
	 * removes a prerequisite from this quest
	 * @param a the assignment to remove
	 */
	void removePrerequisite(UUID q) {
		prerequisites.remove(q);
	}
	
	/**
	 * adds a reward to this quest
	 * @param r the reward to add
	 */
	void addReward(UUID r) {
		rewards.add(r);
	}
	
	/**
	 * removes a reward from this quest
	 * @param r the reward to remove
	 */
	void removeReward(UUID r) {
		rewards.remove(r);
	}
	
	@Override
	public String toString() {
		return getTitle() + " [" + id + "]";
	}

	@Override
	public QuestHandler getQuestHandler() {
		return Assignment.super.getQuestHandler();
	}
	
	@Override
	public void convert(long prevVersion, long nextVersion, NBTTagCompound in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagList prereqs = new NBTTagList();
		NBTTagList rews = new NBTTagList();
		NBTTagList tsks = new NBTTagList();
		
		for(int i = 0; i < prerequisites.size(); i++) {
			prereqs.appendTag(new NBTTagString(prerequisites.get(i).toString()));
		}
		for(int i = 0; i < rewards.size(); i++) {
			rews.appendTag(new NBTTagString(rewards.get(i).toString()));
		}
		for(int i = 0; i < tasks.size(); i++) {
			tsks.appendTag(new NBTTagString(tasks.get(i).toString()));
		}
		
		NBTTagCompound data = new NBTTagCompound();
		
		data.setLong("VERSION", VERSION);
		data.setUniqueId("UUID", id);
		data.setString("TITLE", title);
		data.setString("DESCRIPTION", description);
		data.setInteger("PAGE", page);
		data.setInteger("X", x);
		data.setInteger("Y", y);
		data.setByte("IMPORTANT", important);
		data.setBoolean("LOCKEDBYDEFAULT", lockedByDefault);
		data.setBoolean("HIDDENBYDEFAULT", hiddenByDefault);
		data.setTag("PREREQUISITES", prereqs);
		data.setTag("REWARDS", rews);
		data.setTag("TASKS", tsks);
		
		return data;
	}

	@Override
	public void deserializeNBT(NBTTagCompound data) {
		long ver = data.getLong("VERSION");
		if(ver != VERSION) {
			convert(ver, VERSION, data);
		}
		else {
			title = data.getString("TITLE");
			description = data.getString("DESCRIPTION");
			page = data.getInteger("PAGE");
			x = data.getInteger("X");
			y = data.getInteger("Y");
			important = data.getByte("IMPORTANT");
			lockedByDefault = data.getBoolean("LOCKEDBYDEFAULT");
			hiddenByDefault = data.getBoolean("HIDDENBYDEFAULT");
			
			NBTTagList prereqs = data.getTagList("PREREQUISITES", 8);
			NBTTagList rews = data.getTagList("REWARDS", 8);
			NBTTagList tsks = data.getTagList("TASKS", 8);
			
			for(int i = 0; i < prereqs.tagCount(); i++) {
				this.addPrerequisite(UUID.fromString(prereqs.getStringTagAt(i)));
			}
			for(int i = 0; i < rews.tagCount(); i++) {
				this.addTask(UUID.fromString(rews.getStringTagAt(i)));
			}
			for(int i = 0; i < tsks.tagCount(); i++) {
				this.addTask(UUID.fromString(tsks.getStringTagAt(i)));
			}
		}
	}

	@Override
	public UUID getUUID() {
		return id;
	}
	
	private void setUUID(UUID uuid) {
		Assert(id == null);
		id = uuid;
	}

	@Override
	public long getVersion() {
		// TODO Auto-generated method stub
		return VERSION;
	}
}
