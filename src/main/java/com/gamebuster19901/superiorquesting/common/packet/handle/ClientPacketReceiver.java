package com.gamebuster19901.superiorquesting.common.packet.handle;

import static com.gamebuster19901.superiorquesting.Main.proxy;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, Debuggable{

	public ClientPacketReceiver(Class<Message> type) {debug(type);};
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {
		ByteBuf b;
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		switch(message.getType()) {
		case FULL_QUEST_DATA:
			return null;
		case LIFE_MAXIMUM:
			b = Unpooled.buffer(4);
			message.toBytes(b);
			proxy.getLifeHandler().setMaxLives(b.getDouble(0));
			return null;
		case LIFE_TOTAL:
			b = Unpooled.buffer(4);
			message.toBytes(b);
			proxy.getLifeHandler().setLives(Minecraft.getMinecraft().player, b.getDouble(0), false);
			return null;
		case LIFE_STARTING_TOTAL:
			b = Unpooled.buffer(4);
			message.toBytes(b);
			proxy.getLifeHandler().setStartingLives(b.getDouble(0));
			return null;
		case FINAL_DEATH:
			p.openGui(Main.getInstance(), GuiHandler.FINAL_DEATH, p.world, (int)p.posX, (int)p.posY, (int)p.posZ);
			return null;
		default:
			throw new AssertionError();
		}
	}

}
