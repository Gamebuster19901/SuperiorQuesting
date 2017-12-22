package com.gamebuster19901.superiorquesting.common.shape;

import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.nbt.NBTTagCompound;

public class Square extends Rectangle{
	private static final long VERSION = 1L;
	private int size;
	public Square() {
		this(16);
	}
	
	public Square(NBTTagCompound compoundTag) {
		// TODO Auto-generated constructor stub
	}
	
	public Square(int size) {
		this(0,0,size);
	}
	
	public Square(int x, int y, int size) {
		this(new Point(x, y), size);
	}
	
	public Square(Point p, int size) {
		super(p, size, size);
		this.size = size;
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
			else {
				origin = new Point(data.getCompoundTag("ORIGIN"));
				width = data.getInteger("SIZE");
				height = data.getInteger("SIZE");
			}
		}
		catch(Exception | AssertionError e) {
			throw new SerializationException(e);
		}
	}
}
