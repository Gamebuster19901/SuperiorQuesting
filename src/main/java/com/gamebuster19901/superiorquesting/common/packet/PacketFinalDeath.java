package com.gamebuster19901.superiorquesting.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketFinalDeath extends GenericQuestingPacket{
	String deathMessage = "You died";
	public PacketFinalDeath() {
		super(PacketType.FINAL_DEATH);
	}

	public PacketFinalDeath(ITextComponent deathMessage) {
		super(PacketType.FINAL_DEATH);
		this.deathMessage = deathMessage.getFormattedText();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		deathMessage = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, deathMessage);
	}

}
