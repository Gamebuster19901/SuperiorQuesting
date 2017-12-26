package com.gamebuster19901.superiorquesting.client.gui.book.button;

import com.gamebuster19901.superiorquesting.client.gui.book.GuiQuestBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageButton extends GuiButton{
	private final ResourceLocation image;
	
	public ImageButton(int buttonId, int x, int y, String buttonText, ResourceLocation image) {
		super(buttonId, x, y, 16, 16, buttonText);
		this.image = image;
	}
	
	public ResourceLocation getImage() {
		return image;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(image);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.x, this.y, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
			this.drawString(mc.fontRenderer, displayString, x, y, 0xffffff);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		}
	}
}
