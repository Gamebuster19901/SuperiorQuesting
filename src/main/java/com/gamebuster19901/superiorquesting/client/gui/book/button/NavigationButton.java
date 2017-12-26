package com.gamebuster19901.superiorquesting.client.gui.book.button;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.client.gui.book.GuiQuestBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class NavigationButton extends GuiButton{
	private UUID page;
	private UUID unique;
	private Direction d;
	
	public NavigationButton(int buttonId, Direction d) {
		this(buttonId, 0, 0, d);
	}

	public NavigationButton(int buttonId, int x, int y, Direction d) {
		super(buttonId, x, y, "");
		this.d = d;
		super.width = 18;
		super.height = 10;
	}
	
	public NavigationButton(int buttonId, int x, int y, Direction d, UUID page) {
		this(buttonId, x, y, d);
		this.page = page;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiQuestBook.TWO_PAGES_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            int j = d == Direction.RIGHT ? 0 : 1;
            this.drawTexturedModalRect(this.x, this.y, 3 * i + ((i - 1) * width), 208 + j * 3 + j * height, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int k = 14737632;
        }
    }
	
	public Direction getDirection() {
		return d;
	}
	
	public enum Direction {
		LEFT,
		RIGHT;
	}
	
}
