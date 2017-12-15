package com.gamebuster19901.superiorquesting.client.gui.book;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.client.gui.GuiButton;

public class QuestButton extends GuiButton implements Shape{
	private UUID quest;
	private int importance = Main.proxy.getGlobalQuestHandler().getQuest(quest).getImportance();
	private Shape s;
	public QuestButton(int buttonId, int x, int y, UUID Quest) {
		super(buttonId, x, y, "");
		//questbuttons can be in different shapes, width and height are useless in this context
		this.width = -1;
		this.height = -1;
	}
	
	@Override
	public Rectangle getBounds() {
		return s.getBounds();
	}
	
	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		// TODO Auto-generated method stub
		return null;
	}
}
