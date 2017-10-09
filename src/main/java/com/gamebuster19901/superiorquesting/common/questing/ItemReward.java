package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class ItemReward extends Reward{
	private static final long VERSION = 0;
	private ItemStack reward;
	
	public ItemReward(Quest parent, ItemStack stack) {
		super(parent);
	}
	
	public ItemReward(NBTTagCompound nbt) {
		super(nbt);
	}
	
	@Override
	public boolean canCollect(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void collect(EntityPlayer p) {
		new EntityItem(p.getEntityWorld(), p.posX, p.posY, p.posZ, reward);
	}
	
	@Override
	public void render(int x, int y) {
		if(!reward.isEmpty()) {
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(reward, x, y);
			Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, reward, x, y, "" + reward.getCount());
		}
		else {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, 0, 0, 16, 16);
		}
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
		data.setTag("ITEMSTACK", reward.serializeNBT());
		return data;
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		long ver = nbt.getLong("VERSION");
		if(ver != VERSION) {
			convert(ver, VERSION, nbt);
		}
		else {
			this.reward = new ItemStack(nbt.getCompoundTag("ITEMSTACK"));
		}
	}

	@Override
	public long getVersion() {
		return VERSION;
	}
}
