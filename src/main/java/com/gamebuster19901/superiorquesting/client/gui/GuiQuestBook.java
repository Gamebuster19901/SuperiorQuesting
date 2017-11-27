package com.gamebuster19901.superiorquesting.client.gui;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public final class GuiQuestBook extends GuiScreen implements Assertable{
	public static final ResourceLocation COVER_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_cover.png");
	public static final ResourceLocation FULLSCREEN_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_fullcreen.png");
	public static final ResourceLocation TWO_PAGES_TEXTURE = new ResourceLocation(MODID + ":textures/gui.book_two_sides.png");
	
	/*positive values correspond to quest categories, negative values are reserved, 0 is invalid
 		*current reserved values:
 			* -1 Cover
 			* -2 Main Menu
 	*/
	private static int page = -1;
	private static UUID lastOpenQuest;
	private static boolean editMode = false;

	
	private EntityPlayer player;

	
	public GuiQuestBook(EntityPlayer player) {
		super();
		this.player = player;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		if(lastOpenQuest != null && Main.proxy.getGlobalQuestHandler().getQuest(lastOpenQuest) != null) {
			page = Main.proxy.getGlobalQuestHandler().getQuest(lastOpenQuest).getPage(); //the page of the quest could have changed since we last opened
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1f, 1f, 1f);
		this.drawWorldBackground(0);
		
		switch(page) {
			case -2:
				mc.getTextureManager().bindTexture(TWO_PAGES_TEXTURE);
				break;
			case -1:
				int texW = 146;
				int texH = 180;
				mc.getTextureManager().bindTexture(COVER_TEXTURE);
				//mc.getTextureManager().bindTexture(new ResourceLocation("unknown"));
				GlStateManager.scale(1.5, 1.5, 1);
				this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
				GlStateManager.scale(1, 1, 1);
				break;
			default:
				Assert(page > 0, "Illegal quest book page " + page);
				mc.getTextureManager().bindTexture(FULLSCREEN_TEXTURE);
				this.drawTexturedModalRect(146, 2, 0, 0, 192, 192);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void onGuiClosed(){
		if(page != -1) {
			player.playSound(((ClientProxy)Main.proxy).BOOK_CLOSE, 1, 1);
		}
		super.onGuiClosed();
	}
	
	private int middleWidth(int width) {
		return 0; 
	}
	
	private int middleHeight(int height) {
		return 0; 
	}
	
}
