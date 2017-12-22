package com.gamebuster19901.superiorquesting.client.util;

import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;

public class Circle implements Shape{
	private static final long VERSION = 1l;
	private Point origin;
	private int diameter;
	
	public Circle() {
		this(16);
	}
	
	public Circle(NBTTagCompound nbt) {
		deserializeNBT(nbt);
	}
	
	public Circle(int d) {
		this(0,0,d);
	}
	
	public Circle(int x, int y, int d) {
		this(new Point(x,y),d);
	}
	
	public Circle(Point p, int d) {
		origin = p;
		diameter = d;
	}

	@Override
	public Rectangle getBounds() {
		int actualDiameter = diameter;
		if(diameter % 2 == 0) {
			actualDiameter++;
		}
		return new Square(origin, actualDiameter);
	}

	@Override
	public Point getOrigin() {
		return origin;
	}
	
	public Point getCenter() {
		return getBounds().getCenter();
	}

	@Override
	public boolean contains(Point p) {
		int x = getCenter().getX();
		int y = getCenter().getY();
		
		int xSquared = (x - p.getX())*(x - p.getX());
		int ySquared = (y - p.getY())*(y - p.getY());
		
		if(diameter % 2 == 1) {
			return (double)diameter / 2d > Math.sqrt(xSquared + ySquared);
		}
		else {
			return (double)diameter / 2d >= Math.sqrt(xSquared + ySquared);
		}
	}

	@Override
	public void shift(int shiftX, int shiftY) {
		origin.shift(shiftX, shiftY);
	}

	@Override
	public void convert(long prevVersion, long nextVersion, NBTTagCompound nbtIn) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);	
			}
			Assert(nextVersion != prevVersion, "Cannot convert to a version if it is the same version, this should never happen! (" + nextVersion + ")");
			if(nextVersion > prevVersion + 1L) {
				convert(prevVersion, nextVersion - 1L, nbtIn);
			}
			
			if(prevVersion == 0L && nextVersion == 1L) {
				throw new AssertionError("Tried to convert from nonexistant version 0 to version 1");
			}
			
			if(prevVersion == 1L && nextVersion == 2L) {
				//Future: convert from version 1 to version 2
			}
			
			throw new AssertionError("Tried to convert directl from version " + prevVersion + " to version " + nextVersion);
		}
		catch(Exception| AssertionError e) {
			throw new VersioningError("There was an issue converting from version " + prevVersion + " to version " + nextVersion, e);
		}
		
	}

	@Override
	public long getVersion() {
		return VERSION;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("VERSION", getVersion());
		nbt.setString("CLASS", Circle.class.getCanonicalName());
		nbt.setTag("ORIGIN", origin.serializeNBT());
		nbt.setInteger("DIAMETER", diameter);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound data) {
		long ver = data.getLong("VERSION");
		Class<? extends Shape> clazz = Shape.getShapeClassFromNBT(data);
		try {
			try {
				Assert(ver != 0, "Missing version data in " + Circle.class.getSimpleName() + " instance");
				Assert(clazz.equals(Circle.class), clazz.getName() + " is not " + Circle.class.getName() + '!');
			}
			catch(AssertionError e) {
				debug(getFullNBTString(data, 1));
				throw e;
			}
			if(ver != VERSION) {
				convert(ver, VERSION, data);
			}
			else {
				origin = new Point(data.getCompoundTag("ORIGIN"));
				diameter = data.getInteger("DIAMETER");
			}
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
}
