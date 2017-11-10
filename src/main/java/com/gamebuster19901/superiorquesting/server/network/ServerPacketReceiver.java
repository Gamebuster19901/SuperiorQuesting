package com.gamebuster19901.superiorquesting.server.network;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, Debuggable{
	
	public ServerPacketReceiver(Class<Message> type) {debug(type);}
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) { 
		EntityPlayerMP p = ctx.getServerHandler().player;
		if(p.getServer().canUseCommand(2, "")) {	//the server should never receive a packet from a non-op
			switch(message.getType()) {
				case NEW_QUEST:
					break;
				case NEW_REWARD:
					break;
				case NEW_TASK:
					break;

				case UPDATE_QUEST:
					break;
				case UPDATE_REWARD:
					break;
				case UPDATE_TASK:
					break;
					
				case QUEST_COLLECT:
				case QUEST_FINISH:
				case QUEST_HIDE:
				case QUEST_LOCK:
				case QUEST_NOTIFY:
				case QUEST_UNCOLLECT:
				case QUEST_UNFINISH:
				case QUEST_UNHIDE:
				case QUEST_UNLOCK:
				case QUEST_UNNOTIFY:
				case REMOVE_QUEST:
				case REMOVE_REWARD:
				case REMOVE_TASK:
				case REWARD_COLLECT:
				case REWARD_UNCOLLECT:
				case TASK_FINISH:
				case TASK_HIDE:
				case TASK_LOCK:
				case TASK_NOTIFY:
				case TASK_UNFINISH:
				case TASK_UNHIDE:
				case TASK_UNLOCK:
				case TASK_UNNOTIFY:
				case FINAL_DEATH:
				case LIFE_MAXIMUM:
				case LIFE_STARTING_TOTAL:
				case LIFE_TOTAL:
					ctx.getServerHandler().disconnect(new TextComponentString("Illegal Packet, client should not have sent packet " + message.getType()));
			default:
				ctx.getServerHandler().disconnect(new TextComponentString("Unknown packet received (" + message.getType() + ')'));
			}
		}
		else {
			ctx.getServerHandler().disconnect(new TextComponentString("Unauthorized packet received, not enough permission (" + message.getType() + ')'));
		}
		return null;
	}

}
