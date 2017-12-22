package com.gamebuster19901.superiorquesting.common;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface UpdatableSerializable extends INBTSerializable<NBTTagCompound>, Assertable, NBTDebugger, Debuggable{
	public void convert(long prevVersion, long nextVersion, NBTTagCompound nbtIn);
	public long getVersion();
	public void deserializeNBT(NBTTagCompound data); //so it's named data by default
}
