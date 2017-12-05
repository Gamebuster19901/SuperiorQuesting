package com.gamebuster19901.superiorquesting.common.questing.reward;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.integrate.Rewardable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class Reward implements Rewardable, Assertable{
	private static final String REWARD_KEY = "rewards";
	private static final String COLLECTED_KEY = "collected-";
	private UUID id;
	
	public Reward(Quest q) {
		id = UUID.randomUUID();
		getGlobalQuestHandler().add(true, this);
		q.addReward(this.getUUID());
	}
	
	public Reward(NBTTagCompound nbt) {
		deserializeNBT(nbt);
		getGlobalQuestHandler().add(false, this);
	}
	
	/*
	/*
	 * renders this reward onto the current GUI, at x,y
	 * @param x
	 * @param y
	 *
	public void render(int x, int y) {
		ResourceLocation i = null;
		try {
			i = RewardType.getRewardType(this).image;
		}
		catch(IllegalArgumentException | IllegalStateException e) {
			Main.LOGGER.log(Level.ERROR, e.getMessage(), e);
		}
		
		if(i != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(i);
		}
		else {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
		}
		
		Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, 0, 0, 16, 16);
	}
	*/
	
	public final NBTTagCompound getDefaultPlayerNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", getVersion());
		nbt.setString("UUID", getUUID().toString());
		nbt.setBoolean("COLLECTED", false);
		return nbt;
	}
	
	private final NBTTagCompound getRewardTag(EntityPlayer p) {
		return getPlayerQuestHandler().getRewardNBT(id, p);
	}
	
	private final NBTTagCompound getRewardTag(UUID p) {
		return getPlayerQuestHandler().getRewardNBT(id, p);
	}

	@Override
	public final boolean hasCollected(EntityPlayer p) {
		return getRewardTag(p).getBoolean(COLLECTED_KEY + id);
	}
	
	@Override
	public final boolean hasCollected(UUID p) {
		return getRewardTag(p).getBoolean(COLLECTED_KEY + id);
	}

	@Override
	public final void markCollected(EntityPlayer p) {
		getRewardTag(p).setBoolean(COLLECTED_KEY + id, true);
	}

	@Override
	public final void markCollected(UUID p) {
		getRewardTag(p).setBoolean(COLLECTED_KEY + id, true);
	}

	@Override
	public final void markUncollected(EntityPlayer p) {
		getRewardTag(p).setBoolean(COLLECTED_KEY + id, false);
	}

	@Override
	public final void markUncollected(UUID p) {
		getRewardTag(p).setBoolean(COLLECTED_KEY + id, false);
	}
	
	public final void setUUID(String ID) {
		Assert(id == null);
		id = UUID.fromString(ID);
	}
	
	public final void setUUID(UUID ID) {
		Assert(id == null);
		id = ID;
	}
	
	public final UUID getUUID() {
		return id;
	}
	
	public String toString() {
		return getUUID().toString();
	}
}
