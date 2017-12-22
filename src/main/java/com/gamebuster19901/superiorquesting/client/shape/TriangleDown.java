package com.gamebuster19901.superiorquesting.client.shape;

import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;

public class TriangleDown extends Triangular{
	private static final long VERSION = 1L;
	private int size;
	
	public TriangleDown() {
		this(16);
	}
	
	public TriangleDown(NBTTagCompound nbt) {
		super(nbt);
	}
	
	public TriangleDown(int size) {
		this(new Point(0,0),size);
	}
	
	public TriangleDown(int x, int y, int size) {
		this(new Point(x,y),size);
	}
	
	public TriangleDown(Point p, int size) {
		super(p, new Point(0, size), new Point(size, size / 2), new Point(0,0));
		origin = p;
		this.size = size;
	}

	@Override
	public Rectangle getBounds() {
		return new Square(origin, size + 1);
	}
	
	public void moveTo(Point p) {
		this.shift(p.getX() - origin.getX(), p.getY() - origin.getY());
	}
	
	public void moveTo(int x, int y) {
		this.moveTo(new Point(x, y));
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
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("SIZE", size);
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
			
			origin = new Point(data.getCompoundTag("ORIGIN"));
			int size = data.getInteger("SIZE");
			Assert(size < 256, "Size > 255");
			a = new Point(0, size);
			b = new Point(size, size / 2);
			c = new Point(0,0);
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
}
