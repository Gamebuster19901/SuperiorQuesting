package com.gamebuster19901.superiorquesting.client.shape;

import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;

import net.minecraft.nbt.NBTTagCompound;

public interface Shape extends UpdatableSerializable{
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
	
	public static void printShape(Shape s) {
		boolean[][] arr = s.toArray();
		System.out.println(s.getClass());
		
		String ln = "";
		
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                System.out.print(arr[i][j] ? "TT" : "FF");
            }
            System.out.print('\n');
        }
	}
	
	public static Class<? extends Shape> getShapeClassFromNBT(NBTTagCompound data){
		try {
			Class<? extends Shape> clazz;
			if(data.getString("CLASS").equals("")) {
				throw new NullPointerException();
			}
			clazz = (Class<? extends Shape>) Class.forName(data.getString("CLASS"));
			return clazz;
		}
		catch(ClassNotFoundException | NullPointerException e) {
			NoClassDefFoundError e1 = new NoClassDefFoundError(e.getMessage());
			e1.initCause(e);
			throw e1;
		}
	}
}
