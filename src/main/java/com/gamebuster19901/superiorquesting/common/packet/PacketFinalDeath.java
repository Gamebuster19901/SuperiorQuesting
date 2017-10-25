package com.gamebuster19901.superiorquesting.common.packet;

import io.netty.buffer.ByteBuf;

public class PacketFinalDeath extends GenericQuestingPacket{

	public PacketFinalDeath() {
		super(PacketType.FINAL_DEATH);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

}
