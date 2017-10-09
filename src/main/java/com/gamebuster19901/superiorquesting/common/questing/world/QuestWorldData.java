package com.gamebuster19901.superiorquesting.common.questing.world;

import java.io.ObjectInputStream;
import java.util.Set;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.Reward;
import com.gamebuster19901.superiorquesting.common.questing.Task;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QuestWorldData extends WorldSavedData implements UpdatableSerializable, Assertable{
	public static final long VERSION = 1L;
	public static QuestWorldData instance = null;
	public QuestWorldData(String name) {
		super(name);
	}

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
		
			Set<String> keyset = nbt.getKeySet();
			for(String key : keyset) {
				Main.proxy.getQuestHandler().add(new Quest(nbt.getCompoundTag(key)));
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
		return compound;
	}
	
	private static void setInstance(QuestWorldData data) {
		instance = data;
	}
	
	private static QuestWorldData getInstance(World w) {
		return (QuestWorldData) w.getMapStorage().getOrLoadData(QuestWorldData.class, "Quest");
	}

	@Override
	public long getVersion() {
		// TODO Auto-generated method stub
		return VERSION;
	}
}
