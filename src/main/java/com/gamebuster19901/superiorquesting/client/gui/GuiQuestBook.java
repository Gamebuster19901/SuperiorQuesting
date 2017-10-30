package com.gamebuster19901.superiorquesting.client.gui;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public final class GuiQuestBook extends GuiScreen{
	private EntityPlayer player;
	public GuiQuestBook(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void onGuiClosed(){
		player.playSound(((ClientProxy)Main.proxy).BOOK_CLOSE, 1, 1);
		super.onGuiClosed();
	}
	
}
