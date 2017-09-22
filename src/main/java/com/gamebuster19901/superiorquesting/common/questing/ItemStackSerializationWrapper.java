package com.gamebuster19901.superiorquesting.common.questing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.UpdatableSerializable;
import com.gamebuster19901.superiorquesting.common.questing.exception.FutureVersionError;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class ItemStackSerializationWrapper implements UpdatableSerializable{
	private static final Method writeMethod = ReflectionHelper.findMethod(NBTTagCompound.class, "write", "func_74734_a", DataOutput.class);
	private static final Method readMethod = ReflectionHelper.findMethod(NBTTagCompound.class, "read", "func_152446_a", DataInput.class, int.class, NBTSizeTracker.class);
	private static final long serialVersionUID = 0L;
	private long VERSION = serialVersionUID;
	private transient ItemStack stack;
	
	ItemStackSerializationWrapper(ItemStack stack){
		this.stack = stack;
	}
	
	public ItemStack asItem() {
		return stack;
	}
	
	public void writeObject(ObjectOutputStream ops) throws IOException{
		ops.writeLong(VERSION);
		CompressedStreamTools.write(stack.getTagCompound(), ops);
	}
	
	@Override
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		long inVersion = in.readLong();
		if(inVersion == serialVersionUID) {
			VERSION = serialVersionUID;
			stack = new ItemStack(CompressedStreamTools.read(in, NBTSizeTracker.INFINITE));
		}
		else {
			convert(serialVersionUID, inVersion, in);
		}
	}
	
	@Override
	public void convert(long prevVersion, long nextVersion, ObjectInputStream in) {
		try {
			if(nextVersion > VERSION) {
				throw new FutureVersionError(nextVersion + " is a future version, currently on version " + VERSION);
			}
			if(nextVersion == VERSION) {
				throw new AssertionError(new IllegalArgumentException(prevVersion + " == " + nextVersion));
			}
			if(nextVersion > prevVersion + 1L) {
				convert(prevVersion, nextVersion - 1L, in);
				return;
			}
		

			if(prevVersion == 0L && nextVersion == 1L) {
				Main.LOGGER.log(Level.INFO, "Converting quest from version " + prevVersion + " to version " + nextVersion);
				throw new FutureVersionError("1 is a future version, currently on version 0");
			}
			
			throw new AssertionError("Tried to convert directly from version " + prevVersion + "to version " + nextVersion);
			
		}
		catch(Exception | AssertionError e) {
			throw new VersioningError(e);
		}
	}
}
