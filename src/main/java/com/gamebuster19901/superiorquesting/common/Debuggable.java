package com.gamebuster19901.superiorquesting.common;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.launchwrapper.Launch;

public interface Debuggable {
	boolean debug = (Boolean)(Launch.blackboard.get("fml.deobfuscatedEnvironment"));
	
	default void debug(Object o){
		debug(o,null);
	}
	static void debug(Object o, Object unused){
		if (debug){
			StackTraceElement[] x = Thread.currentThread().getStackTrace();
			int i = 2;
			if (x[2].getClassName().equals(Debuggable.class.getName())){
				i++;
			}
			Main.LOGGER.debug("[" + x[i].getClassName().substring(x[i].getClassName().lastIndexOf('.') + 1) + " " + x[i].getLineNumber() + "]: " + o);
		}
	}
}
