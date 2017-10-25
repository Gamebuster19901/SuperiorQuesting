package com.gamebuster19901.superiorquesting.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketFullQuestData extends GenericQuestingPacket{
	private NBTTagCompound nbt;
	
	public PacketFullQuestData() {
		super(PacketType.FULL_QUEST_DATA);
	}
	
	public PacketFullQuestData(NBTTagCompound nbt) {
		this();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
	}
}
