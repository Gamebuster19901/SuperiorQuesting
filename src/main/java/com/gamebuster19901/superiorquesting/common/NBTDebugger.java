package com.gamebuster19901.superiorquesting.common;

import java.util.ArrayList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

public interface NBTDebugger{
	
	
	public default String getFullNBTString(NBTBase nbt, int level) {
		return getFullNBTString(nbt, level, null);
	}
	
	public default String getFullNBTString(NBTBase nbt, int level, String key) {
		String message = "\n";
		if(level < 1) {
			throw new IndexOutOfBoundsException(level + " < " + 1);
		}
		message = message + HelperMethods.computeLines(nbt, level, key);
		/*if(nbt instanceof NBTTagCompound) {
			level++;
			NBTTagCompound compound = (NBTTagCompound)nbt;
			for(String s : compound.getKeySet()) {
				message = message + getFullNBTString(compound.getTag(s), level, s);
			}
			level--;
		}*/
		return message;
	}
	
	public static final class HelperMethods {
		
		private HelperMethods() {
			throw new InstantiationError(this.getClass() + " should not be instantiated.");
		}
		
		private static final String computeLines(NBTBase nbt, int level, String key) {
			String ret = "";
			for(int i = level; i > 1; i--) {
				ret = ret + "\t";
			}
			ret = ret + "[" + nbt.getClass().getSimpleName();
			ret = key == null ? ret + "]" : ret + "|" + key + "]";
			if(nbt instanceof NBTPrimitive) {
				ret = ret + ": " + nbt.toString();
			}
			else if(nbt instanceof NBTTagString) {
				char[] addition = nbt.toString().toCharArray();
				ArrayList<Integer> newlinelocs = new ArrayList<Integer>();
				for(int i = addition.length - 1; i > 0; i--) {
					if(addition[i] == '\n') {
						newlinelocs.add(i);
					}
				}
				
				ret = ret + ": " + new String(addition).replaceAll("\t", "\\t");
			}
			else if (nbt instanceof NBTTagCompound) {
				NBTTagCompound compound = (NBTTagCompound) nbt;
				//ret = ret + "\t";
				level++;
				if(compound.getKeySet().size() == 0) {
					ret = ret + "(Empty)";
				}
				for(String s : compound.getKeySet()) {
					ret = ret + "\n" + computeLines(compound.getTag(s), level, s);
				}
			}
			else {
				ret = ret + ": Unknown data structure";
			}
			ret = ret + "\n";
			return ret;
		}
		
		private static final String computeLines(NBTBase nbt, int level) {
			return computeLines(nbt, level, null);
		}
	}
	
}
