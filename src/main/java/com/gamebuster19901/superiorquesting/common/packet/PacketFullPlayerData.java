package com.gamebuster19901.superiorquesting.common.packet;

import static com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType.FULL_PLAYER_DATA;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;;

public class PacketFullPlayerData extends GenericQuestingPacket{

	public PacketFullPlayerData() {
		super(FULL_PLAYER_DATA);
	}
	
	public PacketFullPlayerData(NBTTagCompound nbt) {
		super(FULL_PLAYER_DATA);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

}
