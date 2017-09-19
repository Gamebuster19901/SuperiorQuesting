package com.gamebuster19901.superiorquesting.common.questing;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class Reward implements Rewardable{
	//used to check if a quest is allowed to have more than one of this reward type
	private final boolean unique;
	
	public Reward(boolean isUnique) {
		unique = isUnique;
	}
	
	/**
	 * renders this reward onto the current GUI, at x,y
	 * @param x
	 * @param y
	 */
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
	
	public final boolean hasRewarded(EntityPlayer p) {
		return true;
	}

	@Override
	public final int compareTo(Object o) {
		return 0;
	}
	
	@Override
	public abstract boolean equals(Object o);

	@Override
	public boolean hasCollected(EntityPlayer p) {
		//TODO check if player has collected rewards from this quest
	}
	
	public final boolean isUnique() {
		return unique;
	}
}
