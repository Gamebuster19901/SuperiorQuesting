package com.gamebuster19901.superiorquesting.client.util;

import java.util.Arrays;

public interface Shape {
	public Rectangle getBounds();
	
	public Point getOrigin();
	
	public boolean contains(Point p);
	
	public default boolean intersects(Shape otherShape) {
		return doShapesIntersect(this, otherShape);
	}

	public void shift(int shiftX, int shiftY);
	
	public default boolean[][] toArray(){
		boolean[][] arr = new boolean[getBounds().getWidth()][getBounds().getHeight()];
		
		for(int x = 0; x < arr.length; x++) {
			for(int y = 0; y < arr[0].length; y++) {
				arr[x][y] = contains(new Point(x,y));
			}
		}
		
		return arr;
	}
	
	public default void moveTo(Point p) {
		getOrigin().moveTo(p);
	}
	
	public default void moveTo(int x, int y) {
		getOrigin().moveTo(new Point(x,y));
	}
	
	public static boolean doShapesIntersect(Shape shape1, Shape shape2) {
		Shape topLeftMostShape;
		Shape bottomRightMostShape;
		
		if(shape1.getBounds().getOrigin().getY() < shape2.getBounds().getOrigin().getY()) {
			topLeftMostShape = shape1;
			bottomRightMostShape = shape2;
		}
		else if(shape1.getBounds().getOrigin().getX() < shape2.getBounds().getOrigin().getX()) {
			topLeftMostShape = shape1;
			bottomRightMostShape = shape2;
		}
		else {
			topLeftMostShape = shape2;
			bottomRightMostShape = shape1;
		}
		
		int width = bottomRightMostShape.getBounds().getBottomRight().getX();
		int height = bottomRightMostShape.getBounds().getBottomRight().getY();
		
		boolean[][] pasteMap = new boolean[width][height];
		
		Point p = new Point();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				pasteMap[x][y] = (topLeftMostShape.contains(new Point(x,y)) || bottomRightMostShape.contains(new Point(x,y)));
			}
		}
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(pasteMap[x][y] = pasteMap[x][y] ^ (topLeftMostShape.contains(new Point(x,y)) || bottomRightMostShape.contains(new Point(x,y)))) {
					return false;
				}
			}
		}
		
		return true;
	}
}
