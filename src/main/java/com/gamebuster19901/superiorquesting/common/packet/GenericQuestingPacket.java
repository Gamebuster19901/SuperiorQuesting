package com.gamebuster19901.superiorquesting.common.packet;

import org.apache.logging.log4j.Level;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewQuest;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewReward;
import com.gamebuster19901.superiorquesting.common.packet.create.PacketNewTask;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketFinalDeath;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketMaxLife;
import com.gamebuster19901.superiorquesting.common.packet.life.PacketStartingLifeTotal;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketCollect;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketFinish;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketHide;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketLock;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketNotify;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketUnhide;
import com.gamebuster19901.superiorquesting.common.packet.player.PacketUnlock;
import com.gamebuster19901.superiorquesting.common.packet.remove.PacketRemoveQuest;
import com.gamebuster19901.superiorquesting.common.packet.remove.PacketRemoveReward;
import com.gamebuster19901.superiorquesting.common.packet.remove.PacketRemoveTask;
import com.gamebuster19901.superiorquesting.common.packet.update.PacketUpdateQuest;
import com.gamebuster19901.superiorquesting.common.packet.update.PacketUpdateReward;
import com.gamebuster19901.superiorquesting.common.packet.update.PacketUpdateTask;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class GenericQuestingPacket implements IMessage, Assertable{
	
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
				
				case UPDATE_QUEST:
					return PacketUpdateQuest.class;
				case UPDATE_TASK:
					return PacketUpdateTask.class;
				case UPDATE_REWARD:
					return PacketUpdateReward.class;
				
				case REMOVE_QUEST:
					return PacketRemoveQuest.class;
				case REMOVE_TASK:
					return PacketRemoveTask.class;
				case REMOVE_REWARD:
					return PacketRemoveReward.class;
				
				case FINISH:
					return PacketFinish.class;
				case COLLECT:
					return PacketCollect.class;
				case NOTIFY:
					return PacketNotify.class;
				case LOCK:
					return PacketLock.class;
				case UNLOCK:
					return PacketUnlock.class;
				case HIDE:
					return PacketHide.class;
				case UNHIDE:
					return PacketUnhide.class;
				
				case FINAL_DEATH:
					return PacketFinalDeath.class;
				default:
					Assertable.Assert(false, this + " has no associated class", (Object)null);
					return null; //unreachable code
			}
		}
	}
}
