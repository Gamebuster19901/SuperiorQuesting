package com.gamebuster19901.superiorquesting.common.questing;

import java.util.Collection;
import java.util.TreeSet;

import com.gamebuster19901.superiorquesting.common.Debuggable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public final class Quest implements Rewardable, Assignment, Debuggable{
	private String title;
	private String description;
	private int page;
	private int x;
	private int y;
	private byte important = 1;
	private TreeSet<Reward> rewards = new TreeSet<Reward>();
	private TreeSet<Assignment> prerequisites = new TreeSet<Assignment>();
	
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
	public boolean canAward(EntityPlayer p) {
		if(hasCollected(p)) {
			return false;
		}
		return isFinished(p);
	}

	/**
	 * Awards the player
	 * 
	 * @param p the player to award
	 */
	@Override
	public void award(EntityPlayer p) {
		for(Reward r : rewards) {
			r.award(p);
		}
	}

	/**
	 * returns true if the player has completed all prerequisites, and this quest, false otherwise
	 * 
	 * @param p the player to check
	 */
	@Override
	public boolean isFinished(EntityPlayer p) {
		for(Assignment a : prerequisites) {
			if(a instanceof Quest) { //these should be in the order of quest first, then tasks
				
			}
			if(!a.isFinished(p)) {
				return false;
			}
		}
		return true;
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
		//mark this as finished
	}
	
	/**
	 * returns true if the player has collected the reward for this quest, false otherwise
	 * @param p the player to check
	 */
	@Override
	public boolean hasCollected(EntityPlayer p) {
		
	}
	
	/**
	 * returns true if the player has been notified of this quest's completion, false otherwise
	 * @param p the payer to check
	 */
	private boolean hasNotified(EntityPlayer p) {
		
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
					for(Assignment a : prerequisites) { //these should be in order as quests first, then tasks
						if(a instanceof Quest) {
							
						}
					}
					
					if(canAward(p)) {
						award(p);
					}
				}
			}
		}
	}
	
	/**
	 * adds a prerequisite to this quest, a prerequisite by default is another Quest or a Task
	 * @param a the assignment to add
	 */
	void addPrerequisite(Assignment a) {
		prerequisites.add(a);
	}
	
	/**
	 * removes a prerequisite from this quest
	 * @param a the assignment to remove
	 */
	void removePrerequisite(Assignment a) {
		prerequisites.remove(a);
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

	/**
	 * @see Assignment.compareTo
	 */
	@Override
	public final int compareTo(Object o) {
		if(o instanceof Quest) {
			return 0;
		}
		if(o instanceof Task) {
			return -1;
		}
		if(o instanceof Assignment) {
			return(((Assignment) o).compareTo(this)) * -1;
		}
		if(o instanceof Comparable) {
			return (((Comparable) o).compareTo(this)) * -1;
		}
		return 0;
	}
}
