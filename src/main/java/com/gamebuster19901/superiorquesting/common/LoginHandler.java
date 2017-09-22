package com.gamebuster19901.superiorquesting.common;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public final class LoginHandler extends MultiplayerHandler{
	private static final String LOGIN_KEY = MODID + ":loggedin"; 
	
	@Override
	protected void onConfigFinishChanged() {}

	@Override
	protected void playerLoggedIn(PlayerLoggedInEvent e) {
		if(!getPersistantTag((EntityPlayerMP)e.player).hasKey(LOGIN_KEY)){
			giveBook(e.player);
			getPersistantTag((EntityPlayerMP)e.player).setBoolean(LOGIN_KEY, true);
		}
	}
	
	private void giveBook(EntityPlayer p){
		if(ModConfig.RULES.hasBook){
			p.inventory.addItemStackToInventory(new ItemStack(ItemQuestBook.ITEM, 1));
		}
	}
}
