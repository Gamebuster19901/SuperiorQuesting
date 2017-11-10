package com.gamebuster19901.superiorquesting.common.network.packet.update;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Quest;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdateQuest extends GenericQuestingPacket{

	public NBTTagCompound serializedQuest;
	
	public PacketUpdateQuest() {
		super(PacketType.UPDATE_QUEST);
	}
	
	public PacketUpdateQuest(Quest q) {
		this();
		serializedQuest = q.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		serializedQuest = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, serializedQuest);
	}
	
}
