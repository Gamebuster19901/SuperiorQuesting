package com.gamebuster19901.superiorquesting.client.gui.book;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.util.Shape;

import net.minecraft.client.gui.GuiButton;

public class QuestButton extends GuiButton{
	private UUID quest;
	private int importance = Main.proxy.getGlobalQuestHandler().getQuest(quest).getImportance();
	private Shape shape;
	public QuestButton(int buttonId, int x, int y, UUID Quest) {
		super(buttonId, x, y, "");
		//questbuttons can be in different shapes, width and height are useless in this context
		this.width = -1;
		this.height = -1;
		//this.shape = Main.proxy.getGlobalQuestHandler().getQuest(quest).getShape();
	}
}
