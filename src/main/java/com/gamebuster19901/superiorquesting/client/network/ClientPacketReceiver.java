package com.gamebuster19901.superiorquesting.client.network;

import static com.gamebuster19901.superiorquesting.Main.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.client.gui.GuiTrueGameOver;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, Debuggable{

	public ClientPacketReceiver(Class<Message> type) {debug(type);};
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {
		ByteBuf b;
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		debug(message.getType());
		switch(message.getType()) {
			/*
			 * 
			 * 
			 * LIFE
			 * 
			 * 
			 */
			case LIFE_TOTAL:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				double life = b.getDouble(0);
				if(p != null) {
					p.sendMessage(new TextComponentString("Received new life total: " + life));
					proxy.getLifeHandler().setLives(p, life, false);
				}
				return null;
			case LIFE_MAXIMUM:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				proxy.getLifeHandler().setMaxLives(b.getDouble(0));
				return null;
			case LIFE_STARTING_TOTAL:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				proxy.getLifeHandler().setStartingLives(b.getDouble(0));
				return null;
				
			/*
			 * 
			 * 
			 * CREATION
			 * 
			 * 
			 */
				
			case NEW_QUEST:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				new Quest(p.getServer(), ByteBufUtils.readTag(b));
				return null;
			case NEW_TASK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				try {
					Class <? extends Task> clazz;
					final NBTTagCompound nbt = ByteBufUtils.readTag(b);
					clazz = (Class<? extends Task>) Class.forName(nbt.getString("CLASS"));
					Constructor c = clazz.getConstructor(NBTTagCompound.class);
					c.newInstance(nbt);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new AssertionError(e);
				}
				return null;
			case NEW_REWARD:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				try {
					Class <? extends Reward> clazz;
					final NBTTagCompound nbt = ByteBufUtils.readTag(b);
					clazz = (Class<? extends Reward>) Class.forName(nbt.getString("CLASS"));
					Constructor c = clazz.getConstructor(NBTTagCompound.class);
					c.newInstance(nbt);
				} catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new AssertionError(e);
				}
				return null;
				
			/*
			 * 
			 * 
			 * UPDATION
			 * 
			 * 
			 */
				
			case UPDATE_QUEST:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					NBTTagCompound newNBT = ByteBufUtils.readTag(b);
					UUID questID = UUID.fromString(newNBT.getString("UUID"));
					Quest q = Main.proxy.getGlobalQuestHandler().getQuest(questID);
					q.deserializeNBT(newNBT);
				}
				return null;
			case UPDATE_TASK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					NBTTagCompound newNBT = ByteBufUtils.readTag(b);
					UUID taskID = UUID.fromString(newNBT.getString("UUID"));
					Task t = Main.proxy.getGlobalQuestHandler().getTask(taskID);
					t.deserializeNBT(newNBT);
				}
				return null;
			case UPDATE_REWARD:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					NBTTagCompound newNBT = ByteBufUtils.readTag(b);
					UUID rewardID = UUID.fromString(newNBT.getString("UUID"));
					Reward r = Main.proxy.getGlobalQuestHandler().getReward(rewardID);
					r.deserializeNBT(newNBT);
				}
				return null;
			
			/*
			 * 
			 * 
			 * REMOVAL
			 * 
			 * 
			 */
				
			case REMOVE_QUEST:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().removeQuest(p.getServer(), id);
				}
				return null;
			case REMOVE_TASK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().removeTask(p.getServer(), id);
				}
				return null;
			case REMOVE_REWARD:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().removeReward(p.getServer(), id);
				}
				return null;
				
			/*
			 * 
			 * 
			 * PLAYER QUEST DATA
			 * 
			 * 
			 */
				
			case QUEST_FINISH:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				NBTTagCompound nbt = ByteBufUtils.readTag(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).finish(p);
				}
				return null;
			case QUEST_COLLECT:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).collect(p);
				}
				return null;
			case QUEST_NOTIFY:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).notify(p);
				}
				return null;
			case QUEST_LOCK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).lock(p);
				}
				return null;
			case QUEST_UNLOCK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).unlock(p);
				}
				return null;
			case QUEST_HIDE:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).hide(p);
				}
				return null;
			case QUEST_UNHIDE:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getQuest(id).unhide(p);
				}
				return null;
				
			/*
			 * 
			 * 
			 * PLAYER TASK DATA 
			 * 
			 * 
			 */
			case TASK_FINISH:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).finish(p);
				}
				return null;
				
			case TASK_UNFINISH:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).markUnfinished(p);
				}
				return null;
			case TASK_NOTIFY:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).notify(p);
				}
				return null;
				
			case TASK_UNNOTIFY:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).markUnnotified(p);
				}
				return null;
			case TASK_LOCK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).lock(p);
				}
				return null;
			case TASK_UNLOCK:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).unlock(p);
				}
				return null;
			case TASK_HIDE:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).hide(p);
				}
				return null;
			case TASK_UNHIDE:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getTask(id).unhide(p);
				}
				return null;
			
			/*
			 *
			 * 
			 * PLAYER REWARD DATA
			 * 
			 * 
			 */
				
			case REWARD_COLLECT:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getReward(id).markCollected(p); //should just mark it to avoid desync issues
				}
				return null;
			case REWARD_UNCOLLECT:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				{
					UUID id = UUID.fromString(ByteBufUtils.readUTF8String(b));
					Main.proxy.getGlobalQuestHandler().getReward(id).markUncollected(p);
				}
				return null;
				
			/*
			 * 
			 * 
			 * DEATH
			 * 
			 * 
			 */
				
			case FINAL_DEATH:
				b = Unpooled.buffer(4);
				message.toBytes(b);
				GuiTrueGameOver.deathCause = new TextComponentString(ByteBufUtils.readUTF8String(b));
				p.openGui(Main.getInstance(), GuiHandler.FINAL_DEATH, p.world, (int)p.posX, (int)p.posY, (int)p.posZ);
				debug("opened gui");
				return null;
			default:
				throw new AssertionError("Don't know how to handle packet " + message.getType());
		}
	}

}
