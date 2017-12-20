package com.gamebuster19901.superiorquesting.client.util;

import java.awt.Polygon;

public class RegularGon implements Shape{
	private Point[] points;
	private Rectangle bounds;
	
	public RegularGon(int verticies, int angle, int width) {
		this(verticies, angle, new Square(width));
	}
	
	public RegularGon(int verticies, int angle, int x, int y, int width) {
		this(verticies, angle, new Square(x, y, width));
	}
	
	public RegularGon(int verticies, double angle, Square square) {
		if(verticies < 5) {
			throw new IllegalArgumentException ("Must have at least 5 verticies");
		}
		points = new Point[verticies];
		bounds = square;
		int radius = square.getWidth() / 2;
		
		double angleStep = (Math.PI * 2) / verticies;
		double x = 0;
		double y = 0;
		for(int i = 0; i < verticies; i++) {
			 x = square.getCenter().getX() + radius * Math.cos(angle);
			 y = square.getCenter().getY() + radius * Math.sin(angle);
			 angle += angleStep;
			 points[i] = new Point((int)x,(int)y);
		}
	}
	
	public final Point[] getPoints() {
		return points;
	}
	
	public final int getGonType() {
		return points.length;
	}

	@Override
	public final Rectangle getBounds() {
		return bounds;
	}

	@Override
	public final Point getOrigin() {
		return bounds.getOrigin();
	}

	@Override
	public final void shift(int shiftX, int shiftY) {
		bounds.shift(shiftX, shiftY);
		for(Point p : points) {
			p.shift(shiftX, shiftY);
		}
	}

	@Override
	public final boolean contains(Point p) {
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		for(int i = 0; i < points.length; i++) {
			x[i] = points[i].getX();
			y[i] = points[i].getY();
		}
		return new Polygon(x,y,points.length).contains(p.getX(), p.getY());
	}
}
