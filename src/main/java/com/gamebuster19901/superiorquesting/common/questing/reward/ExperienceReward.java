package com.gamebuster19901.superiorquesting.common.questing.reward;

import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public final class ExperienceReward extends Reward{
	private static final long VERSION = 1L;
	private int amount;
	private boolean isLevels;
	
	private RenderXPOrb renderer = new RenderXPOrb(Minecraft.getMinecraft().getRenderManager());
	private EntityXPOrb orb = new EntityXPOrb(null, 0, 0, 0, 2477);
	
	public ExperienceReward(Quest quest, int exp, boolean isLevels) {
		super(quest);
		amount = exp;
		this.isLevels = isLevels; 
	}
	
	public ExperienceReward(NBTTagCompound nbt) {
		super(nbt);
	}
	
	@Override
	public boolean canCollect(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void collect(EntityPlayer p) {
		if(isLevels) {
			p.addExperienceLevel(amount);
		}
		else {
			p.addExperience(amount);
		}
	}
	
	/*
	@Override
	public void render(int x, int y) {
        renderer.doRender(orb, x, y, 0, 0, 0f);
        String suffix = (isLevels) ? "L" : "xP";
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, new ItemStack(Items.AIR), x, y, amount + suffix);
	}
	*/
	
	public int getAmount() {
		return amount;
	}
	
	public boolean isLevels() {
		return isLevels;
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
	public NBTTagCompound serializeNBT() {
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("VERSION", VERSION);
		data.setString("CLASS", getClass().getCanonicalName());
		data.setString("UUID", getUUID().toString());
		data.setInteger("AMOUNT", amount);
		data.setBoolean("ISLEVELS", isLevels);
		return data;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		long ver = nbt.getLong("VERSION");
		if(ver != VERSION) {
			convert(ver, VERSION, nbt);
		}
		else {
			setUUID(nbt.getString("UUID"));
			amount = nbt.getInteger("AMOUNT");
			isLevels = nbt.getBoolean("ISLEVELS");
		}
	}

	@Override
	public long getVersion() {
		// TODO Auto-generated method stub
		return VERSION;
	}
}
