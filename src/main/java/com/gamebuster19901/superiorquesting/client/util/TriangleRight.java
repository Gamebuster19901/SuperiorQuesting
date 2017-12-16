package com.gamebuster19901.superiorquesting.client.util;

public class TriangleRight extends Triangular{
	private int size;
	
	public TriangleRight() {
		this(16);
	}
	
	public TriangleRight(int size) {
		this(new Point(0,0),size);
	}
	
	public TriangleRight(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleRight(Point p, int size) {
		super(p, new Point(0, 0), new Point(size, 0), new Point(size / 2, size));
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
