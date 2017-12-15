package com.gamebuster19901.superiorquesting.client.util;

public abstract class Triangular{
	
	protected Point a;
	protected Point b;
	protected Point c;
	
	Triangular (Point a, Point b, Point c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	protected void shift(int shiftX, int shiftY) {
		a.shift(shiftX, shiftY);
		b.shift(shiftX, shiftY);
		c.shift(shiftX, shiftY);
	}
	
	public final int getArea() {
		return getArea(a,b,c);
	}
	
	public final int getArea(Point a, Point b, Point c) {
		return getArea(a.getX(), b.getX(), c.getX(), a.getY(), b.getY(), c.getY());
	}
	
	public final int getArea(int ax, int bx, int cx, int ay, int by, int cy) {
		return Math.abs(ax * (by - cy) + bx * (cy - ay) + cx * (ay - by) / 2);
	}

	public final boolean contains(Point p) {
		return(getArea(p,a,b) + getArea(p,b,c) + getArea(p,a,c) == this.getArea());
	}
}
