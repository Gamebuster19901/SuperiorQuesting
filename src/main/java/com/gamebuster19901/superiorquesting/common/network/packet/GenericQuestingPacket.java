package com.gamebuster19901.superiorquesting.common.network.packet;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.network.packet.create.PacketNewQuest;
import com.gamebuster19901.superiorquesting.common.network.packet.create.PacketNewReward;
import com.gamebuster19901.superiorquesting.common.network.packet.create.PacketNewTask;
import com.gamebuster19901.superiorquesting.common.network.packet.life.PacketFinalDeath;
import com.gamebuster19901.superiorquesting.common.network.packet.life.PacketLifeTotal;
import com.gamebuster19901.superiorquesting.common.network.packet.life.PacketMaxLife;
import com.gamebuster19901.superiorquesting.common.network.packet.life.PacketStartingLifeTotal;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.collection.PacketQuestCollect;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.collection.PacketQuestUncollect;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.finish.PacketQuestFinish;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.finish.PacketQuestUnfinish;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.hide.PacketQuestHide;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.hide.PacketQuestUnhide;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.lock.PacketQuestLock;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.lock.PacketQuestUnlock;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.notify.PacketQuestNotify;
import com.gamebuster19901.superiorquesting.common.network.packet.player.quest.notify.PacketQuestUnnotify;
import com.gamebuster19901.superiorquesting.common.network.packet.player.reward.collection.PacketRewardCollect;
import com.gamebuster19901.superiorquesting.common.network.packet.player.reward.collection.PacketRewardUncollect;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.finish.PacketTaskFinish;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.finish.PacketTaskUnfinish;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.hide.PacketTaskHide;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.hide.PacketTaskUnhide;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.lock.PacketTaskLock;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.lock.PacketTaskUnlock;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.notify.PacketTaskNotify;
import com.gamebuster19901.superiorquesting.common.network.packet.player.task.notify.PacketTaskUnnotify;
import com.gamebuster19901.superiorquesting.common.network.packet.remove.PacketRemoveQuest;
import com.gamebuster19901.superiorquesting.common.network.packet.remove.PacketRemoveReward;
import com.gamebuster19901.superiorquesting.common.network.packet.remove.PacketRemoveTask;
import com.gamebuster19901.superiorquesting.common.network.packet.update.PacketUpdateQuest;
import com.gamebuster19901.superiorquesting.common.network.packet.update.PacketUpdateReward;
import com.gamebuster19901.superiorquesting.common.network.packet.update.PacketUpdateTask;

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
		
		QUEST_FINISH,
		QUEST_UNFINISH,
		QUEST_COLLECT,
		QUEST_UNCOLLECT,
		QUEST_NOTIFY,
		QUEST_UNNOTIFY,
		QUEST_LOCK,
		QUEST_UNLOCK,
		QUEST_HIDE,
		QUEST_UNHIDE,
		
		TASK_FINISH,
		TASK_UNFINISH,
		TASK_NOTIFY,
		TASK_UNNOTIFY,
		TASK_LOCK,
		TASK_UNLOCK,
		TASK_HIDE,
		TASK_UNHIDE,
		
		REWARD_COLLECT,
		REWARD_UNCOLLECT,
		
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
				
				case QUEST_FINISH:
					return PacketQuestFinish.class;
				case QUEST_UNFINISH:
					return PacketQuestUnfinish.class;
				case QUEST_COLLECT:
					return PacketQuestCollect.class;
				case QUEST_UNCOLLECT:
					return PacketQuestUncollect.class;
				case QUEST_NOTIFY:
					return PacketQuestNotify.class;
				case QUEST_UNNOTIFY:
					return PacketQuestUnnotify.class;
				case QUEST_LOCK:
					return PacketQuestLock.class;
				case QUEST_UNLOCK:
					return PacketQuestUnlock.class;
				case QUEST_HIDE:
					return PacketQuestHide.class;
				case QUEST_UNHIDE:
					return PacketQuestUnhide.class;
					
				case TASK_FINISH:
					return PacketTaskFinish.class;
				case TASK_UNFINISH:
					return PacketTaskUnfinish.class;
				case TASK_NOTIFY:
					return PacketTaskNotify.class;
				case TASK_UNNOTIFY:
					return PacketTaskUnnotify.class;
				case TASK_LOCK:
					return PacketTaskLock.class;
				case TASK_UNLOCK:
					return PacketTaskUnlock.class;
				case TASK_HIDE:
					return PacketTaskHide.class;
				case TASK_UNHIDE:
					return PacketTaskUnhide.class;
					
				case REWARD_COLLECT:
					return PacketRewardCollect.class;
				case REWARD_UNCOLLECT:
					return PacketRewardUncollect.class;
				
				case FINAL_DEATH:
					return PacketFinalDeath.class;
				default:
					Assertable.Assert(false, this + " has no associated class", (Object)null);
					return null; //unreachable code
			}
		}
	}
}
