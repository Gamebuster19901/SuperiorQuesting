package com.gamebuster19901.superiorquesting.client.util;

public class TriangleLeft extends Triangular{
	private int size;
	
	public TriangleLeft() {
		this(16);
	}
	
	public TriangleLeft(int size) {
		this(new Point(0,0),size);
	}
	
	public TriangleLeft(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleLeft(Point p, int size) {
		super(p, new Point(0, size), new Point(size / 2, -1), new Point(size, size));
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
