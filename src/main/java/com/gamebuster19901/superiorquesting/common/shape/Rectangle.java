package com.gamebuster19901.superiorquesting.common.shape;

import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.IllegalShapeException;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;

public class Rectangle implements Shape{
	private static final long VERSION = 1l;
	protected Point origin;
	protected int width;
	protected int height;
	
	public Rectangle() {
		this(16,8);
	}
	
	public Rectangle(NBTTagCompound nbt) {
		deserializeNBT(nbt);
	}
	
	public Rectangle(int w, int h) {
		this(0,0,w,h);
	}
	
	public Rectangle(int x, int y, int w, int h) {
		this(new Point(x,y),w,h);
	}
	
	public Rectangle(Point topLeft, int width, int height) {
		origin = topLeft;
		String error = "";
		if(width > 255 || width < 0 || height > 255 || height < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.width = width;
		this.height = height;
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	public final Point getTopLeft() {
		return origin;
	}
	
	public final Point getCenter() {
		return new Point(origin.getX() + width / 2, origin.getY() + height / 2);
	}
	
	public final Point getBottomRight() {
		return new Point(origin.getX() + width, origin.getY() + height);
	}

	@Override
	public final Rectangle getBounds() {
		return this;
	}

	@Override
	public final boolean contains(Point p) {
		return p.getX() >= origin.getX() || p.getX() <= this.getBottomRight().getX() || p.getY() >= origin.getY() || p.getY() <= this.getBottomRight().getY();
	}

	@Override
	public final void shift(int shiftX, int shiftY) {
		origin.shift(shiftX, shiftY);
	}

	@Override
	public final Point getOrigin() {
		return origin;
	}

	@Override
	public boolean[][] toArray(){
		boolean[][] b = new boolean[height][width];
		
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[0].length; j++) {
				b[i][j] = true;
			}
		}
		
		return b;
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
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + " to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
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
		nbt.setString("CLASS", Rectangle.class.getCanonicalName());
		nbt.setTag("ORIGIN", origin.serializeNBT());
		nbt.setInteger("WIDTH", width);
		nbt.setInteger("HEIGHT", height);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound data) {
		long ver = data.getLong("VERSION");
		try {
			try {
				Assert(ver != 0, "Missing version data in quest " + data.getString("UUID"));
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
				width = data.getInteger("WIDTH");
				height = data.getInteger("HEIGHT");
			}
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
}
