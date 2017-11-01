package com.gamebuster19901.superiorquesting.common.packet.life;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;

import io.netty.buffer.ByteBuf;

public class PacketLifeTotal extends GenericQuestingPacket{
	
	private double life;
	
	public PacketLifeTotal() {
		super(PacketType.LIFE_TOTAL);
	};
	
	public PacketLifeTotal(double lives) {
		super(PacketType.LIFE_TOTAL);
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
