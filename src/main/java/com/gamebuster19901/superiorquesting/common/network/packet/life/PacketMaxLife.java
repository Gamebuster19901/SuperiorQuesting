package com.gamebuster19901.superiorquesting.common.network.packet.life;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;

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
