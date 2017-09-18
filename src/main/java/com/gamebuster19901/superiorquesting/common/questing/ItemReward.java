package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemReward extends Reward{
	private ItemStack reward;
	
	public ItemReward(ItemStack i) {
		if(!i.isEmpty()) {
			reward = i;
			return;
		}
		throw new IllegalArgumentException("Cannot have an item reward be an empty itemstack");
	}
	@Override
	public boolean canAward(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void award(EntityPlayer p) {
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
	public boolean equals(Object o) {
		if(o.getClass() == this.getClass()) {
			return reward.equals(((ItemReward)o).reward);
		}
		return false;
	}
}
