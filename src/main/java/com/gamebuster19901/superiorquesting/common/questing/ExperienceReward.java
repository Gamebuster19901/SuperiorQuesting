package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class ExperienceReward extends Reward{
	private static final long serialVersionUID = 0L;
	
	private long VERSION = serialVersionUID;
	private int amount;
	private boolean isLevels;
	
	private transient RenderXPOrb renderer = new RenderXPOrb(Minecraft.getMinecraft().getRenderManager());
	private transient EntityXPOrb orb = new EntityXPOrb(null, 0, 0, 0, 2477);
	
	public ExperienceReward(Quest quest, int exp, boolean isLevels) {
		super(quest, true); //quests cannot have more than one experience reward
		amount = exp;
		this.isLevels = isLevels; 
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
	
	@Override
	public void render(int x, int y) {
        renderer.doRender(orb, x, y, 0, 0, 0f);
        String suffix = (isLevels) ? "L" : "xP";
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, new ItemStack(Items.AIR), x, y, amount + suffix);
	}
	
	public int getAmount() {
		return amount;
	}
	
	public boolean isLevels() {
		return isLevels;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof ExperienceReward) {
			ExperienceReward r = (ExperienceReward)o;
			return r.isLevels() == isLevels && r.getAmount() == getAmount();
		}
		return false;
	}

	@Override
	public void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	@Override
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.mark(8);
		long inVersion = in.readLong();
		if(inVersion == serialVersionUID) {
			in.reset();
			in.defaultReadObject();
		}
		else {
			convert(serialVersionUID, inVersion, in);
		}
	}
	
	@Override
	public void convert(long prevVersion, long nextVersion, ObjectInputStream in) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);
			}
			if(nextVersion == VERSION) {
				throw new AssertionError(new IllegalArgumentException(prevVersion + " == " + nextVersion));
			}
			if(nextVersion > prevVersion + 1L) {
				convert(prevVersion, nextVersion - 1L, in);
				return;
			}
		

			if(prevVersion == 0L && nextVersion == 1L) {
				Main.LOGGER.log(Level.INFO, "Converting quest from version " + prevVersion + " to version " + nextVersion);
				throw new FutureVersionError("1 is a future version, currently on version 0");
			}
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + "to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
			throw new VersioningError(e);
		}
	}
}
