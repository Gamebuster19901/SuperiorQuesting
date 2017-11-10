package com.gamebuster19901.superiorquesting.common.network.packet.create;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketNewTask extends GenericQuestingPacket{

	public NBTTagCompound serializedTask;
	
	public PacketNewTask() {
		super(PacketType.NEW_TASK);
	}
	
	public PacketNewTask(Task t) {
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
