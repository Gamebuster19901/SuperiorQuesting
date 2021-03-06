package com.gamebuster19901.superiorquesting.common.shape;

import java.awt.Polygon;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Triangular implements Shape{
	protected Point origin;
	protected Point a;
	protected Point b;
	protected Point c;
	
	protected Triangular(NBTTagCompound nbt){
		deserializeNBT(nbt);
	}
	
	protected Triangular (Point o, Point a, Point b, Point c){
		this.origin = o;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public final void shift(int shiftX, int shiftY) {
		origin.shift(shiftX, shiftY);
		a.shift(shiftX, shiftY);
		b.shift(shiftX, shiftY);
		c.shift(shiftX, shiftY);
	}
	
	@Override
	public final Point getOrigin() {
		return origin;
	}
	
	public final int getArea() {
		return getArea(a,b,c);
	}
	
	public final int getArea(Point a, Point b, Point c) {
		return getArea(a.getX(), b.getX(), c.getX(), a.getY(), b.getY(), c.getY());
	}
	
	public final int getArea(int x1, int x2, int x3, int y1, int y2, int y3) {
		return Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) / 2);
	}

	public final boolean contains(Point p) {
		Polygon poly = new Polygon(new int[] {a.getX(), b.getX(), c.getX()}, new int[] {a.getY(), b.getY(), c.getY()}, 3);
		return poly.contains(p.getX(), p.getY());
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		
		NBTTagCompound data = new NBTTagCompound();
		
		data.setLong("VERSION", getVersion());
		data.setString("CLASS", getClass().getName());
		data.setTag("ORIGIN", origin.serializeNBT());
		
		return data;
	}
}
