package com.gamebuster19901.superiorquesting.client.util;

public class Square extends Rectangle{
	
	public Square(int size) {
		this(0,0,size);
	}
	
	public Square(int x, int y, int size) {
		this(new Point(x, y), size);
	}
	
	public Square(Point p, int size) {
		super(p, size, size);
	}
	
	public static boolean isSquare(Shape s) {
		if(s instanceof Rectangle) {
			if(s instanceof Square) {
				return true;
			}
			Rectangle r = (Rectangle) s;
			return r.getWidth() == r.getHeight();
		}
		return false;
	}
}
