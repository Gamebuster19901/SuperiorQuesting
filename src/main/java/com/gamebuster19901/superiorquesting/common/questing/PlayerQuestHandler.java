package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.PAGE_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.PAGES;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.QUESTS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.QUEST_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.REWARDS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.REWARD_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.TASKS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.TASK_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.integrate.Assignment.COMPLETED;
import static com.gamebuster19901.superiorquesting.common.questing.integrate.Lockable.UNLOCKED;
import static com.gamebuster19901.superiorquesting.common.questing.integrate.Notifyable.NOTIFIED;
import static com.gamebuster19901.superiorquesting.common.questing.integrate.Rewardable.COLLECTED;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class PlayerQuestHandler extends MultiplayerHandler{
	
	NBTTagCompound getPageNBT(UUID id, EntityPlayer p) {
		Assert(hasPageNBT(id, p), "page " + id + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(PAGE_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getPageNBT(UUID id, UUID p) {
		Assert(hasPageNBT(id, p), "page " + id + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(PAGE_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getPageNBT(EntityPlayer p) {
		Assert(hasPageNBT(p), "No page nbt found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(PAGE_KEY);
	}
	
	public boolean hasPageNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(PAGE_KEY);
	}
	
	public boolean hasPageNBT(UUID p) {
		return getPersistantTag(p).hasKey(PAGE_KEY);
	}
	
	public boolean hasPageNBT(UUID page, EntityPlayer p) {
		Assert(hasPageNBT(p), "page " + page + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(PAGE_KEY).hasKey(page.toString());
	}
	
	public boolean hasPageNBT(UUID page, UUID p) {
		Assert(hasPageNBT(p), "page " + page + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(PAGE_KEY).hasKey(page.toString());
	}
	
	public TreeSet<Page> getVisiblePages(UUID p) {
		final TreeSet<Page> visiblePages = new TreeSet<Page>();
		for(Page page : Main.proxy.getGlobalQuestHandler().getAllPages()) {
			if(!page.isHidden(p)) {
				visiblePages.add(page);
			}
		}
		return visiblePages;
	}
	
	public TreeSet<Page> getVisiblePages(EntityPlayer p) {
		final TreeSet<Page> visiblePages = new TreeSet<Page>();
		for(Page page : Main.proxy.getGlobalQuestHandler().getAllPages()) {
			if(!page.isHidden(p)) {
				visiblePages.add(page);
			}
		}
		return visiblePages;
	}
	
	NBTTagCompound getQuestNBT(UUID id, EntityPlayer p) {
		Assert(hasQuestNBT(id, p), "quest " + id + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getQuestNBT(UUID id, UUID p) {
		Assert(hasQuestNBT(id, p), "quest " + id + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getQuestNBT(EntityPlayer p) {
		Assert(hasQuestNBT(p), "No quest nbt found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(QUEST_KEY);
	}
	
	public boolean hasQuestNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	public boolean hasQuestNBT(UUID p) {
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	public boolean hasQuestNBT(UUID quest, EntityPlayer p) {
		Assert(hasQuestNBT(p), "quest " + quest + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	public boolean hasQuestNBT(UUID quest, UUID p) {
		Assert(hasQuestNBT(p), "quest " + quest + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	public TreeSet<Quest> getVisibleQuests(UUID p) {
		final TreeSet<Quest> visibleQuests = new TreeSet<Quest>();
		for(Quest quest : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
			if(!quest.isHidden(p)) {
				visibleQuests.add(quest);
			}
		}
		return visibleQuests;
	}
	
	public TreeSet<Quest> getVisibleQuests(EntityPlayer p) {
		final TreeSet<Quest> visibleQuests = new TreeSet<Quest>();
		for(Quest quest : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
			if(!quest.isHidden(p)) {
				visibleQuests.add(quest);
			}
		}
		return visibleQuests;
	}
	
	public TreeSet<Quest> getVisibleQuests(EntityPlayer p, Page page) {
		final TreeSet<Quest> visibleQuests = new TreeSet<Quest>();
		for(Quest quest : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
			if(!quest.isHidden(p) && quest.getPage().equals(page.getUUID())) {
				visibleQuests.add(quest);
			}
		}
		return visibleQuests;
	}
	
	public TreeSet<Quest> getVisibleQuestsInPage(UUID p, Page page) {
		final TreeSet<Quest> visibleQuests = new TreeSet<Quest>();
		for(Quest quest : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
			if(!quest.isHidden(p) && quest.getPage().equals(page.getUUID())) {
				visibleQuests.add(quest);
			}
		}
		return visibleQuests;
	}
	
	public TreeSet<Quest> getVisibleQuestsInPage(EntityPlayer p, Page page) {
		final TreeSet<Quest> visibleQuests = new TreeSet<Quest>();
		for(Quest quest : Main.proxy.getGlobalQuestHandler().getAllQuests()) {
			if(!quest.isHidden(p) && quest.getPage().equals(page.getUUID())) {
				visibleQuests.add(quest);
			}
		}
		return visibleQuests;
	}
	
	public NBTTagCompound getRewardNBT(UUID id, EntityPlayer p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	public NBTTagCompound getRewardNBT(UUID id, UUID p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	public NBTTagCompound getRewardNBT(EntityPlayer p) {
		Assert(hasRewardNBT(p), "No reward nbt found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(REWARD_KEY);
	}
	
	boolean hasRewardNBT(EntityPlayer p) {
		return getPersistantTag(p).hasKey(REWARD_KEY);
	}
	
	boolean hasRewardNBT(UUID p) {
		return getPersistantTag(p).hasKey(REWARD_KEY);
	}
	
	boolean hasRewardNBT(UUID reward, EntityPlayer p) {
		Assert(hasRewardNBT(p), "No reward nbt for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).hasKey(reward.toString());
	}
	
	boolean hasRewardNBT(UUID reward, UUID p) {
		Assert(hasRewardNBT(p), "No reward nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).hasKey(reward.toString());
	}
	
	NBTTagCompound getTaskNBT(UUID task, EntityPlayer p) {
		Assert(hasTaskNBT(task, p), "task " + task + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(TASK_KEY).getCompoundTag(task.toString());
	}
	
	NBTTagCompound getTaskNBT(UUID task, UUID p) {
		Assert(hasTaskNBT(task, p), "task " + task + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(TASK_KEY).getCompoundTag(task.toString());
	}
	
	NBTTagCompound getTaskNBT(EntityPlayer p) {
		Assert(hasTaskNBT(p), "No task nbt found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(TASK_KEY);
	}
	
	boolean hasTaskNBT(EntityPlayer p) {
		return getPersistantTag(p).hasKey(TASK_KEY);
	}
	
	boolean hasTaskNBT(UUID p) {
		return getPersistantTag(p).hasKey(TASK_KEY);
	}
	
	boolean hasTaskNBT(UUID task, EntityPlayer p) {
		Assert(hasTaskNBT(p), "task " + task + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(TASK_KEY).hasKey(task.toString());
	}
	
	boolean hasTaskNBT(UUID task, UUID p) {
		Assert(hasTaskNBT(p), "task " + task + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(TASK_KEY).hasKey(task.toString());
	}
	
	void resetPage(Page p, EntityPlayer plyr) {
		getPersistantTag(plyr).getCompoundTag(PAGE_KEY).setTag(p.getUUID().toString(), p.getDefaultPlayerNBT());
	}
	
	void resetQuest(Quest q, EntityPlayer p) {
		for(UUID task : q.getTasks()) {
			resetTask(TASKS.get(task), p);
		}
		for(UUID reward : q.getRewards()) {
			resetReward(REWARDS.get(reward), p);
		}
		getPersistantTag(p).getCompoundTag(QUEST_KEY).setTag(q.getUUID().toString(), q.getDefaultPlayerNBT());
	}
	
	void resetTask(Task t, EntityPlayer p) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", t.getVersion());
		nbt.setString("UUID", t.getUUID().toString());
		nbt.setString("PARENT", t.getParent().toString());
		nbt.setBoolean("FINISHED", false);
		nbt.setBoolean("NOTIFIED", false);
		nbt.setBoolean("HIDDEN", t.isHiddenByDefault());
		nbt.setBoolean("LOCKED", t.isLockedByDefault());
		getPersistantTag(p).getCompoundTag(TASK_KEY).setTag(t.getUUID().toString(), nbt);
	}
	
	void resetReward(Reward r, EntityPlayer p) {
		NBTTagCompound nbt = new NBTTagCompound();
		r.getDefaultPlayerNBT();
		getPersistantTag(p).getCompoundTag(REWARD_KEY).setTag(r.getUUID().toString(), nbt);
	}
	
	void add(Page p) {
		MinecraftServer s = Main.proxy.getServer();
		Assert(s != null);
		Assert(p != null);
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer plyr = s.getPlayerList().getPlayerByUsername(username);
			resetPage(p, plyr);
		}
	}
	
	void add(Quest q) {
		MinecraftServer s = Main.proxy.getServer();
		Assert(s != null);
		Assert(q != null);
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetQuest(q, p);
		}
	}
	
	void add(Reward r) {
		MinecraftServer s = Main.proxy.getServer();
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetReward(r, p);
		}
	}
	
	void add(Task t) {
		MinecraftServer s = Main.proxy.getServer();
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetTask(t, p);
		}
	}
	
	/**
	 * If the player's NBT is not up to date update it
	 * 
	 * E.X. if it doesn't contain quests that the server has, then add them,
	 * if it contains quests that the server doesn't have, then remove them.
	 * 
	 * @param p The player to check
	 */
	public void assertValidNBT(EntityPlayer p) {
		if(hasPageNBT(p) && hasQuestNBT(p) && hasTaskNBT(p) && hasRewardNBT(p)){
			removeRemovedNBT(p);
			for(UUID key : PAGES.keySet()) {
				Page page = PAGES.get(key);
				if(!hasPageNBT(key, p)) {
					resetPage(page, p);
				}
			}
			for(UUID key : QUESTS.keySet()) {
				Quest q = QUESTS.get(key);
				if (!hasQuestNBT(key, p)) {
					resetQuest(q, p);
				}
			}
			for(UUID key : TASKS.keySet()) {
				Task t = TASKS.get(key);
				if(!hasTaskNBT(key, p)) {
					resetTask(t, p);
				}
			}
			for(UUID key : REWARDS.keySet()) {
				Reward r = REWARDS.get(key);
				if(!hasRewardNBT(key, p)) {
					resetReward(r, p);
				}
			}
		}
		else{
			resetAllNBT(p);
		}
	}
	
	/**
	 * Clears and sets a players NBT to default, as if they just joined for the first time
	 * @param p
	 */
	void resetAllNBT(EntityPlayer p) {
		getPersistantTag(p).setTag(PAGE_KEY, new NBTTagCompound());
		getPersistantTag(p).setTag(QUEST_KEY, new NBTTagCompound());
		getPersistantTag(p).setTag(TASK_KEY, new NBTTagCompound());
		getPersistantTag(p).setTag(REWARD_KEY, new NBTTagCompound());
		for(UUID key : PAGES.keySet()) {
			Page page = PAGES.get(key);
			resetPage(page, p);
		}
		for(UUID key : QUESTS.keySet()) {
			Quest q = QUESTS.get(key);
			resetQuest(q, p);
		}
	}
	
	void removeRemovedNBT(EntityPlayer p) {
		NBTTagCompound pages = getPageNBT(p);
		NBTTagCompound quests = getQuestNBT(p);
		NBTTagCompound tasks = getTaskNBT(p);
		NBTTagCompound rewards = getRewardNBT(p);
		
		ArrayList<String> pagesToRemove = new ArrayList<String>();
		ArrayList<String> questsToRemove = new ArrayList<String>();
		ArrayList<String> tasksToRemove = new ArrayList<String>();
		ArrayList<String> rewardsToRemove = new ArrayList<String>();
		
		
		for(String page : pages.getKeySet()){
			if(!PAGES.containsKey(UUID.fromString(page))) {
				pagesToRemove.add(page);
			}
		}
		for(String q : quests.getKeySet()) {
			if(!QUESTS.containsKey(UUID.fromString(q))) {
				questsToRemove.add(q);
			}
		}
		for(String t : tasks.getKeySet()) {
			if(!TASKS.containsKey(UUID.fromString(t))) {
				tasksToRemove.add(t);
			}
		}
		for(String r : rewards.getKeySet()) {
			if(!REWARDS.containsKey(UUID.fromString(r))) {
				rewardsToRemove.add(r);
			}
		}
		
		for(String page : pagesToRemove) {
			pages.removeTag(page);
		}
		
		for(String q : questsToRemove) {
			quests.removeTag(q);
		}
		
		for(String t : tasksToRemove) {
			tasks.removeTag(t);
		}
		
		for(String r : rewardsToRemove) {
			rewards.removeTag(r);
		}
	}
}
