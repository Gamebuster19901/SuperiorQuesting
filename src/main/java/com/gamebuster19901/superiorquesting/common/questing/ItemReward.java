package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class ItemReward extends Reward{
	private static final long serialVersionUID = 0L;
	private long VERSION = serialVersionUID;
	private ItemStackSerializationWrapper reward;
	
	public ItemReward(Quest quest, ItemStack i) {
		super(quest, false); //quests can have more than one item reward
		if(!i.isEmpty()) {
			reward = new ItemStackSerializationWrapper(i);
			return;
		}
		throw new IllegalArgumentException("Cannot have an item reward be an empty itemstack");
	}
	@Override
	public boolean canCollect(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void collect(EntityPlayer p) {
		new EntityItem(p.getEntityWorld(), p.posX, p.posY, p.posZ, reward.asItem());
	}
	
	@Override
	public void render(int x, int y) {
		if(!reward.asItem().isEmpty()) {
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(reward.asItem(), x, y);
			Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, reward.asItem(), x, y, "" + reward.asItem().getCount());
		}
		else {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, 0, 0, 16, 16);
		}
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof ItemReward) {
			ItemReward r = (ItemReward)o;
			return ItemStack.areItemStacksEqual(reward.asItem(), r.reward.asItem());
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
