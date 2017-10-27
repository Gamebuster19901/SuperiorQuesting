package com.gamebuster19901.superiorquesting.client.gui;

import java.io.IOException;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiTrueGameOver extends GuiGameOver implements Assertable{

	private int enableButtonsTimer;
	public static ITextComponent deathCause;

	public GuiTrueGameOver(ITextComponent causeOfDeath) {
		super(causeOfDeath);
		deathCause = causeOfDeath;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.enableButtonsTimer = 0;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))));
		for (GuiButton guibutton : this.buttonList)
		{
			guibutton.enabled = false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		switch (button.id){
			case 0:
				this.mc.player.respawnPlayer();
				this.mc.displayGuiScreen((GuiScreen)null);
				break;
			case 1:
				if(this.mc.isSingleplayer()) {
					String worldName = DimensionManager.getCurrentSaveRootDirectory().getName();
					this.mc.world.sendQuittingDisconnectingPacket();
					this.mc.loadWorld((WorldClient)null);
					this.mc.displayGuiScreen(new GuiMainMenu());
					this.mc.getSaveLoader().deleteWorldDirectory(worldName);
				}
				else {
					this.mc.world.sendQuittingDisconnectingPacket();
					this.mc.loadWorld((WorldClient)null);
					this.mc.displayGuiScreen(new GuiMainMenu());
				}
		}
	}
	
	@Override
	public void confirmClicked(boolean result, int id)
	{
		throw new AssertionError();
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		++this.enableButtonsTimer;

		if (this.enableButtonsTimer == 20)
		{
			for (GuiButton guibutton : this.buttonList)
			{
				guibutton.enabled = true;
			}
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		boolean flag = true;
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		this.drawCenteredString(this.fontRenderer, I18n.format("deathScreen.title.hardcore"), this.width / 2 / 2, 30, 16777215);
		GlStateManager.popMatrix();

		if (deathCause != null)
		{
			this.drawCenteredString(this.fontRenderer, deathCause.getFormattedText(), this.width / 2, 85, 16777215);
		}

		this.drawCenteredString(this.fontRenderer, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.mc.player.getScore(), this.width / 2, 100, 16777215);

		if (deathCause != null && mouseY > 85 && mouseY < 85 + this.fontRenderer.FONT_HEIGHT)
		{
			ITextComponent itextcomponent = this.getClickedComponentAt(mouseX);

			if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null)
			{
				this.handleComponentHover(itextcomponent, mouseX, mouseY);
			}
		}
		
		for(GuiButton b : buttonList) {
			b.drawButton(mc, mouseX, mouseY, partialTicks);
		}
	}
	
	@SubscribeEvent
	public void GuiOpenEvent(GuiOpenEvent e) {
		if(e.getGui() != null && e.getGui().getClass() == GuiGameOver.class) {
			if(Main.proxy.getLifeHandler().getLives(Minecraft.getMinecraft().player) <= 0) {
				e.setCanceled(true);
				Minecraft.getMinecraft().player.openGui(Main.getInstance(), GuiHandler.FINAL_DEATH, Minecraft.getMinecraft().player.world, (int)Minecraft.getMinecraft().player.posX, (int)Minecraft.getMinecraft().player.posY, (int)Minecraft.getMinecraft().player.posZ);
			}
		}
	}
}
