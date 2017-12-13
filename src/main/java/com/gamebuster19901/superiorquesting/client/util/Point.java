package com.gamebuster19901.superiorquesting.client.util;

public class Point {
	private final double x;
	private final double y;
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getX(){
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double distanceTo(Point p) {
		final double x = getX() - p.getX();
		final double y = getY() - p.getY();
		return Math.sqrt((x * x) + (y * y));
	}
	
	@Override
	public String toString() {
		return "point (" + x + ',' + y + ')';
	}
}
