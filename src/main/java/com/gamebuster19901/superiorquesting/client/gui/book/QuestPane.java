package com.gamebuster19901.superiorquesting.client.gui.book;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.gui.GuiButton;

public class QuestPane extends GuiButton{
	private UUID page;
	private ArrayList<QuestButton> questButtons;
	private QuestButton hoveredButton;
	public QuestPane(int buttonID, int x, int y, UUID page) {
		super(buttonID, x, y, 221, 145, "");
		this.page = page;
	}
	
	
}
