package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
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
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
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
		return hasFinished(p);
	}

	/**
	 * Collects the player
	 * 
	 * @param p the player to collect
	 */
	@Override
	public void collect(EntityPlayer p) {
		for(Reward r : rewards) {
			r.collect(p);
		}
	}

	/**
	 * returns true if the player has completed all prerequisites, and this quest, false otherwise
	 * 
	 * @param p the player to check
	 */
	@Override
	public boolean hasFinished(EntityPlayer p) {
		for(Quest q : prerequisites) {
			if(!q.hasFinished(p)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean hasFinished(UUID p) {
		if(Main.proxy.getQuestHandler().getNbtOfPlayerUsingUUID(p) != null) {
			for(Quest q : prerequisites) {
				if(!q.hasFinished(p)) {
					return false;
				}
			}
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
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COMPLETED, true);
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
		Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).setBoolean(COMPLETED, true);
	}
	
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
	 * returns true if the player has been notified of this quest's completion, false otherwise
	 * @param p the payer to check
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
	public boolean isUnlocked(EntityPlayer p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(UNLOCKED);
	}
	
	@Override
	public boolean isUnlocked(UUID p) {
		return Main.proxy.getQuestHandler().getQuestNBT(this.getTitle(), p).getBoolean(UNLOCKED);
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
	
	@SubscribeEvent
	public void worldTickEvent(WorldTickEvent e) {
		if(e.phase.equals(Phase.END)) {
			if(e.world.isRemote) {
				for(EntityPlayerMP p : e.world.getPlayers(EntityPlayerMP.class, null)) {
					for(Quest q : prerequisites) { //these should be in order as quests first, then tasks
						
					}
					
					if(canCollect(p)) {
						collect(p);
					}
				}
			}
		}
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
	
	ArrayList<Task> tasks(){
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

	@Override
	public void hide(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unhide(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unhide(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLockedByDefault() {
		// TODO Auto-generated method stub
		return lockedByDefault;
	}
	
	@Override
	public boolean isHiddenByDefault() {
		return hiddenByDefault;
	}
	
	public boolean canCompleteTasksWhileLocked() {
		return completeTasksWhileLocked;
	}
}
