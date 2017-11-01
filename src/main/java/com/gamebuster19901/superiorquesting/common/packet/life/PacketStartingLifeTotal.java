package com.gamebuster19901.superiorquesting.common.packet.life;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;

import io.netty.buffer.ByteBuf;

public class PacketStartingLifeTotal extends GenericQuestingPacket {
	private double amount;
	
	public PacketStartingLifeTotal() {
		super(PacketType.LIFE_STARTING_TOTAL);
	}
	
	public PacketStartingLifeTotal(double amount) {
		super(PacketType.LIFE_STARTING_TOTAL);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		amount = buf.getDouble(0);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(amount);
	}

}
