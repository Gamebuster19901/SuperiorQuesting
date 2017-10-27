package com.gamebuster19901.superiorquesting.server.packet.handle;

import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable{
	
	public ServerPacketReceiver(Class<Message> type) {
		
	}
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) { //the server should never receive a packet from the client
		ctx.getServerHandler().disconnect(new TextComponentString("Illegal Packet (" + message.getType() + ")"));
		return null;
	}

}
