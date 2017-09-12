package com.gamebuster19901.superiorquesting.common.questing;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public abstract class Reward implements Rewardable{

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
	
}
