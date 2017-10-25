package com.gamebuster19901.superiorquesting.common.packet;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class GenericQuestingPacket implements IMessage{
	
	private final PacketType type;
	
	public GenericQuestingPacket(PacketType t) {
		type = t;
	}
	
	public final PacketType getType() {
		return type;
	}
	
	public static enum PacketType {
		LIFE_TOTAL,
		LIFE_MAXIMUM,
		LIFE_STARTING_TOTAL,
		FULL_QUEST_DATA,
		UPDATE_QUEST,
		UPDATE_TASK,
		UPDATE_REWARD,
		FULL_PLAYER_DATA,
		UPDATE_PLAYER_QUEST,
		UPDATE_PLAYER_TASK,
		UPDATE_PLAYER_REWARD,
		FINAL_DEATH;
		
		public Class<? extends GenericQuestingPacket> getMappedClass(){
			switch(this){
			case LIFE_TOTAL:
				return PacketLifeTotal.class;
			case LIFE_MAXIMUM:
				return PacketMaxLife.class;
			case LIFE_STARTING_TOTAL:
				return PacketStartingLifeTotal.class;
			case FULL_QUEST_DATA:
				return PacketFullQuestData.class;
			case FULL_PLAYER_DATA:
				return PacketFullPlayerData.class;
			case FINAL_DEATH:
				return PacketFinalDeath.class;
			default:
				Main.LOGGER.log(Level.WARN, new AssertionError());
				return null;
			}
		}
	}
}
