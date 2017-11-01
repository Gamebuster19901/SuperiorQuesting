package com.gamebuster19901.superiorquesting.common.packet.remove;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketRemoveReward extends GenericQuestingPacket{
	private UUID id;
	
	public PacketRemoveReward() {
		super(PacketType.REMOVE_REWARD);
	}
	
	public PacketRemoveReward(UUID id) {
		this();
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id.toString());
	}
}
