package com.gamebuster19901.superiorquesting.client.gui.book.button;

import static com.gamebuster19901.superiorquesting.Main.MODID;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PageButton extends BookButtonLong{
	public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(MODID + ":textures/gui/book_two_sides.png");
	
	public boolean pressed = false;
	private UUID page;
	
	public PageButton(int buttonId, int x, int y) {
		super(buttonId, x, y, "");
	}
	
	
    /**
     * Draws this button to the screen.
     */
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            if(!pressed) {
            	this.drawTexturedModalRect(this.x, this.y, 49, 208 + (i - 1) * 16, this.width, this.height);
            }
            else {
            	this.drawTexturedModalRect(this.x, this.y, 49, 208 + 2 * 16, this.width, this.height);
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

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
            if(page != null) {
            	this.displayString = Main.proxy.getGlobalQuestHandler().getPage(page).getTitle();
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }
	
	@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY)) {
        	pressed = !pressed;
        	return true;
        }
        return false;
    }
	
	public void setPage(UUID id) {
		page = id;
	}
	
	public UUID getPage() {
		return page;
	}
	
}
