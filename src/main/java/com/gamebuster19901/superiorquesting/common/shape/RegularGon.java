package com.gamebuster19901.superiorquesting.common.shape;

import java.awt.Polygon;

import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;

import net.minecraft.nbt.NBTTagCompound;

public class RegularGon implements Shape{
	private static final long VERSION = 1l;
	private Point[] points;
	private Rectangle bounds;
	private double angle;
	
	public RegularGon(NBTTagCompound nbt) {
		deserializeNBT(nbt);
	}
	
	public RegularGon(int verticies, double angle, int size) {
		this(verticies, angle, new Square(size));
	}
	
	public RegularGon(int verticies, double angle, int x, int y, int size) {
		this(verticies, angle, new Square(x, y, size));
	}
	
	public RegularGon(int verticies, double angle, Square square) {
		construct(verticies, angle, square);
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

	@Override
	public void convert(long prevVersion, long nextVersion, NBTTagCompound in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getVersion() {
		return VERSION;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", getVersion());
		nbt.setString("CLASS", RegularGon.class.getCanonicalName());
		nbt.setTag("BOUNDS", bounds.serializeNBT());
		nbt.setDouble("ANGLE", angle);
		nbt.setInteger("VERTICIES", points.length);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound data) {
		long ver = data.getLong("VERSION");
		try {
			try {
				Assert(ver != 0, "Missing version data in point");
			}
			catch(AssertionError e) {
				debug(getFullNBTString(data, 1));
				throw e;
			}
			if(ver != VERSION) {
				convert(ver, VERSION, data);
			}
			else {
				construct(data.getInteger("VERTICIES"), data.getDouble("ANGLE"), new Square(data.getCompoundTag("BOUNDS")));
			}
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
	
	private final void construct(int verticies, double angle, Square square) {
		if(verticies < 5) {
			throw new IndexOutOfBoundsException ("Must have at least 5 verticies");
		}
		if(verticies > 25) {
			throw new IndexOutOfBoundsException ("Must have less than 25 verticies");
		}
		points = new Point[verticies];
		bounds = square;
		this.angle = angle;
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
}
