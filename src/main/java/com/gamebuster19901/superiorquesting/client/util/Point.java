package com.gamebuster19901.superiorquesting.client.util;

import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;

public class Point implements Shape{
	private static final long VERSION = 1L;
	private int x;
	private int y;
	
	public Point() {
		this(0,0);
	}
	
	public Point(NBTTagCompound nbt) {
		deserializeNBT(nbt);
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
		nbt.setString("CLASS", Point.class.getCanonicalName());
		nbt.setInteger("X", x);
		nbt.setInteger("Y", y);
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
				x = data.getInteger("X");
				y = data.getInteger("Y");
			}
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
}
