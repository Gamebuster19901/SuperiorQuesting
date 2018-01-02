package com.gamebuster19901.superiorquesting.client.gui.book;

import static com.gamebuster19901.superiorquesting.Main.MODID;
import static com.gamebuster19901.superiorquesting.client.gui.book.button.NavigationButton.Direction.LEFT;
import static com.gamebuster19901.superiorquesting.client.gui.book.button.NavigationButton.Direction.RIGHT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.book.button.BookButtonLong;
import com.gamebuster19901.superiorquesting.client.gui.book.button.EditButton;
import com.gamebuster19901.superiorquesting.client.gui.book.button.NavigationButton;
import com.gamebuster19901.superiorquesting.client.gui.book.button.PageButton;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.IngameDebuggable;
import com.gamebuster19901.superiorquesting.common.item.ItemQuestBook;
import com.gamebuster19901.superiorquesting.common.questing.Page;
import com.gamebuster19901.superiorquesting.common.shape.Circle;
import com.gamebuster19901.superiorquesting.common.shape.Point;
import com.gamebuster19901.superiorquesting.common.shape.Rectangle;
import com.gamebuster19901.superiorquesting.common.shape.RegularGon;
import com.gamebuster19901.superiorquesting.common.shape.Shape;
import com.gamebuster19901.superiorquesting.common.shape.Square;
import com.gamebuster19901.superiorquesting.common.shape.TriangleDown;
import com.gamebuster19901.superiorquesting.common.shape.TriangleLeft;
import com.gamebuster19901.superiorquesting.common.shape.TriangleRight;
import com.gamebuster19901.superiorquesting.common.shape.TriangleUp;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
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
 			* 0 Cover
 			* -1 Page Selection/Main menu
 			* -2 Quest Selection
 			* -3 Quest Page
 	*/
	
	private static byte pageType = 0;
	private static UUID page;
	private static UUID quest;
	private static int scroll = 0;
	private static ResourceLocation texture;
	
	
	private EntityPlayer player;
	private boolean editMode;
	private NavigationButton[] navButtons = new NavigationButton[] {
			addButton(new NavigationButton(buttonList.size(), LEFT)),
			addButton(new NavigationButton(buttonList.size(), RIGHT))
	};
	private EditButton[] editButtons = new EditButton[3];
	private BookButtonLong[] longNavigationButtons = new BookButtonLong[2];
	private PageButton[] pageButtons = new PageButton[12];
	private Point mid;

	
	public GuiQuestBook(EntityPlayer player, boolean editMode) {
		super();
		this.player = player;
		this.editMode = editMode;
	}
	
	@Override
	public void initGui() { //Good luck
		super.initGui();
		this.addButton(navButtons[0]);
		this.addButton(navButtons[1]);
		
		int i = buttonList.size(); //button id
		
		mid = new Point(width / 2, height / 2);
		
		int canScrollUp = 0;
		int canScrollDown = 0;
		
		int horizontalAdjustment = 172;
		int verticalAdjustment = 102;
		
		for(int j = 0; j < 12; i++, j++) {
			pageButtons[j] = (this.addButton(new PageButton(i , (int)mid.getX() - horizontalAdjustment, (int)mid.getY() - verticalAdjustment + (j * (16 + 2)))));
		}
		longNavigationButtons[0] = this.addButton(new BookButtonLong(i++, ((int)mid.getX()) - horizontalAdjustment, ((int)mid.getY()) - verticalAdjustment, ((char) (0x25b2)) + ""));
		longNavigationButtons[1] = this.addButton(new BookButtonLong(i++, width / 2 - horizontalAdjustment, (height / 2 - verticalAdjustment) + (i++ * (16 + 2)), ((char) (0x25bc)) + ""));
		editButtons[0] = this.addButton(new EditButton(i++, 0, 0, 0, 240)); //add
		editButtons[1] = this.addButton(new EditButton(i++, 0, 0, 16, 240)); //edit
		editButtons[2] = this.addButton(new EditButton(i++, 0, 0, 32, 240)); //remove
		open(page, quest, true);
		
	}
	
	/* button list visibility logic (old version)
 		Point mid = new Point(width / 2, height / 2);
		
		int canScrollUp = 0;
		int canScrollDown = 0;
		
		int horizontalAdjustment = 172;
		int verticalAdjustment = 102;
		
 		if(canScrollUp == 1) {
			longNavigationButtons[0] = this.addButton(new BookButtonLong(i++, ((int)mid.getX()) - horizontalAdjustment, ((int)mid.getY()) - verticalAdjustment, ((char) (0x25b2)) + ""));
		}
		int pagelist = i + 12 - canScrollUp - canScrollDown;
		{
			int j = 0 + canScrollUp;
			for(; i < pagelist; i++, j++) {
				this.addButton(new PageButton(i - pagelist, (int)mid.getX() - horizontalAdjustment, (int)mid.getY() - verticalAdjustment + (j * (16 + 2)), (Page)null));
			}
			if(canScrollDown == 1) {
				longNavigationButtons[1] = this.addButton(new BookButtonLong(i, width / 2 - horizontalAdjustment, (height / 2 - verticalAdjustment) + (j * (16 + 2)), ((char) (0x25bc)) + ""));
			}
		}
	 */
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	//Good luck
	public void open(UUID p, UUID q, boolean init) {
		setLongButtonVisibility(false);
		debug(p);
		
		page = p;
		quest = q;
		
		if(pageType == 1) {
			texture = FULLSCREEN_TEXTURE;
			int horizontalAdjustment = 225;
			navButtons[0].visible = true;
			navButtons[1].visible = false;
			navButtons[0].x = (int)mid.getX() -horizontalAdjustment + 67;
			navButtons[0].y = 285;
			navButtons[0].visible = true;
		}
		
		if(pageType == 0) {
			texture = COVER_TEXTURE;
			navButtons[0].visible = false;
			navButtons[1].visible = true;
			navButtons[1].x = mid.getX() - 9;
			navButtons[1].y = height - 50;
		}
		
		if (pageType == -1) {
			texture = TWO_PAGES_TEXTURE;
			int horizontalAdjustment = 172;
			int verticalAdjustment = 102;
			
			navButtons[0].x = (int)mid.getX() -horizontalAdjustment + 67;
			navButtons[0].y = 285;
			navButtons[0].visible = true;
			navButtons[1].x = (int)mid.getX() + horizontalAdjustment - 85;
			navButtons[1].y = 285;
			
			for(int i = 0; i < 3; i++) {
				editButtons[i].x = (int)mid.getX() + (80 * i) - 184;
				editButtons[i].y = (int)mid.getY() - 139;
			}
			
			setLongButtonVisibility(true);
			

		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		debug(pageType);
		debug(editMode);
		GlStateManager.color(1f, 1f, 1f);
		this.drawWorldBackground(0);
		final ArrayList<String> errors = new ArrayList<String>();
		Point mouseLoc = new Point(mouseX, mouseY);
		//debug((mid.getX() - mouseLoc.getX() + "") + ", " + (mid.getY() - mouseLoc.getY() + ""));
		switch(pageType) {
			case -1:
				mc.getTextureManager().bindTexture(texture);
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
					
					this.navButtons[1].visible = page != null;
					
					for(PageButton b : this.pageButtons) {
						if(b.getPage() == page) {
							b.pressed = true;
							if(page != null) {
								Page p = Main.proxy.getGlobalQuestHandler().getPage(page);
								if(Main.proxy.getGlobalQuestHandler().getPage(page).getDescription() != null) {
									fontRenderer.drawSplitString(Main.proxy.getGlobalQuestHandler().getPage(page).getDescription(), mid.getX() + 27, mid.getY() - 101, 139, 0x000000);
								}
							}
						}
						else {
							b.pressed = false;
						}
					}
					for(EditButton b : editButtons) {
						b.visible = editMode;
					}
				
				}
				break;
			case 0:
				mc.getTextureManager().bindTexture(texture);
				{
					int texW = 146;
					int texH = 180;
					
					GlStateManager.pushMatrix();
					GlStateManager.scale(1.5, 1.5, 1);
					this.drawTexturedModalRect(width / 3 - texW / 2, height / 3 - texH / 2, 0, 0, texW, texH);
					GlStateManager.scale(1, 1, 1);
					GlStateManager.popMatrix();
					
					for(EditButton b : editButtons) {
						b.visible = false;
					}
				
				}
				break;
			case 1:
				mc.getTextureManager().bindTexture(texture);
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
				if(button instanceof NavigationButton) {
					NavigationButton navButton = (NavigationButton) button;
					Assert(navButton.getDirection() == LEFT, "Impossible navigation button direction: " + navButton.getDirection());
					pageType = -1;
					open(page, null, false);
				}
				break;
			case 0:
				if(button instanceof NavigationButton) {
					NavigationButton navButton = (NavigationButton) button;
					Assert(navButton.getDirection() == RIGHT, "Impossible navigation button direction: " + navButton.getDirection());
					pageType = -1;
					open(null, null, false);
				}
				break;
			case -1:
				if(button instanceof NavigationButton) {
					NavigationButton navButton = (NavigationButton) button;
					if(navButton.getDirection() == LEFT) {
						pageType = 0;
						open(null, null, false);
					}
				}
				else if (button instanceof PageButton) {
					PageButton pageButton = (PageButton)button;
					if(this.page != pageButton.getPage()) {
						this.page = pageButton.getPage();
					}
					else {
						this.page = null;
					}
				}
				else if (button instanceof BookButtonLong) {
					BookButtonLong bookButton = (BookButtonLong)button;
					if(button == longNavigationButtons[0]) {
						
					}
					else if (button == longNavigationButtons[1]) {
						
					}
					else {
						Assert(false, "Unknown button " + button);
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
	
	public void setLongButtonVisibility(boolean state) {
		int pageCount = Main.proxy.getGlobalQuestHandler().getAllPages().size();
		int canScrollUp = scroll > 0 ? 1 : 0;
		int canScrollDown = scroll + 11 >= pageCount ? 1 : 0;
		for(PageButton b : pageButtons) {
			b.visible = false;
		}
		longNavigationButtons[0].visible = canScrollUp == 1 ? true : false;
		longNavigationButtons[1].visible = canScrollDown == 1 ? true : false;
		if(state) {
			for(int i = scroll + canScrollUp; i < scroll + 11 && i < pageCount; i++) {
				Page p = Main.proxy.getGlobalQuestHandler().getAllPages().get(i);
				pageButtons[i].setPage(p.getUUID());
				debug(pageButtons[i].visible = p.getUUID() != null);
			}
		}
	}

	public void onGuiClosed(){
		if(pageType != 0) {
			player.playSound(((ClientProxy)Main.proxy).BOOK_CLOSE, 1, 1);
		}
		super.onGuiClosed();
	}
	
	public static void testShapes() {
		Shape[] shapes = new Shape[] {
			new Rectangle(),
			new Square(),
			new Circle(),
			new TriangleUp(),
			new TriangleRight(),
			new TriangleLeft(),
			new TriangleDown(),
			new RegularGon(5,180, new Square())
		};
		for(Shape s : shapes) {
			Shape.printShape(s);
		}
		for(Point p : ((RegularGon)shapes[7]).getPoints()) {
			System.out.println(p.getX() + ", " + p.getY());
		}
		
		System.out.println(new Square().getCenter().getX());
		System.out.println(new Square().getCenter().getY());
		
	}
	
	public static ResourceLocation getImage() {
		return texture;
	}
	
	private void drawPixel(Point p, int color) {
		drawVerticalLine(p.getX(), p.getY(), p.getY(), color);
	}
	
	public static void clean() {
		pageType = 0;
		page = null;
		quest = null;
		scroll = 0;
		texture = null;
	}
}