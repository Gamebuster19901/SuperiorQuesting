package com.gamebuster19901.superiorquesting.client.gui;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public final class GuiQuestBook extends GuiScreen{
	public static final ResourceLocation COVER_TEXTURE = new ResourceLocation(MODID + ":textures/gui/bookCover.png");
	public static final ResourceLocation FULLSCREEN_TEXTURE = new ResourceLocation(MODID + ":textures/gui/bookFullScreen.png");
	public static final ResourceLocation TWO_PAGES_TEXTURE = new ResourceLocation(MODID + ":textures/gui.bookTwoSides.png");
	
	private static int state = 0; //which of the above textures to use
	private static int page = -1; // see below comment
	
	/*positive values correspond to quest categories, negative values are reserved, 0 is invalid
	 	*current reserved values:
	 		* -1 Cover
	 		* -2 Main Menu
	*/
	
	private EntityPlayer player;
	private boolean editMode = false;
	
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
