package com.gamebuster19901.superiorquesting.common;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public interface IngameDebuggable extends Debuggable{
	default void debug(EntityPlayer p, Object o){
		debug(p, o, null);
	}
	static void debug(EntityPlayer p, Object o, Object unused){
		if (debug){
			p.sendMessage(new TextComponentString(o.toString()));
		}
	}
}
