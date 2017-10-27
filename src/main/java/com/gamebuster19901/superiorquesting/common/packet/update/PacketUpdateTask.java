package com.gamebuster19901.superiorquesting.common.packet.update;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdateTask extends GenericQuestingPacket{
	
	NBTTagCompound serializedTask;
	
	public PacketUpdateTask() {
		super(PacketType.UPDATE_TASK);
	}
	
	public PacketUpdateTask(Task t) {
		this();
		serializedTask = t.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		serializedTask = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, serializedTask);
	}

}
