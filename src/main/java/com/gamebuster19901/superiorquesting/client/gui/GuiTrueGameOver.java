package com.gamebuster19901.superiorquesting.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

public class GuiTrueGameOver extends GuiGameOver{

	private int enableButtonsTimer;

	public GuiTrueGameOver(ITextComponent causeOfDeath) {
		super(causeOfDeath);
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
		switch (button.id)
		{
			case 0:
				this.mc.player.respawnPlayer();
				this.mc.displayGuiScreen((GuiScreen)null);
				break;
			case 1:
				this.mc.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (this.mc.world != null)
            {
                this.mc.world.sendQuittingDisconnectingPacket();
            }

            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        else
        {
            this.mc.player.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
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
}
