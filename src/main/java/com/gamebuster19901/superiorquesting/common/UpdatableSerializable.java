package com.gamebuster19901.superiorquesting.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface UpdatableSerializable extends INBTSerializable<NBTTagCompound>{
	public void convert(long prevVersion, long nextVersion, NBTTagCompound in);
	public long getVersion();
	
}
