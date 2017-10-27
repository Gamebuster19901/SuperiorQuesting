package com.gamebuster19901.superiorquesting.proxy;

import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.server.packet.handle.ServerPacketReceiver;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public final class ServerProxy extends Proxy {
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
		
		PacketType[] types = GenericQuestingPacket.PacketType.values();
		for(int discriminator = 0; discriminator < types.length; discriminator++) {
			Class<? extends GenericQuestingPacket> clazz = types[discriminator].getMappedClass();
			if(clazz != null) {
				NETWORK.registerMessage(new ServerPacketReceiver(clazz), clazz, discriminator, Side.SERVER);
			}
		}
	}
	
	public void init(FMLInitializationEvent e){
		super.init(e);
	}
	
	public void postInit(FMLPostInitializationEvent e){
		super.postInit(e);
	}
}
