package com.gamebuster19901.superiorquesting.client.util;

public class TriangleDown extends Triangular{
	private int size;
	
	public TriangleDown() {
		this(16);
	}
	
	public TriangleDown(int size) {
		this(new Point(0,0),size);
	}
	
	public TriangleDown(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleDown(Point p, int size) {
		super(p, new Point(0, size), new Point(size, size / 2), new Point(0,0));
		origin = p;
		this.size = size;
	}

	@Override
	public Rectangle getBounds() {
		return new Square(origin, size + 1);
	}
	
	public void moveTo(Point p) {
		this.shift(p.getX() - origin.getX(), p.getY() - origin.getY());
	}
	
	public void moveTo(int x, int y) {
		this.moveTo(new Point(x, y));
	}
}
