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
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public final class Quest implements Rewardable, Assignment, Debuggable, Assertable{
	private static final long serialVersionUID = 0L;
	private long VERSION = serialVersionUID;
	private String title;
	private String description;
	private int page;
	private int x;
	private int y;
	private byte important = 1;
	private boolean completeTasksWhileLocked = false;
	private boolean lockedByDefault = false;
	private boolean hiddenByDefault = false; //a quest that is hidden is not visible and is locked unless all prerequisite quests are completed.
	private ArrayList<Reward> rewards = new ArrayList<Reward>();
	private ArrayList<Quest> prerequisites = new ArrayList<Quest>();
	private TreeSet<Task> tasks = new TreeSet<Task>();
	
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
	public Quest(String title, String description, int page, int x, int y, Collection<Reward> rewards, Collection<Assignment> assignments) {
		if (x < 0 || y < 0) {
			throw new IndexOutOfBoundsException(x + " or " + y + " < 0.");
		}
		this.title = title;
		this.description = description;
		this.page = page;
		this.x = x;
		this.y = y;
		this.addRewards(rewards);
		this.addPrerequisites(assignments);
	}
	
	/**
	 * @param title the title of the quest
	 * @param description the description of the quest
	 * @param page the page the quest is located on
	 * @param x the x-coordinate the quest is located at on the page
	 * @param y the y-coordinate the quest is located at on the page
	 * @param rewards the rewards the quest gives once completed
	 * @param assignments the tasks a player must complete to collect rewards or continue
	 * @param important how large this quest should be in the quest book as a byte. Default is 1, sets size in quest book to (15 + importance) pixels
	 * 
	 * @throws IndexOutOfBoundsException if (important < 1), (x < 0), (or y < 0)
	 */
	public Quest(String title, String description, int page, int x, int y, Collection<Reward> rewards, Collection<Assignment> assignments, byte important) {
		this(title, description, page, x, y, rewards, assignments);
		if(important >= 1) {
			this.important = important;
			return;
		}
		throw new IndexOutOfBoundsException(important + " < " + 1);
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
			for(Quest q : prerequisites) {
				if(!q.isFinished(p)) {
					return false;
				}
			}
			for(Task t : tasks) {
				if(!t.isFinished(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFinished(EntityPlayer p) {
		return Main.proxy.getQuestHandler().getQuestNBT(getTitle(), p).getBoolean(COMPLETED);
	}
	
	@Override
	public boolean isFinished(UUID p) {
		if(Main.proxy.getQuestHandler().getNbtOfPlayerUsingUUID(p) != null) {
			return Main.proxy.getQuestHandler().getQuestNBT(getTitle(), p).getBoolean(COMPLETED);
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
		for(Assignment a : prerequisites) {
			a.finish(p);
		}
		for(Task t : tasks) {
			t.finish(p);
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
		for(Assignment a : prerequisites) {
			a.finish(p);
		}
		for(Task t : tasks) {
			t.finish(p);
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
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(COLLECTED);
	}
	
	@Override
	public boolean hasCollected(UUID p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(COLLECTED);
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
		for(Reward r : rewards) {
			r.collect(p);
		}
		markCollected(p);
	}
	
	@Override
	@Deprecated
	public void markCollected(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COLLECTED, true);
	}

	@Override
	@Deprecated
	public void markCollected(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COLLECTED, true);
	}
	
	@Override
	@Deprecated
	public void markUncollected(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COLLECTED, false);
	}

	@Override
	@Deprecated
	public void markUncollected(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COLLECTED, false);
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
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(NOTIFIED);
	}
	
	@Override
	public boolean hasNotified(UUID p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(NOTIFIED);
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
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public boolean isUnlocked(UUID p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public void lock(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void lock(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(UNLOCKED, false);
	}

	@Override
	public void unlock(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(UNLOCKED, true);
	}

	@Override
	public void unlock(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(UNLOCKED, true);
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
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(HIDDEN);
	}

	@Override
	public boolean isHidden(UUID p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(HIDDEN);
	}
	
	@Override
	public void hide(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void hide(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void unhide(EntityPlayer p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(HIDDEN, false);
	}

	@Override
	public void unhide(UUID p) {
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(HIDDEN, false);
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
	void addPrerequisite(Assignment a) {
		if(a instanceof Quest) {
			prerequisites.add((Quest)a);
		}
		else {
			tasks.add((Task) a);
		}
	}
	
	/**
	 * removes a prerequisite from this quest
	 * @param a the assignment to remove
	 */
	void removePrerequisite(Assignment a) {
		if(a instanceof Quest) {
			prerequisites.remove((Quest)a);
		}
		else {
			tasks.remove((Task)a);
		}
	}
	
	/**
	 * adds a reward to this quest
	 * @param r the reward to add
	 */
	void addReward(Reward r) {
		rewards.add(r);
	}
	
	/**
	 * adds all of the rewards in the passed collection to this quest
	 * @param rewards the rewards to add
	 */
	void addRewards(Collection<Reward> rewards) {
		for(Reward r : rewards) {
			addReward(r);
		}
	}
	
	/**
	 * removes a reward from this quest
	 * @param r the reward to remove
	 */
	void removeReward(Reward r) {
		rewards.remove(r);
	}
	
	/**
	 * removes all rewards in the passed collection from this quest
	 * @param rewards the rewards to remove
	 */
	void removeRewards(Collection<Reward> rewards) {
		for(Reward r : rewards) {
			addReward(r);
		}
	}
	
	/**
	 * adds all of the prerequisites in the passed collection to this quest
	 * @param assignments the prerequisites to add
	 */
	void addPrerequisites(Collection<Assignment> assignments) {
		for(Assignment a : assignments) {
			addPrerequisite(a);
		}
	}
	
	/**
	 * removes all of the prerequisites in the passed collection to this quest
	 * @param assignments the prerequisites to remove
	 */
	void removePrerequisites(Collection<Assignment> assignments) {
		for(Assignment a : assignments) {
			removePrerequisite(a);
		}
	}
	
	ArrayList<Quest> prerequisites(){
		return prerequisites;
	}
	
	TreeSet<Task> tasks(){
		return tasks;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

	public static Quest fromString(String title) {
		return Main.proxy.getQuestHandler().getQuest(title);
	}

	@Override
	public void convert(long prevVersion, long nextVersion, ObjectInputStream in) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);
			}
			if(nextVersion == VERSION) {
				throw new AssertionError(new IllegalArgumentException(prevVersion + " == " + nextVersion));
			}
			if(nextVersion > prevVersion + 1L) {
				convert(prevVersion, nextVersion - 1L, in);
				return;
			}
		

			if(prevVersion == 0L && nextVersion == 1L) {
				Main.LOGGER.log(Level.INFO, "Converting quest from version " + prevVersion + " to version " + nextVersion);
				throw new FutureVersionError("1 is a future version, currently on version 0");
			}
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + "to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
			throw new VersioningError(e);
		}
	}

	@Override
	public void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	
	@Override
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.mark(8);
		long inVersion = in.readLong();
		if(inVersion == serialVersionUID) {
			in.reset();
			in.defaultReadObject();
		}
		else {
			convert(serialVersionUID, inVersion, in);
		}
	}
}
