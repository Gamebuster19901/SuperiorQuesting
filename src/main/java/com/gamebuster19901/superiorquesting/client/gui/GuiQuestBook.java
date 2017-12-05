package com.gamebuster19901.superiorquesting.client.gui;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.io.IOException;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.IngameDebuggable;
import com.gamebuster19901.superiorquesting.common.questing.Page;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public final class GuiQuestBook extends GuiScreen implements Assertable, IngameDebuggable{
	public static ResourceLocation COVER_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_cover.png");
	public static ResourceLocation FULLSCREEN_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_fullscreen.png");
	public static ResourceLocation TWO_PAGES_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_two_sides.png");
	
	/*
 		*current values:
 			* 1 Any com.gamebuster19901.common.questing.Page
 			* 0 Cover
 			* -1 Main Menu
 	*/
	private static byte pageType = 0;
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
			pageType = 1;
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void open(Page p) {
		pageType = 1;
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1f, 1f, 1f);
		this.drawWorldBackground(0);
		
		switch(pageType) {
			case -1:
				mc.getTextureManager().bindTexture(TWO_PAGES_TEXTURE);
				{
				int texW = 256;
				int texH = 196;
				GlStateManager.scale(1.5, 1.5, 1);
				this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
				GlStateManager.scale(1, 1, 1);
				}
				break;
			case 0:
				mc.getTextureManager().bindTexture(COVER_TEXTURE);
				{
				int texW = 146;
				int texH = 180;
				GlStateManager.scale(1.5, 1.5, 1);
				this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
				GlStateManager.scale(1, 1, 1);
				}
				break;
			case 1:
				int texW = 256;
				int texH = 196;
				mc.getTextureManager().bindTexture(FULLSCREEN_TEXTURE);
				GlStateManager.scale(1.5, 1.5, 1);
				this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
				GlStateManager.scale(1, 1, 1);
			default:
				Assert(pageType > 1 && pageType > -1, "Illegal quest book page " + pageType);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
		if(Debuggable.debug && this.isCtrlKeyDown() && keyCode != 29 && keyCode != 157) {
			if (keyCode == 79)
			{
				pageType = -1;
			}
			else if (keyCode == 80) {
				pageType = 0;
			}
		}
		debug(Minecraft.getMinecraft().player, keyCode + " " + typedChar + this.isCtrlKeyDown());
    }
	
	private native boolean isScrollLockOn();

	public void onGuiClosed(){
		if(pageType != 0) {
			player.playSound(((ClientProxy)Main.proxy).BOOK_CLOSE, 1, 1);
		}
		super.onGuiClosed();
	}	
}