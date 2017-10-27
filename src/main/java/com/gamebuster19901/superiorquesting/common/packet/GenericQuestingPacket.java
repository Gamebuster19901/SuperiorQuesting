package com.gamebuster19901.superiorquesting.common.packet;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewQuest;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewReward;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewTask;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketFinalDeath;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketMaxLife;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketStartingLifeTotal;

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
		
		NEW_QUEST,
		NEW_TASK,
		NEW_REWARD,
		
		UPDATE_QUEST,
		UPDATE_TASK,
		UPDATE_REWARD,
		
		REMOVE_QUEST,
		REMOVE_TASK,
		REMOVE_REWARD,
		
		FINISH,
		COLLECT,
		NOTIFY,
		LOCK,
		UNLOCK,
		HIDE,
		UNHIDE,
		
		FINAL_DEATH;
		
		public Class<? extends GenericQuestingPacket> getMappedClass(){
			switch(this){
			case LIFE_TOTAL:
				return PacketLifeTotal.class;
			case LIFE_MAXIMUM:
				return PacketMaxLife.class;
			case LIFE_STARTING_TOTAL:
				return PacketStartingLifeTotal.class;
				
			case NEW_QUEST:
				return PacketNewQuest.class;
			case NEW_TASK:
				return PacketNewTask.class;
			case NEW_REWARD:
				return PacketNewReward.class;
				
			case REMOVE_QUEST:
			case REMOVE_TASK:
			case REMOVE_REWARD:
				
			case FINISH:
			case COLLECT:
			case NOTIFY:
			case LOCK:
			case UNLOCK:
			case HIDE:
			case UNHIDE:
				
			case FINAL_DEATH:
				return PacketFinalDeath.class;
			default:
				Main.LOGGER.log(Level.WARN, new AssertionError());
				return null;
			}
		}
	}
}
