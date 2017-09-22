package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class ExperienceReward extends Reward{
	private transient RenderXPOrb renderer = new RenderXPOrb(Minecraft.getMinecraft().getRenderManager());
	private transient EntityXPOrb orb = new EntityXPOrb(null, 0, 0, 0, 2477);
	private int amount;
	private boolean isLevels;
	
	public ExperienceReward(Quest quest, int exp, boolean isLevels) {
		super(quest, true); //quests cannot have more than one experience reward
		amount = exp;
		this.isLevels = isLevels; 
	}
	
	@Override
	public boolean canAward(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void award(EntityPlayer p) {
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
}
