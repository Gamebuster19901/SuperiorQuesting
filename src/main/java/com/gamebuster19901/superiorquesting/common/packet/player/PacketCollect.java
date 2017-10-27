package com.gamebuster19901.superiorquesting.common.packet.player;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;
import com.gamebuster19901.superiorquesting.common.questing.reward.Rewardable;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketCollect extends GenericQuestingPacket{
	public Class<? extends Rewardable> type;
	public UUID id;
	
	public PacketCollect() {
		super(PacketType.COLLECT);
	}
	
	public PacketCollect(Rewardable r) {
		this();
		if(r instanceof Quest) {
			type = Quest.class;
		}
		else if(r instanceof Reward) {
			type = Reward.class;
		}
		else {
			throw new AssertionError();
		}
		id = r.getUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		try {
			type = (Class<? extends Rewardable>) Class.forName(nbt.getString("CLASS"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		id = UUID.fromString(nbt.getString("UUID"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("CLASS", type.getCanonicalName());
		nbt.setString("UUID", id.toString());
		ByteBufUtils.writeTag(buf, nbt);
	}
}
