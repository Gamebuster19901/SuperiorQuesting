package com.gamebuster19901.superiorquesting.client.util;

public class TriangleRight extends Triangular implements Shape{
	private Point origin;
	private int size;
	
	public TriangleRight(int size) {
		this(0,0,size);
	}
	
	public TriangleRight(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleRight(Point p, int size) {
		super(new Point(0, size), new Point(size / 2, 0), new Point(size, size));
		origin = p;
		this.size = size;
	}

	@Override
	public Rectangle getBounds() {
		return new Square(origin, size);
	}

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public void shift(int shiftX, int shiftY) {
		super.shift(shiftX, shiftY);
		origin.shift(shiftX, shiftY);
	}
	
	public void moveTo(Point p) {
		this.shift(p.getX() - origin.getX(), p.getY() - origin.getY());
	}
	
	public void moveTo(int x, int y) {
		this.moveTo(new Point(x, y));
	}
}
