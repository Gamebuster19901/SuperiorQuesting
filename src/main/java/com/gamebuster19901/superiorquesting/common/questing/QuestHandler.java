package com.gamebuster19901.superiorquesting.common.questing;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.MultiplayerHandler;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.NonExistantKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public final class QuestHandler extends MultiplayerHandler implements Serializable{
	private static final String QUEST_KEY = MODID + ":quests"; 
	private static final HashMap<String,Quest> QUESTS = new HashMap<String, Quest>();
	private static final String DLMR = (char)0x00A7 + "";
	
	private static final int VERSION = 0;
	private static final File QUEST_DATA_FILE = new File(Main.proxy.getQuestDirectory().getAbsolutePath() + "/quests.config");
	static {
		if (!QUEST_DATA_FILE.exists()) {
			QUEST_DATA_FILE.getParentFile().mkdirs();
		}
	}
	
	private final void convertConfig(int prevVersion, int nextVersion) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);
			}
			if(nextVersion == VERSION) {
				throw new AssertionError(new IllegalArgumentException(prevVersion + " == " + nextVersion));
			}
			if(nextVersion > prevVersion + 1) {
				convertConfig(prevVersion, nextVersion - 1);
				return;
			}
		

			if(prevVersion == 0 && nextVersion == 1) {
				Main.LOGGER.log(Level.INFO, "Converting quests from version " + prevVersion + " to version " + nextVersion);
				return;
			}
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + "to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
			throw new VersioningError(e);
		}
	}
	
	private final void add(String title, Quest quest) {
		if(QUESTS.containsKey(title)) {
			throw new DuplicateKeyException(title);
		}
		QUESTS.put(title, quest);
	}
	
	private final void remove(String title) {
		if(!QUESTS.containsKey(title)) {
			throw new NonExistantKeyException(title);
		}
		QUESTS.remove(title);
	}
	
	private final boolean ChangeTitle(String prevTitle, String newTitle, Quest quest) {
		if(QUESTS.containsKey(prevTitle) && !QUESTS.containsKey(newTitle)) {
			Quest q = QUESTS.remove(prevTitle);
			QUESTS.put(newTitle, q);
			return true;
		}
		return false;
	}
	
	private final int getConfigFileVersion() {
		int x = -1;
		Stream<String> lines = null;
		try{
			lines = Files.lines(Paths.get(QUEST_DATA_FILE.toURI()));
			x = Integer.parseInt(lines.skip(0).findFirst().get());
		} catch (IOException e) {
			throw new VersioningError(e);
		}
		finally {
			lines.close();
			Debuggable.debug(x, null);
		}
		return x;
	}
	
	private final void saveOverConfigFile() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(QUEST_DATA_FILE.toURI().toString()));
			for(Quest q : QUESTS.values()) {
				String data = DLMR + q.getTitle() + DLMR + q.getDescription() + DLMR + "prereqs" + DLMR;
				for(Assignment a : q.prerequisites()) {
					data = data + a.getClass().getName() + DLMR + a.toString() + DLMR;
				}
				pw.write(DLMR + q.getTitle() + DLMR + q.getDescription() + DLMR + data);
			}
		}
		catch(IOException e) {
			
		}
		finally {
			pw.close();
		}
	}
	
	private final void saveOverHashMap() {
		
	}
	
	final Quest getQuest(String title) {
		return QUESTS.get(title);
	}
	
	NBTTagCompound getQuestNBT(String quest, EntityPlayer p) {
		return getPersistantTag(p).getCompoundTag(quest);
	}
	
	public boolean hasQuestNBT(EntityPlayer p){
		return getPersistantTag(p).hasKey(QUEST_KEY);
	}
	
	public boolean hasQuestNBT(String quest, EntityPlayer p) {
		return getPersistantTag(p).getCompoundTag(QUEST_KEY).hasKey(quest);
	}
	
	/**
	 * If the player's quest NBT is not up to date, update the player's quest nbt
	 * @param p The player to check
	 * @return true if the player's life count changed
	 */
	public void assertValidNBT(EntityPlayer p) {
		if(hasQuestNBT(p)){
			for(String key : QUESTS.keySet()) {
				Quest q = QUESTS.get(key);
				NBTTagCompound tag = getPersistantTag(p).getCompoundTag(key);
				
				
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
		for(String key : QUESTS.keySet()) { //key is the quest title
			Quest q = QUESTS.get(key);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("UNLOCKED", false);
			nbt.setBoolean("COMPLETED", false);
			nbt.setBoolean("NOTIFIED", false);
			nbt.setBoolean("COLLECTED", false);
			getPersistantTag(p).setTag(key, nbt);
		}
	}

	@Override
	protected void onConfigFinishChanged() {}

	@Override
	protected void playerLoggedIn(PlayerLoggedInEvent e) {
		if(hasQuestNBT(e.player)) {
			
		}
	}
}
