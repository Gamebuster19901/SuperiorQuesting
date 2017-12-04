package com.gamebuster19901.superiorquesting.common.network.packet.player.reward.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import scala.actors.threadpool.Arrays;

public class PacketRewardUncollect extends GenericQuestingPacket{
	public UUID id;
	public List<UUID> players = new ArrayList<UUID>();
	
	public PacketRewardUncollect() {
		super(PacketType.REWARD_UNCOLLECT);
	}
	
	public PacketRewardUncollect(Reward r) {
		this();
		id = r.getUUID();
	}
	
	public PacketRewardUncollect(Reward r, UUID... players) {
		this();
		id = r.getUUID();
		this.players = Arrays.asList(players);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		id = UUID.fromString(nbt.getString("UUID"));
		for(NBTBase b : nbt.getTagList("PLAYERS", 8)) {
			Assert(b instanceof NBTTagString, "NBTBase b is not an instance of NBTTagString, it is of the unexpected type '" + b.getClass().getSimpleName() + "'");
			players.add(UUID.fromString(((NBTTagString)b).getString()));
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList players = new NBTTagList();
		for(UUID p : this.players) {
			players.appendTag(new NBTTagString(p.toString()));
		}
		nbt.setString("UUID", id.toString());
		nbt.setTag("PLAYERS", players);
		
		ByteBufUtils.writeTag(buf, nbt);
	}
}
