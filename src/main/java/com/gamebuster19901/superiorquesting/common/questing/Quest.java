package com.gamebuster19901.superiorquesting.common.questing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.NBTDebugger;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Assignment;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Rewardable;
import com.gamebuster19901.superiorquesting.common.shape.Point;
import com.gamebuster19901.superiorquesting.common.shape.Shape;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class Quest implements Rewardable, Assignment, Debuggable, Assertable, NBTDebugger{
	public static final long VERSION = 1L;
	private UUID id;
	private String title;
	private String description;
	private UUID page;
	private int x;
	private int y;
	private Class<? extends Shape> shape;
	private byte important = 1;
	private boolean lockedByDefault = false; //a quest that is locked by default is locked until another quest unlocks it.
	private boolean hiddenByDefault = false; //a quest that is hidden is not visible unless all prerequisite quests are completed, or another quest unhides it
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
	public Quest(String title, String description, UUID page, int x, int y, Class<? extends Shape> shape, byte important, Collection<UUID> rewards, Collection<UUID> prerequisites, Collection<UUID> tasks) {
		this(UUID.randomUUID(), title, description, page, x, y, shape, important, rewards, prerequisites, tasks);
	}
	
	private Quest(UUID id, String title, String description, UUID page, int x, int y, Class<? extends Shape> shape, byte important, Collection<UUID> rewards, Collection<UUID> prerequisites, Collection<UUID> tasks) {
		try {
			if(id == null) {
				throw new NullPointerException("id");
			}
			if(title == null) {
				throw new NullPointerException("title");
			}
			if(description == null) {
				throw new NullPointerException("description");
			}
			if (x < 0) {
				throw new IndexOutOfBoundsException(x + " < 0. (x)");
			}
			if(y < 0) {
				throw new IndexOutOfBoundsException(y + " < 0. (y)");
			}
			if(shape == null) {
				throw new NullPointerException("shape");
			}
			if(shape.getClass().isAssignableFrom(Point.class)) {
				throw new IllegalArgumentException("shape cannot be a point");
			}
			if(important < 1) {
				throw new IndexOutOfBoundsException(important + " < 1 (importance)");
			}
			if(page == null) {
				throw new NullPointerException(page + "page");
			}
			if(getGlobalQuestHandler().getPage(page) == null) {
				throw new IllegalArgumentException("UUID does not correspond to a page: (" + page + ")");
			}
			if(rewards == null) {
				throw new NullPointerException("rewards");
			}
			if(prerequisites == null) {
				throw new NullPointerException("prerequisites");
			}
			if(tasks == null) {
				throw new NullPointerException("tasks");
			}
		}
		catch(NullPointerException | IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Programming error, report to Gamebuster1990", e);
		}
		this.id = id;
		this.title = title;
		this.description = description;
		this.page = page;
		this.x = x;
		this.y = y;
		this.important = important;
		this.rewards = new ArrayList<UUID>(rewards);
		this.prerequisites = new ArrayList<UUID>(prerequisites);
		this.tasks = new ArrayList<UUID>(tasks);
		getGlobalQuestHandler().add(true, this);
		getGlobalQuestHandler().getPage(page).add(this);
	}
	
	public Quest(String title, String description, UUID page, int x, int y, Class<? extends Shape> shape, byte important) {
		this(UUID.randomUUID(), title, description, page, x, y, shape, important, new ArrayList<UUID>(), new ArrayList<UUID>(), new ArrayList<UUID>());
	}
	
	public Quest(NBTTagCompound data) {
		this.deserializeNBT(data);
		getGlobalQuestHandler().add(false, this);
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
				if(!getGlobalQuestHandler().getQuest(q).isFinished(p)) {
					return false;
				}
			}
			for(UUID t : tasks) {
				if(!getGlobalQuestHandler().getTask(t).isFinished(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFinished(EntityPlayer p) {
		return getPlayerQuestHandler().getQuestNBT(getUUID(), p).getBoolean(COMPLETED);
	}
	
	@Override
	public boolean isFinished(UUID p) {
		if(getPlayerQuestHandler().getNbtOfPlayerUsingUUID(p) != null) {
			return getPlayerQuestHandler().getQuestNBT(getUUID(), p).getBoolean(COMPLETED);
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
			getGlobalQuestHandler().getQuest(q).finish(p);
		}
		for(UUID t : tasks) {
			getGlobalQuestHandler().getTask(t).finish(p);
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
			getGlobalQuestHandler().getQuest(q).finish(p);
		}
		for(UUID t : tasks) {
			getGlobalQuestHandler().getTask(t).finish(p);
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
	
	@Override
	@Deprecated
	public void markUnfinished(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void markUnfinished(UUID p) {
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
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(COLLECTED);
	}
	
	@Override
	public boolean hasCollected(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(COLLECTED);
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
			this.getGlobalQuestHandler().getReward(r).collect(p);
		}
		markCollected(p);
	}
	
	@Override
	@Deprecated
	public void markCollected(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, true);
	}

	@Override
	@Deprecated
	public void markCollected(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, true);
	}
	
	@Override
	@Deprecated
	public void markUncollected(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, false);
	}

	@Override
	@Deprecated
	public void markUncollected(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(COLLECTED, false);
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
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(NOTIFIED);
	}
	
	@Override
	public boolean hasNotified(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(NOTIFIED);
	}
	
	@Override
	public void notify(EntityPlayer p) {
		Assert(false, "Not yet implemented");
	}

	@Override
	@Deprecated
	public void markNotified(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void markNotified(UUID p) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Deprecated
	public void markUnnotified(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Deprecated
	public void markUnnotified(UUID p) {
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
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public boolean isUnlocked(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public void lock(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void lock(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void unlock(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, true);
	}

	@Override
	public void unlock(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(UNLOCKED, true);
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
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}

	@Override
	public boolean isHidden(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}
	
	@Override
	public void hide(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void hide(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void unhide(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
	}

	@Override
	public void unhide(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
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
	 * @return the page of this quest
	 */
	public UUID getPage() {
		return page;
	}
	
	/**
	 * adds a prerequisite to this quest, a prerequisite is another Quest
	 * @param a the assignment to add
	 */
	void addPrerequisite(UUID q) {
		prerequisites.add(q);
	}
	
	/**
	 * removes a prerequisite from this quest
	 * @param a the assignment to remove
	 */
	void removePrerequisite(UUID q) {
		prerequisites.remove(q);
	}
	
	/**
	 * adds a task to this quest
	 * @param t
	 */
	void addTask(UUID t) {
		prerequisites.add(t);
	}
	
	/**
	 * removes a task from this quest
	 * @param t
	 */
	void removeTask(UUID t) {
		tasks.remove(t);
	}
	/**
	 * adds a reward to this quest
	 * @param r the reward to add
	 */
	public void addReward(UUID r) {
		rewards.add(r);
	}
	
	/**
	 * removes a reward from this quest
	 * @param r the reward to remove
	 */
	void removeReward(UUID r) {
		rewards.remove(r);
	}
	
	public int getImportance() {
		return important;
	}

	@Override
	public GlobalQuestHandler getGlobalQuestHandler() {
		return Assignment.super.getGlobalQuestHandler();
	}
	
	@Override
	public PlayerQuestHandler getPlayerQuestHandler() {
		return Assignment.super.getPlayerQuestHandler();
	}
	
	@Override
	public void convert(long prevVersion, long nextVersion, NBTTagCompound nbtIn) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);
			}
			
			Assert(nextVersion != prevVersion, "Cannot convert to a version if it is the same version, this should never happen! (" + nextVersion + ")");
			
			if(nextVersion > prevVersion + 1L) {
				convert(prevVersion, nextVersion - 1L, nbtIn);
			}
			
			if(prevVersion == 0L && nextVersion == 1L) {
				throw new AssertionError("Tried to convert from nonexistant version 0 to version 1");
			}
			
			if(prevVersion == 1L && nextVersion == 2L) {
				//Future: convert from version 1 to version 2
			}
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + " to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
			throw new VersioningError("There was an issue converting from version " + prevVersion + " to version " + nextVersion, e);
		}
		
	}
	
	@Override
	public final NBTTagCompound getDefaultPlayerNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", getVersion());
		nbt.setString("UUID", getUUID().toString());
		nbt.setBoolean("FINISHED", false);
		nbt.setBoolean("NOTIFIED", false);
		nbt.setBoolean("HIDDEN", isHiddenByDefault());
		nbt.setBoolean("LOCKED", isLockedByDefault());
		nbt.setBoolean("COLLECTED", false);
		return nbt;
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
		Assert(id != null, "UUID of quest " + this + " is null");
		data.setString("UUID", id.toString());
		data.setString("TITLE", title);
		data.setString("DESCRIPTION", description);
		data.setString("PAGE", page.toString());
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
		try {
			try {
				Assert(ver != 0, "Missing version data in quest " + data.getString("UUID"));
			}
			catch(AssertionError e) {
				debug(getFullNBTString(data, 1));
				throw e;
			}
			if(ver != VERSION) {
				convert(ver, VERSION, data);
			}
			else {
				debug(data.getString("UUID"));
				id = UUID.fromString(data.getString("UUID"));
				title = data.getString("TITLE");
				description = data.getString("DESCRIPTION");
				page = UUID.fromString(data.getString("PAGE"));
				x = data.getInteger("X");
				if(x < 0) {
					throw new IndexOutOfBoundsException("x <= 0");
				}
				y = data.getInteger("Y");
				if(y < 0) {
					throw new IndexOutOfBoundsException("y <= 0");
				}
				important = data.getByte("IMPORTANT");
				if(important < 1) {
					throw new IndexOutOfBoundsException("important < 1");
				}
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
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
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
	
	ArrayList<UUID> getRewards(){
		return rewards;
	}
	
	ArrayList<UUID> getPrerequisites(){
		return prerequisites;
	}
	
	ArrayList<UUID> getTasks(){
		return tasks;
	}
	
	@Override
	public String toString() {
		return getTitle() + " [" + id + "]";
	}


}
