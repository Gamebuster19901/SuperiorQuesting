package com.gamebuster19901.superiorquesting.client.util;

public class TriangleUp extends Triangular{
	private Point origin;
	private int size;
	
	public TriangleUp(int size) {
		this(new Point(0,0),size);
	}
	
	public TriangleUp(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleUp(Point p, int size) {
		super(p, new Point(size, 0), new Point(0, size / 2), new Point(size, size));
		origin = p;
		this.size = size;
	}

	@Override
	public Rectangle getBounds() {
		return new Square(origin, size + 1);
	}

	@Override
	public Point getOrigin() {
		return origin;
	}
	
	public void moveTo(Point p) {
		this.shift(p.getX() - origin.getX(), p.getY() - origin.getY());
	}
	
	public void moveTo(int x, int y) {
		this.moveTo(new Point(x, y));
	}
}
