package com.gamebuster19901.superiorquesting.client.gui;

import com.gamebuster19901.superiorquesting.client.gui.book.GuiQuestBook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {
	public static final int QUEST_BOOK = 0;
	public static final int FINAL_DEATH = 1;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == QUEST_BOOK) {
			return new GuiQuestBook(player);
		}
		if(ID == FINAL_DEATH) {
			return new GuiTrueGameOver(GuiTrueGameOver.deathCause);
		}
		return null;
	}
}
