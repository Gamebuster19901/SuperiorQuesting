package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.NonExistantKeyException;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;
import com.gamebuster19901.superiorquesting.common.questing.world.QuestWorldData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public final class GlobalQuestHandler extends MultiplayerHandler implements Debuggable{
	public static final String QUEST_KEY = MODID + ":quests";
	public static final String REWARD_KEY = MODID + ":rewards";
	public static final String TASK_KEY = MODID + ":tasks";
	
	public static final NBTTagCompound BUFFER = new NBTTagCompound();
	
	static final HashMap<UUID, Quest> QUESTS = new HashMap<UUID, Quest>();
	static final HashMap<UUID, Task> TASKS = new HashMap<UUID, Task>();
	static final HashMap<UUID, Reward> REWARDS = new HashMap<UUID, Reward>();
	static World world;
	
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
	
	public final void add(MinecraftServer server, boolean markDirty, Quest quest) {
		if(QUESTS.containsKey(quest.getUUID())) {
			throw new DuplicateKeyException("Quest " + quest.getUUID().toString());
		}
		if(markDirty) {
			markDirty();
		}
		QUESTS.put(quest.getUUID(), quest);
		getPlayerQuestHandler().add(server, quest);
		if(markDirty) {
			markDirty();
		}
	}
	
	public final void add(MinecraftServer server, boolean markDirty, Task task) {
		if(TASKS.containsKey(task.getUUID())) {
			throw new DuplicateKeyException("Task " + task.getUUID().toString());
		}
		if(markDirty) {
			markDirty();
		}
		TASKS.put(task.getUUID(), task);
		getPlayerQuestHandler().add(server, task);
		if(markDirty) {
			markDirty();
		}
	}
	
	public void add(MinecraftServer server, boolean markDirty, Reward reward) {
		if(REWARDS.containsKey(reward.getUUID())) {
			throw new DuplicateKeyException("Reward " + reward.getUUID());
		}
		if(markDirty) {
			markDirty();
		}
		REWARDS.put(reward.getUUID(), reward);
		getPlayerQuestHandler().add(server, reward);
		if(markDirty) {
			markDirty();
		}
	}
	
	private final void removeQuest(MinecraftServer server, UUID uuid) {
		if(!QUESTS.containsKey(uuid)) {
			throw new NonExistantKeyException("Quest " + uuid);
		}
		markDirty();
		QUESTS.remove(uuid);
		markDirty();
	}
	
	private final void removeTask(MinecraftServer server, UUID uuid) {
		if(!TASKS.containsKey(uuid)) {
			throw new NonExistantKeyException("Task " + uuid);
		}
		markDirty();
		TASKS.remove(uuid);
		markDirty();
	}
	
	private final void removeReward(MinecraftServer server, UUID uuid) {
		if(!REWARDS.containsKey(uuid)) {
			throw new NonExistantKeyException("Reward " + uuid.toString());
		}
		markDirty();
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
	
	private final void markDirty() {
		QuestWorldData.get(world).markDirty();
	}
	
	private final ArrayList<EntityPlayer> getAllOnlinePlayers(MinecraftServer server) {
		ArrayList<EntityPlayer> onlineplayers = new ArrayList<EntityPlayer>();
		for(String username : server.getOnlinePlayerNames()) {
			EntityPlayer p = server.getPlayerList().getPlayerByUsername(username);
			if(p != null) {
				onlineplayers.add(p);
			}
		}
		return onlineplayers;
	}
	
	private final PlayerQuestHandler getPlayerQuestHandler() {
		return Main.proxy.getPlayerQuestHandler();
	}
}
