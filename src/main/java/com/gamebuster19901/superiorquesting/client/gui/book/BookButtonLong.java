package com.gamebuster19901.superiorquesting.client.gui.book;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.awt.Color;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.Page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BookButtonLong extends GuiButton{
	public static final int width = 102;
	public static final int height = 9;
	public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(MODID + ":textures/gui/book_two_sides.png");
	
	public BookButtonLong(int buttonId, int x, int y) {
		this(buttonId, x, y, "");
	}
	
	public BookButtonLong(int buttonId, int x, int y, String text) {
		super(buttonId, x, y, text);
	}
	
    /**
     * Draws this button to the screen.
     */
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
        	//mouseX = (int)(mouseX * 1.5);
        	//mouseY = (int)(mouseY * 1.5);
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.5, 1.5, 1);
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x * 1.5 && mouseY >= this.y * 1.5 && mouseX < this.x * 1.5 + this.width * 1.5 && mouseY < this.y * 1.5 + this.height * 1.5;
            int i = this.getHoverState(this.hovered);
            this.drawTexturedModalRect(this.x, this.y, 49, 210 + (i - 1) * 13, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = Color.GRAY.getRGB();

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }
	
	@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.enabled && this.visible &&mouseX >= this.x * 1.5 && mouseY >= this.y * 1.5 && mouseX < this.x * 1.5 + this.width * 1.5 && mouseY < this.y * 1.5 + this.height * 1.5) {
        	return true;
        }
        return false;
    }
	

}
