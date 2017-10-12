package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.common.questing.Assignment.COMPLETED;
import static com.gamebuster19901.superiorquesting.common.questing.Assignment.NOTIFIED;
import static com.gamebuster19901.superiorquesting.common.questing.Assignment.UNLOCKED;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.QUESTS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.QUEST_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.REWARDS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.REWARD_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.TASKS;
import static com.gamebuster19901.superiorquesting.common.questing.GlobalQuestHandler.TASK_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.Rewardable.COLLECTED;

import java.util.ArrayList;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.MultiplayerHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerQuestHandler extends MultiplayerHandler{
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
	
	boolean hasQuestNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	boolean hasQuestNBT(UUID p) {
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	boolean hasQuestNBT(UUID quest, EntityPlayer p) {
		Assert(hasQuestNBT(p), "quest " + quest + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	boolean hasQuestNBT(UUID quest, UUID p) {
		Assert(hasQuestNBT(p), "quest " + quest + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	NBTTagCompound getRewardNBT(UUID id, EntityPlayer p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found for player " + p.getName());
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getRewardNBT(UUID id, UUID p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found for player " + p);
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getRewardNBT(EntityPlayer p) {
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
	
	
	
	
	void resetQuest(Quest q, EntityPlayer p) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", q.getVersion());
		nbt.setString("UUID", q.getUUID().toString());
		nbt.setBoolean("FINISHED", false);
		nbt.setBoolean("NOTIFIED", false);
		nbt.setBoolean("HIDDEN", q.isHiddenByDefault());
		nbt.setBoolean("LOCKED", q.isLockedByDefault());
		nbt.setBoolean("COLLECTED", false);
		//No need to reset prerequisites since resetQuests will do that anyway
		for(UUID task : q.getTasks()) {
			resetTask(TASKS.get(task), p);
		}
		for(UUID reward : q.getRewards()) {
			resetReward(REWARDS.get(reward), p);
		}
		getPersistantTag(p).getCompoundTag(QUEST_KEY).setTag(q.getUUID().toString(), nbt);
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
		nbt.setLong("VERSION", r.getVersion());
		nbt.setString("UUID", r.getUUID().toString());
		nbt.setBoolean("COLLECTED", false);
		getPersistantTag(p).getCompoundTag(REWARD_KEY).setTag(r.getUUID().toString(), nbt);
	}
	
	void add(MinecraftServer s, Quest q) {
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetQuest(q, p);
		}
	}
	
	void add(MinecraftServer s, Reward r) {
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetReward(r, p);
		}
	}
	
	void add(MinecraftServer s, Task t) {
		for(String username : s.getOnlinePlayerNames()) {
			EntityPlayer p = s.getPlayerList().getPlayerByUsername(username);
			resetTask(t, p);
		}
	}
	
	/**
	 * If the player's quest NBT is not up to date, update the player's quest nbt
	 * @param p The player to check
	 * @return true if the player's life count changed
	 */
	public void assertValidNBT(EntityPlayer p) {
		if(hasQuestNBT(p) && hasTaskNBT(p) && hasRewardNBT(p)){
			removeRemovedNBT(p);
			for(UUID key : QUESTS.keySet()) {
				Quest q = QUESTS.get(key);
				if (!hasQuestNBT(key, p)) {
					resetQuest(q, p);
				}
			}
		}
		else{
			resetAllNBT(p);
		}
	}
	
	/**
	 * resets a players quest NBT, 
	 * @param p
	 */
	void resetAllNBT(EntityPlayer p) {
		getPersistantTag(p).setTag(QUEST_KEY, new NBTTagCompound());
		getPersistantTag(p).setTag(TASK_KEY, new NBTTagCompound());
		getPersistantTag(p).setTag(REWARD_KEY, new NBTTagCompound());
		for(UUID key : QUESTS.keySet()) { //key is the quest title
			Quest q = QUESTS.get(key);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean(UNLOCKED, false);
			nbt.setBoolean(COMPLETED, false);
			nbt.setBoolean(NOTIFIED, false);
			nbt.setBoolean(COLLECTED, false);
			getPersistantTag(p).setTag(key.toString(), nbt);
		}
	}
	
	void removeRemovedNBT(EntityPlayer p) {
		NBTTagCompound quests = getQuestNBT(p);
		NBTTagCompound tasks = getTaskNBT(p);
		NBTTagCompound rewards = getRewardNBT(p);
		
		ArrayList<String> questsToRemove = new ArrayList<String>();
		ArrayList<String> tasksToRemove = new ArrayList<String>();
		ArrayList<String> rewardsToRemove = new ArrayList<String>();
		
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

	@Override
	protected void onConfigFinishChanged() {}

	@Override
	protected void playerLoggedIn(PlayerLoggedInEvent e) {
		assertValidNBT(e.player);
	}
}
