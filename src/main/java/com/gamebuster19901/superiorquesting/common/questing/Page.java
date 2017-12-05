package com.gamebuster19901.superiorquesting.common.questing;

import java.util.TreeSet;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Hideable;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Lockable;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Notifyable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class Page implements Lockable, Hideable, Notifyable, Comparable, Assertable{
	private static final long VERSION = 1L;
	private UUID id;
	private final TreeSet<UUID> quests = new TreeSet<UUID>();
	private String title;
	private int order;
	private boolean hiddenByDefault;
	private boolean lockedByDefault;
	
	public Page(String title) {
		this(title, Main.proxy.getGlobalQuestHandler().PAGES.size());
	}
	
	public Page(String title, int order) {
		this(title, order, false, false);
	}
	
	public Page(String title, int order, boolean hiddenByDefault, boolean lockedByDefault) {
		this(UUID.randomUUID(), title, order, hiddenByDefault, lockedByDefault);
	}
	
	private Page(UUID id, String title, int order, boolean hiddenByDefault, boolean lockedByDefault) {
		try {
			if(id == null) {
				throw new NullPointerException("id");
			}
			if(title == null) {
				throw new NullPointerException("title");
			}
			if(order < 1) {
				throw new IndexOutOfBoundsException(order + " < 1 (order)");
			}
		}
		catch(NullPointerException | IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Programming error, report to Gamebuster1990");
		}
		this.id = id;
		this.title = title;
		this.order = order;
		this.hiddenByDefault = hiddenByDefault;
		this.lockedByDefault = lockedByDefault;
	}
	
	public Page(NBTTagCompound nbt) {
		deserializeNBT(nbt);
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", VERSION);
		nbt.setString("UUID", id.toString());
		NBTTagList quests = new NBTTagList();
		for(UUID q : this.quests) {
			quests.appendTag(new NBTTagString(q.toString()));
		}
		nbt.setTag("QUESTS", quests);
		nbt.setString("TITLE", title);
		nbt.setInteger("ORDER", order);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		long ver = nbt.getLong("VERSION");
		if (ver == VERSION) {
			id = UUID.fromString(nbt.getString("UUID"));
			NBTTagList quests = nbt.getTagList("QUESTS", 8);
			for(NBTBase qBase : quests) {
				NBTTagString q = (NBTTagString)qBase;
				this.quests.add(UUID.fromString(q.getString()));
			}
			title = nbt.getString("TITLE");
			order = nbt.getInteger("ORDER");
		}
		else {
			convert(ver, VERSION, nbt);
		}
	}
	
	@Override
	public NBTTagCompound getDefaultPlayerNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", getVersion());
		nbt.setString("UUID", getUUID().toString());
		nbt.setBoolean("NOTIFIED", false);
		nbt.setBoolean("HIDDEN", isHiddenByDefault());
		nbt.setBoolean("LOCKED", isLockedByDefault());
		return nbt;
	}

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
	public long getVersion() {
		return VERSION;
	}

	@Override
	public final int compareTo(Object o) {
		if(o instanceof Page) {
			Page p = (Page)o;
			if(this.order < p.order) {
				return -1;
			}
			else if (this.order > p.order) {
				return 1;
			}
			return 0;
		}
		throw new IllegalArgumentException("Cannot compare with something that is not a page " + o.getClass() + " is not a subclass of " + Page.class);
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public boolean hasNotified(EntityPlayer p) {
		return getPlayerQuestHandler().getPageNBT(this.getUUID(), p).getBoolean(NOTIFIED);
	}

	@Override
	public boolean hasNotified(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(NOTIFIED);
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

	@Override
	public void markUnnotified(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markUnnotified(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isHidden(EntityPlayer p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}

	@Override
	public boolean isHidden(UUID p) {
		return getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).getBoolean(HIDDEN);
	}

	@Override
	public void hide(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void hide(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, true);
	}

	@Override
	public void unhide(EntityPlayer p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
	}

	@Override
	public void unhide(UUID p) {
		getPlayerQuestHandler().getQuestNBT(this.getUUID(), p).setBoolean(HIDDEN, false);
	}

	@Override
	public boolean isHiddenByDefault() {
		return hiddenByDefault;
	}

	@Override
	public void lock(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lock(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlock(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlock(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUnlocked(EntityPlayer p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnlocked(UUID p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLockedByDefault() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private final PlayerQuestHandler getPlayerQuestHandler() {
		return Main.proxy.getPlayerQuestHandler();
	}

}
