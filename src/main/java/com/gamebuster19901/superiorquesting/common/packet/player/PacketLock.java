package com.gamebuster19901.superiorquesting.common.packet.player;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.task.Assignment;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketLock extends GenericQuestingPacket{
	public Class<? extends Assignment> type;
	public UUID id;
	
	public PacketLock() {
		super(PacketType.LOCK);
	}
	
	public PacketLock(Assignment a) {
		this();
		if(a instanceof Quest) {
			type = Quest.class;
		}
		else if (a instanceof Task) {
			type = Task.class;
		}
		else {
			throw new AssertionError();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		try {
			type = (Class<? extends Assignment>) Class.forName(nbt.getString("CLASS"));
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
