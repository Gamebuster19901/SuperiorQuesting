package com.gamebuster19901.superiorquesting.common.packet.create;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketNewReward extends GenericQuestingPacket{

	public NBTTagCompound serializedReward;
	
	public PacketNewReward() {
		super(PacketType.NEW_REWARD);
	}
	
	public PacketNewReward(Reward r) {
		this();
		serializedReward = r.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		serializedReward = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, serializedReward);
	}
	
}
