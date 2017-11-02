package com.gamebuster19901.superiorquesting.common.packet.player.reward.collection;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.common.questing.Quest;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketRewardUncollect extends GenericQuestingPacket{
	public UUID id;
	
	public PacketRewardUncollect() {
		super(PacketType.QUEST_COLLECT);
	}
	
	public PacketRewardUncollect(Quest q) {
		this();
		id = q.getUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		id = UUID.fromString(nbt.getString("UUID"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("UUID", id.toString());
		ByteBufUtils.writeTag(buf, nbt);
	}
}
