package com.gamebuster19901.superiorquesting.common.questing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemReward extends Reward{
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
	public boolean canAward(EntityPlayer p) {
		return !p.isDead;
	}

	@Override
	public void award(EntityPlayer p) {
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
}
