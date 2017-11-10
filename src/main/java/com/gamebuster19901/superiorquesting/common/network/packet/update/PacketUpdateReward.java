package com.gamebuster19901.superiorquesting.common.network.packet.update;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdateReward extends GenericQuestingPacket{

	public NBTTagCompound serializedReward;
	
	public PacketUpdateReward() {
		super(PacketType.UPDATE_REWARD);
	}
	
	public PacketUpdateReward(Reward r) {
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
