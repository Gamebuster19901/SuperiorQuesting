package com.gamebuster19901.superiorquesting.common.network.packet.remove;

import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketRemovePage extends GenericQuestingPacket{
	private UUID id;
	
	public PacketRemovePage() {
		super(PacketType.REMOVE_PAGE);
	}
	
	public PacketRemovePage(UUID id) {
		this();
		this.id = id;
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
	}
}
