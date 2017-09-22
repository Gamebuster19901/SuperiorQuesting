package com.gamebuster19901.superiorquesting.common.questing;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class ItemStackSerializationWrapper implements Serializable{
	private static final Method writeMethod = ReflectionHelper.findMethod(NBTTagCompound.class, "write", "func_74734_a", DataOutput.class);
	private static final Method readMethod = ReflectionHelper.findMethod(NBTTagCompound.class, "read", "func_152446_a", DataInput.class, int.class, NBTSizeTracker.class);
	private transient ItemStack stack;
	
	ItemStackSerializationWrapper(ItemStack stack){
		this.stack = stack;
	}
	
	public ItemStack asItem() {
		return stack;
	}
	
	private void writeObject(ObjectOutputStream ops) throws IOException{
		CompressedStreamTools.write(stack.getTagCompound(), ops);
	}
	
	private void readObject(ObjectInputStream ips) throws IOException{
		try {
			ips.defaultReadObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		stack = new ItemStack(CompressedStreamTools.read(ips, NBTSizeTracker.INFINITE));
	}
}
