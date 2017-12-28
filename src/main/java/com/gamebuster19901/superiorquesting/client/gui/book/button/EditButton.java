package com.gamebuster19901.superiorquesting.client.gui.book.button;

import com.gamebuster19901.superiorquesting.client.gui.book.GuiQuestBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class EditButton extends GuiButton{
	private final int texturex;
	private final int texturey;
	
	public EditButton(int buttonId, int x, int y, int texturex, int texturey) {
		this(buttonId, x, y, texturex, texturey, "");
	}
	
	public EditButton(int buttonId, int x, int y, int texturex, int texturey, String buttonText) {
		super(buttonId, x, y, 16, 16, buttonText);
		this.texturex = texturex;
		this.texturey = texturey;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(GuiQuestBook.getImage());
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			GlStateManager.scale(1f, 1f, 1);
			this.drawTexturedModalRect(this.x, this.y, texturex, texturey, 16, 16);
			GlStateManager.scale(1, 1, 1);
			GlStateManager.popMatrix();
			this.drawString(mc.fontRenderer, displayString, x, y, 0xffffff);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		}
	}
}
