package com.gamebuster19901.superiorquesting.client.util;

public class Rectangle implements Shape{
	private Point origin;
	private int width;
	private int height;
	
	public Rectangle(int w, int h) {
		this(0,0,w,h);
	}
	
	public Rectangle(int x, int y, int w, int h) {
		this(new Point(x,y),w,h);
	}
	
	public Rectangle(Point topLeft, int width, int height) {
		origin = topLeft;
		this.width = width;
		this.height = height;
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	public final Point getTopLeft() {
		return origin;
	}
	
	public final Point getCenter() {
		return new Point(origin.getX() + width / 2, origin.getY() + height / 2);
	}
	
	public final Point getBottomRight() {
		return new Point(origin.getX() + width, origin.getY() + height);
	}

	@Override
	public final Rectangle getBounds() {
		return this;
	}

	@Override
	public final boolean contains(Point p) {
		return p.getX() >= origin.getX() || p.getX() <= this.getBottomRight().getX() || p.getY() >= origin.getY() || p.getY() <= this.getBottomRight().getY();
	}

	@Override
	public final void shift(int shiftX, int shiftY) {
		origin.shift(shiftX, shiftY);
	}

	@Override
	public final Point getOrigin() {
		return origin;
	}

	@Override
	public boolean[][] toArray() {
		boolean[][] b = new boolean[height][width];
		
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[0].length; j++) {
				b[i][j] = true;
			}
		}
		
		return b;
	}
}
