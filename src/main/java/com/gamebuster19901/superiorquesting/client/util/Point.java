package com.gamebuster19901.superiorquesting.client.util;

public class Point implements Shape{
	private int x;
	private int y;
	
	public Point() {
		this(0,0);
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Point getOrigin() {
		return this;
	}

	public int getX(){
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int distanceTo(Point p) {
		final int x = getX() - p.getX();
		final int y = getY() - p.getY();
		return (int) Math.sqrt((x * x) + (y * y));
	}
	
	@Override
	public String toString() {
		return "point (" + x + ',' + y + ')';
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(this,1,1);
	}

	@Override
	public boolean contains(Point p) {
		return x == p.getX() && y == p.getY();
	}

	@Override
	public boolean intersects(Shape otherShape) {
		return otherShape.contains(this);
	}

	@Override
	public void shift(int shiftX, int shiftY) {
		x = x + shiftX;
		y = y + shiftY;
	}

	@Override
	public void moveTo(Point p) {
		x = p.getX();
		y = p.getY();
	}

	@Override
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean[][] toArray() {
		return new boolean[1][1];
	}
}
