package com.gamebuster19901.superiorquesting.common.network.packet.update;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Page;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdatePage extends GenericQuestingPacket{
	public NBTTagCompound serializedPage;
	
	public PacketUpdatePage() {
		super(PacketType.UPDATE_PAGE);
	}
	
	public PacketUpdatePage(Page p) {
		this();
		serializedPage = p.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		serializedPage = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, serializedPage);
	}
}
