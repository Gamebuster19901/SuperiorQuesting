package com.gamebuster19901.superiorquesting.common.questing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class Reward implements Rewardable{
	private final Quest parent;
	
	//used to check if a quest is allowed to have more than one of this reward type
	private boolean collected;
	private UUID id;
	
	public Reward(Quest parent) {
		this.parent = parent;
		this.id = UUID.randomUUID();
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

	@Override
	public final boolean hasCollected(EntityPlayer p) {
		return parent.hasCollected(p);
	}
	
	@Override
	public final boolean hasCollected(UUID p) {
		return parent.hasCollected(p);
	}

	@Override
	public final void markCollected(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void markCollected(UUID p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void markUncollected(EntityPlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void markUncollected(UUID p) {
		// TODO Auto-generated method stub
		
	}
}
