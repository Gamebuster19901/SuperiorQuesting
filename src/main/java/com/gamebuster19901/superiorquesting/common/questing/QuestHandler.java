package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.Main.MODID;
import static com.gamebuster19901.superiorquesting.common.questing.Assignment.UNLOCKED;
import static com.gamebuster19901.superiorquesting.common.questing.Assignment.COMPLETED;
import static com.gamebuster19901.superiorquesting.common.questing.Assignment.NOTIFIED;
import static com.gamebuster19901.superiorquesting.common.questing.Rewardable.COLLECTED;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.MultiplayerHandler;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.NonExistantKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;
import com.gamebuster19901.superiorquesting.common.questing.world.QuestWorldData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public final class QuestHandler extends MultiplayerHandler {
	private static final String QUEST_KEY = MODID + ":quests";
	private static final String REWARD_KEY = MODID + ":rewards";
	private static final String TASK_KEY = MODID + ":tasks";
	
	private final HashMap<UUID, Quest> QUESTS = new HashMap<UUID, Quest>();
	private final HashMap<UUID, Task> TASKS = new HashMap<UUID, Task>();
	private final HashMap<UUID, Reward> REWARDS = new HashMap<UUID, Reward>();
	
	public final ArrayList<Quest> getCompletedQuests(EntityPlayer p){
		final ArrayList<Quest> ret = new ArrayList<Quest>();
		for(UUID u : QUESTS.keySet()) {
			Quest q = getQuest(u);
			if(q.isFinished(p)) {
				ret.add(q);
			}
		}
		return ret;
	}
	
	public final ArrayList<Quest> getAllQuests(){
		final ArrayList<Quest> ret = new ArrayList<Quest>();
		for(UUID s : QUESTS.keySet()) {
			ret.add(getQuest(s));
		}
		return ret;
	}
	
	public final ArrayList<Task> getAllTasks(){
		final ArrayList<Task> ret = new ArrayList<Task>();
		for(UUID s : TASKS.keySet()) {
			ret.add(getTask(s));
		}
		return ret;
	}
	
	public final ArrayList<Reward> getAllRewards(){
		final ArrayList<Reward> ret = new ArrayList<Reward>();
		for(UUID u : REWARDS.keySet()) {
			ret.add(getReward(u));
		}
		return ret;
	}
	
	public final void add(Quest quest) {
		if(QUESTS.containsKey(quest.getUUID())) {
			throw new DuplicateKeyException("Quest " + quest.getUUID().toString());
		}
		QUESTS.put(quest.getUUID(), quest);
		markDirty();
	}
	
	public final void add(Task task) {
		if(TASKS.containsKey(task.getUUID())) {
			throw new DuplicateKeyException("Task " + task.getUUID().toString());
		}
		TASKS.put(task.getUUID(), task);
		markDirty();
	}
	
	public void add(Reward reward) {
		if(REWARDS.containsKey(reward.getUUID())) {
			throw new DuplicateKeyException("Reward " + reward.getUUID());
		}
		REWARDS.put(reward.getUUID(), reward);
		markDirty();
	}
	
	private final void removeQuest(UUID uuid) {
		if(!QUESTS.containsKey(uuid)) {
			throw new NonExistantKeyException("Quest " + uuid);
		}
		QUESTS.remove(uuid);
		markDirty();
	}
	
	private final void removeTask(UUID uuid) {
		if(!TASKS.containsKey(uuid)) {
			throw new NonExistantKeyException("Task " + uuid);
		}
		TASKS.remove(uuid);
		markDirty();
	}
	
	private final void removeReward(UUID uuid) {
		if(!REWARDS.containsKey(uuid)) {
			throw new NonExistantKeyException("Reward " + uuid.toString());
		}
		REWARDS.remove(uuid);
		markDirty();
	}
	
	final Quest getQuest(UUID uuid) {
		return QUESTS.get(uuid);
	}
	
	final Task getTask(UUID uuid) {
		return TASKS.get(uuid);
	}
	
	final Reward getReward(UUID uuid) {
		return REWARDS.get(uuid);
	}
	
	private final void setQuests(NBTTagCompound nbt) {
		QUESTS.clear();
		for(String key : nbt.getKeySet()) {
			QUESTS.put(UUID.fromString(key), new Quest(nbt.getCompoundTag(key)));
		}
		markDirty();
	}
	
	private final void setTasks(NBTTagCompound nbt) {
		TASKS.clear();
		for(String key : nbt.getKeySet()) {
			NBTTagCompound data = nbt.getCompoundTag(key);
			Task t;
			try {
				Class<? extends Task> tsk = (Class<? extends Task>) Class.forName(data.getString("CLASS"));
				t = tsk.newInstance();
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			t.deserializeNBT(data);
			TASKS.put(UUID.fromString(key), t);
		}
		markDirty();
	}
	
	private final void setRewards(NBTTagCompound nbt) {
		REWARDS.clear();
		for(String key : nbt.getKeySet()) {
			NBTTagCompound data = nbt.getCompoundTag(key);
			Reward r;
			try {
				Class<? extends Reward> rew = (Class<? extends Reward>) Class.forName(data.getString("CLASS"));
				r = rew.newInstance();
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
				throw new RuntimeException(e);
			}
			r.deserializeNBT(data);
			REWARDS.put(UUID.fromString(key), r);
		}
		markDirty();
	}
	
	NBTTagCompound getQuestNBT(UUID id, EntityPlayer p) {
		Assert(hasQuestNBT(id, p), "quest " + id + " not found");
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getQuestNBT(UUID id, UUID p) {
		Assert(hasQuestNBT(id, p), "quest " + id + " not found");
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).getCompoundTag(id.toString());
	}
	
	boolean hasQuestNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	boolean hasQuestNBT(UUID p) {
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	boolean hasQuestNBT(UUID quest, EntityPlayer p) {
		Assert(hasQuestNBT(p), "No quest nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	boolean hasQuestNBT(UUID quest, UUID p) {
		Assert(hasQuestNBT(p), "No quest nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest.toString());
	}
	
	NBTTagCompound getRewardNBT(UUID id, EntityPlayer p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found");
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	NBTTagCompound getRewardNBT(UUID id, UUID p) {
		Assert(hasRewardNBT(id, p), "reward " + id + " not found");
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).getCompoundTag(id.toString());
	}
	
	boolean hasRewardNBT(EntityPlayer p) {
		return getPersistantTag(p).hasKey(REWARD_KEY);
	}
	
	boolean hasRewardNBT(UUID p) {
		return getPersistantTag(p).hasKey(REWARD_KEY);
	}
	
	boolean hasRewardNBT(UUID reward, EntityPlayer p) {
		Assert(hasRewardNBT(p), "No reward nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).hasKey(reward.toString());
	}
	
	boolean hasRewardNBT(UUID reward, UUID p) {
		Assert(hasRewardNBT(p), "No reward nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(REWARD_KEY).hasKey(reward.toString());
	}
	
	NBTTagCompound getTaskNBT(UUID task, EntityPlayer p) {
		Assert(hasTaskNBT(task, p), "task " + task + " not found");
		return getPersistantTag(p).getCompoundTag(TASK_KEY).getCompoundTag(task.toString());
	}
	
	NBTTagCompound getTaskNBT(UUID task, UUID p) {
		Assert(hasTaskNBT(task, p), "task " + task + " not found");
		return getPersistantTag(p).getCompoundTag(TASK_KEY).getCompoundTag(task.toString());
	}
	
	boolean hasTaskNBT(EntityPlayer p) {
		return getPersistantTag(p).hasKey(TASK_KEY);
	}
	
	boolean hasTaskNBT(UUID p) {
		return getPersistantTag(p).hasKey(TASK_KEY);
	}
	
	boolean hasTaskNBT(UUID task, EntityPlayer p) {
		Assert(hasTaskNBT(p), "No task nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(TASK_KEY).hasKey(task.toString());
	}
	
	boolean hasTaskNBT(UUID task, UUID p) {
		Assert(hasTaskNBT(p), "No task nbt for player " + p);
		return getPersistantTag(p).getCompoundTag(TASK_KEY).hasKey(task.toString());
	}
	
	/**
	 * If the player's quest NBT is not up to date, update the player's quest nbt
	 * @param p The player to check
	 * @return true if the player's life count changed
	 */
	public void assertValidNBT(EntityPlayer p) {
		if(hasQuestNBT(p)){
			for(UUID key : QUESTS.keySet()) {
				Quest q = QUESTS.get(key);
				NBTTagCompound tag = getPersistantTag(p).getCompoundTag(key.toString());
				
				
			}
		}
		else{
			resetQuests(p);
		}
	}
	
	/**
	 * resets a players quest NBT, 
	 * @param p
	 */
	public void resetQuests(EntityPlayer p) {
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
	
	

	@Override
	protected void onConfigFinishChanged() {}

	@Override
	protected void playerLoggedIn(PlayerLoggedInEvent e) {
		if(hasQuestNBT(e.player)) {
			
		}
	}
	
	private final void markDirty() {
		Assert(QuestWorldData.instance != null);
		QuestWorldData.instance.markDirty();
	}
}
