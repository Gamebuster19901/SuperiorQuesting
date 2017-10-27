package com.gamebuster19901.superiorquesting.common.packet.life;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType;

import io.netty.buffer.ByteBuf;

public class PacketMaxLife extends GenericQuestingPacket{
	
	private double life;
	
	public PacketMaxLife() {
		super(PacketType.LIFE_MAXIMUM);
	};
	
	public PacketMaxLife(double lives) {
		super(PacketType.LIFE_MAXIMUM);
		life = lives;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		life = buf.getDouble(0);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(life);
	}

}
