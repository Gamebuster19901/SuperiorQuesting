package com.gamebuster19901.superiorquesting.client.util;

public class Circle implements Shape{
	private Point origin;
	private int diameter;
	
	public Circle(int d) {
		this(0,0,d);
	}
	
	public Circle(int x, int y, int d) {
		this(new Point(x,y),d);
	}
	
	public Circle(Point p, int d) {
		origin = p;
		diameter = d;
	}

	@Override
	public Rectangle getBounds() {
		return new Square(origin, diameter);
	}

	@Override
	public Point getOrigin() {
		return origin;
	}
	
	public Point getCenter() {
		return getBounds().getCenter();
	}

	@Override
	public boolean contains(Point p) {
		int x = getCenter().getX();
		int y = getCenter().getY();
		
		int xSquared = (x - p.getX())*(x - p.getX());
		int ySquared = (y - p.getY())*(y - p.getY());
		
		return diameter / 2 > (int)Math.sqrt(xSquared + ySquared);
	}

	@Override
	public void shift(int shiftX, int shiftY) {
		origin.shift(shiftX, shiftY);
	}
}
