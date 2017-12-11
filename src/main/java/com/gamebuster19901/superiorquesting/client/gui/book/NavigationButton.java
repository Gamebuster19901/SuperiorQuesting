package com.gamebuster19901.superiorquesting.client.gui.book;

import com.gamebuster19901.superiorquesting.common.Unique;

import net.minecraft.client.gui.GuiButton;

public class NavigationButton extends GuiButton{
	private Unique u;

	public NavigationButton(int buttonId, int x, int y, Direction d) {
		super(buttonId, x, y, "");
		// TODO Auto-generated constructor stub
	}
	
	public enum Direction {
		LEFT,
		RIGHT;
	}
	
}
