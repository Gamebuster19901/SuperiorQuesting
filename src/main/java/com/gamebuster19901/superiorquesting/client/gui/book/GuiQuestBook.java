package com.gamebuster19901.superiorquesting.client.gui.book;

import static com.gamebuster19901.superiorquesting.Main.MODID;
import static com.gamebuster19901.superiorquesting.client.gui.book.NavigationButton.Direction.LEFT;
import static com.gamebuster19901.superiorquesting.client.gui.book.NavigationButton.Direction.RIGHT;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.util.Circle;
import com.gamebuster19901.superiorquesting.client.util.Point;
import com.gamebuster19901.superiorquesting.client.util.Rectangle;
import com.gamebuster19901.superiorquesting.client.util.Shape;
import com.gamebuster19901.superiorquesting.client.util.Square;
import com.gamebuster19901.superiorquesting.client.util.TriangleDown;
import com.gamebuster19901.superiorquesting.client.util.TriangleLeft;
import com.gamebuster19901.superiorquesting.client.util.TriangleRight;
import com.gamebuster19901.superiorquesting.client.util.TriangleUp;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.IngameDebuggable;
import com.gamebuster19901.superiorquesting.common.questing.Page;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public final class GuiQuestBook extends GuiScreen implements Assertable, IngameDebuggable{
	public static final TextComponentTranslation ILLEGAL_STATE = new TextComponentTranslation("gui.book.illegalstate");
	public static final TextComponentTranslation INVALID_PAGE = new TextComponentTranslation("gui.book.invalidpage");
	public static final TextComponentTranslation INVALID_QUEST = new TextComponentTranslation("gui.book.invalidquest");
	public static final TextComponentTranslation NO_PAGES = new TextComponentTranslation("gui.book.nopages");
	public static final TextComponentTranslation NO_QUESTS = new TextComponentTranslation("gui.book.noquests");
	public static final ResourceLocation COVER_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_cover.png");
	public static final ResourceLocation FULLSCREEN_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_fullscreen.png");
	public static final ResourceLocation TWO_PAGES_TEXTURE = new ResourceLocation(MODID + ":textures/gui/book_two_sides.png");
	
	/*
 		*current values:
 			* 1 Any com.gamebuster19901.common.questing.Page
 			* 0 Cover
 			* -1 Main Menu
 	*/
	
	private static byte pageType = 0;
	private static UUID page;
	private static UUID quest;
	private static boolean editMode = false;
	private static long scroll = 0;

	
	private EntityPlayer player;
	private NavigationButton[] navButtons = new NavigationButton[] {
			addButton(new NavigationButton(buttonList.size(), LEFT)),
			addButton(new NavigationButton(buttonList.size(), RIGHT))
	};
	private GuiButton[] editButtons = new GuiButton[] {};
	private ArrayList<BookButtonLong> bookbuttons;

	
	public GuiQuestBook(EntityPlayer player) {
		super();
		this.player = player;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		open(page, quest);
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	//Good luck
	public void open(UUID p, UUID q) {
		debug(p);
		int i = buttonList.size(); //button id
		Point mid = new Point(width / 2, height / 2);
		
		if(p != null) {
			pageType = 1;
		}
		page = p;
		quest = q;
		
		if(pageType == 0) {
			navButtons[0].visible = false;
			navButtons[1].visible = true;
		}
		
		if (pageType == -1) {
			int horizontalAdjustment = 172;
			int verticalAdjustment = 102;
			
			int canScrollUp = 0;
			int canScrollDown = 0;
			
			navButtons[0].x = (int)mid.getX() -horizontalAdjustment + 67;
			navButtons[0].y = 285;
			navButtons[0].visible = true;
			navButtons[1].x = (int)mid.getX() + horizontalAdjustment - 85;
			navButtons[1].y = 285;
			navButtons[1].visible = true;
			
			if(canScrollUp == 1) {
				this.addButton(new BookButtonLong(i++, ((int)mid.getX()) - horizontalAdjustment, ((int)mid.getY()) - verticalAdjustment, ((char) (0x25b2)) + ""));
			}
			int pagelist = i + 12 - canScrollUp - canScrollDown;
			{
				int j = 0 + canScrollUp;
				for(; i < pagelist; i++, j++) {
					this.addButton(new PageButton(i - pagelist, (int)mid.getX() - horizontalAdjustment, (int)mid.getY() - verticalAdjustment + (j * (16 + 2)), (Page)null));
				}
				if(canScrollDown == 1) {
					this.addButton(new BookButtonLong(i, width / 2 - horizontalAdjustment, (height / 2 - verticalAdjustment) + (j * (16 + 2)), ((char) (0x25bc)) + ""));
				}
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1f, 1f, 1f);
		this.drawWorldBackground(0);
		final ArrayList<String> errors = new ArrayList<String>();
		Point mid = new Point(width / 2, height / 2);
		Point mouseLoc = new Point(mouseX, mouseY);
		//debug((mid.getX() - mouseLoc.getX() + "") + ", " + (mid.getY() - mouseLoc.getY() + ""));
		switch(pageType) {
			case -1:
				mc.getTextureManager().bindTexture(TWO_PAGES_TEXTURE);
				{
					GlStateManager.pushMatrix();
					GlStateManager.scale(1.5, 1.5, 1);
					
					int texW = 256;
					int texH = 196;
					
					this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, 0, texW, texH);
					
					texW = 102;
					texH = 9;
					
					GlStateManager.scale(1, 1, 1);
					GlStateManager.popMatrix();
				
				}
				break;
			case 0:
				mc.getTextureManager().bindTexture(COVER_TEXTURE);
				{
					int texW = 146;
					int texH = 180;
					
					GlStateManager.pushMatrix();
					GlStateManager.scale(1.5, 1.5, 1);
					this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
					GlStateManager.scale(1, 1, 1);
					GlStateManager.popMatrix();
				
				}
				break;
			case 1:
				mc.getTextureManager().bindTexture(FULLSCREEN_TEXTURE);
				{
					int texW = 256;
					int texH = 196;
					GlStateManager.pushMatrix();
					GlStateManager.scale(1.5, 1.5, 1);
					this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, -4, texW, texH + 4);
					GlStateManager.scale(1, 1, 1);
					GlStateManager.popMatrix();
					
					if(Main.proxy.getGlobalQuestHandler().getQuest(quest) == null) {
						errors.add(ILLEGAL_STATE.getFormattedText() + INVALID_QUEST.getFormattedText() + " (" + quest + ")");
					}
					
					this.renderMultipleLines(this.fontRenderer, width / 2 , height / 2, Color.red.getRGB(), errors.toArray());
				}
				break;
			default:
				Assert(pageType > 1 && pageType > -1, "Illegal quest book page " + pageType);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		debug(button);
		switch(pageType) {
			case 1:
				break;
			case 0:
				if(button instanceof NavigationButton) {
					NavigationButton navButton = (NavigationButton) button;
					Assert(navButton.getDirection() == RIGHT, "Impossible navigation button direction: " + navButton.getDirection());
					pageType = -1;
					open(null, null);
				}
				break;
			case -1:
				if(button instanceof NavigationButton) {
					NavigationButton navButton = (NavigationButton) button;
					if(navButton.getDirection() == LEFT) {
						pageType = 0;
						//open(null, null);
					}
				}
				break;
			default:
				Assert(pageType > 1 && pageType > -1, "Illegal quest book page " + pageType);
		}
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
		if(Debuggable.debug && this.isCtrlKeyDown() && keyCode != 29 && keyCode != 157) {
			if (keyCode == 82)
			{
				pageType = 0;
			}
			else if (keyCode == 79) {
				pageType = -1;
			}
			else if (keyCode == 80) {
				pageType = 1;
			}
			else if (keyCode == 81) {
				testShapes();
			}
		}
		debug(Minecraft.getMinecraft().player, keyCode + " " + typedChar + this.isCtrlKeyDown());
    }

	public void onGuiClosed(){
		if(pageType != 0) {
			player.playSound(((ClientProxy)Main.proxy).BOOK_CLOSE, 1, 1);
		}
		super.onGuiClosed();
	}
	
	private void renderMultipleLines(FontRenderer render, int x, int y, int rgb, Object... objects) {
		int i = 0;
		for(Object o : objects) {
			String m = o.toString();
			render.drawString(m, x - render.getStringWidth(m) / 2, (y - 16 / 2) + i * 16, rgb);
			i++;
		}
	}
	
	public static void testShapes() {
		Shape[] shapes = new Shape[] {
			new Rectangle(),
			new Square(),
			new Circle(),
			new TriangleUp(),
			new TriangleRight(),
			new TriangleLeft(),
			new TriangleDown()
		};
		for(Shape s : shapes) {
			Shape.printShape(s);
		}
	}
}