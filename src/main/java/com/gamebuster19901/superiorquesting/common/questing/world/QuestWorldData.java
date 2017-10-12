package com.gamebuster19901.superiorquesting.common.questing.world;

import static com.gamebuster19901.superiorquesting.common.questing.QuestHandler.QUEST_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.QuestHandler.REWARD_KEY;
import static com.gamebuster19901.superiorquesting.common.questing.QuestHandler.TASK_KEY;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.Reward;
import com.gamebuster19901.superiorquesting.common.questing.Task;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class QuestWorldData extends WorldSavedData implements UpdatableSerializable, Assertable{
	public QuestWorldData(String name) {
		super(name);
	}

	public static final long VERSION = 1L;
	public static QuestWorldData instance = null;

	/**
	 * Converts the quest data in nbtIn to the specified version recursively.
	 * 
	 * For example, when converting from version 1 to 4, it will convert version 1 to version 2,
	 * then version 2 to version 3, then version 3 to version 4.
	 * 
	 * @param prevVersion the version to convert from
	 * @param nextVersion the version to convert to
	 * @param nbtIn the data to convert
	 */
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
	public void readFromNBT(NBTTagCompound nbt) {
		int ver = nbt.getInteger("VERSION");
		if(ver == VERSION) {
			NBTTagCompound quests = nbt.getCompoundTag(QUEST_KEY);
			for(String key : quests.getKeySet()) {
				Main.proxy.getQuestHandler().add(new Quest(nbt.getCompoundTag(key)));
			}
			NBTTagCompound rewards = nbt.getCompoundTag(REWARD_KEY);
			Class<? extends Reward> reward = Reward.class;
			for(String key : rewards.getKeySet()) {
				try {
					reward = (Class<? extends Reward>) Class.forName(rewards.getString("CLASS"));
					Constructor<? extends Reward> constructor = reward.getConstructor(NBTTagCompound.class);
					constructor.newInstance(nbt.getCompoundTag(key));
				} catch (ClassNotFoundException ex) {
					NoClassDefFoundError er = new NoClassDefFoundError("Missing class, most likely a missing dependee");
					er.initCause(ex);
					throw er;
				} catch (NoSuchMethodException ex1) {
					LinkageError er1 = new LinkageError();
					er1.initCause(new VersioningError("Missing NBTTagCompound Constructor in " + reward.getCanonicalName(), ex1));
					throw er1;
				} catch (InstantiationException ex2) {
					InstantiationError er2 = new InstantiationError();
					er2.initCause(er2);
					throw er2;
				} catch (IllegalAccessException ex3) {
					IllegalAccessError er3 = new IllegalAccessError();
					er3.initCause(ex3);
					throw er3;
				} catch (IllegalArgumentException ex4) {
					LinkageError er4 = new LinkageError();
					er4.initCause(ex4);
					throw er4;
				} catch (InvocationTargetException ex5) {
					LinkageError er5 = new LinkageError();
					er5.initCause(ex5);
					throw er5;
				}
			}
			
			NBTTagCompound tasks = nbt.getCompoundTag(TASK_KEY);
			Class<? extends Task> task = Task.class;
			for(String key : tasks.getKeySet()) {
				try {
					task = (Class<? extends Task>) Class.forName(tasks.getString("CLASS"));
					Constructor<? extends Task> constructor = task.getConstructor(NBTTagCompound.class);
					constructor.newInstance(nbt.getCompoundTag(key));
				} catch (ClassNotFoundException ex) {
					NoClassDefFoundError er = new NoClassDefFoundError("Missing class, most likely a missing dependee");
					er.initCause(ex);
					throw er;
				} catch (NoSuchMethodException ex1) {
					LinkageError er1 = new LinkageError();
					er1.initCause(new VersioningError("Missing NBTTagCompound Constructor in " + task.getCanonicalName(), ex1));
					throw er1;
				} catch (InstantiationException ex2) {
					InstantiationError er2 = new InstantiationError();
					er2.initCause(er2);
					throw er2;
				} catch (IllegalAccessException ex3) {
					IllegalAccessError er3 = new IllegalAccessError();
					er3.initCause(ex3);
					throw er3;
				} catch (IllegalArgumentException ex4) {
					LinkageError er4 = new LinkageError();
					er4.initCause(ex4);
					throw er4;
				} catch (InvocationTargetException ex5) {
					LinkageError er5 = new LinkageError();
					er5.initCause(ex5);
					throw er5;
				}
			}
		}
		else {
			convert(VERSION, ver, nbt);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setLong("VERSION", VERSION);
		NBTTagCompound quests = new NBTTagCompound();
		NBTTagCompound tasks = new NBTTagCompound();
		NBTTagCompound rewards = new NBTTagCompound();
		for(Quest q : Main.proxy.getQuestHandler().getAllQuests()) {
			quests.setTag(q.getUUID().toString(), q.serializeNBT());
		}
		for(Task t : Main.proxy.getQuestHandler().getAllTasks()) {
			tasks.setTag(t.getUUID().toString(), t.serializeNBT());
		}
		for(Reward r : Main.proxy.getQuestHandler().getAllRewards()) {
			tasks.setTag(r.getUUID().toString(), r.serializeNBT());
		}
		compound.setTag(QUEST_KEY, quests);
		compound.setTag(TASK_KEY, tasks);
		compound.setTag(REWARD_KEY, rewards);
		return compound;
	}
	
	public static QuestWorldData get(World w) {
		MapStorage storage = w.getMapStorage();
		instance = (QuestWorldData) storage.getOrLoadData(QuestWorldData.class, "Quest");
		
		if (instance == null) {
			instance = new QuestWorldData("Quest");
			storage.setData("Quest", instance);
		}
		return instance;
	}

	@Override
	public long getVersion() {
		// TODO Auto-generated method stub
		return VERSION;
	}
}