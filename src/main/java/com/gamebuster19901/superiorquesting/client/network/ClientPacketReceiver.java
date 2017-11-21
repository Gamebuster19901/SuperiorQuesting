package com.gamebuster19901.superiorquesting.client.network;

import static com.gamebuster19901.superiorquesting.Main.proxy;
import static com.gamebuster19901.superiorquesting.client.network.ClientPacketReceiver.Type.QUEST;
import static com.gamebuster19901.superiorquesting.client.network.ClientPacketReceiver.Type.REWARD;
import static com.gamebuster19901.superiorquesting.client.network.ClientPacketReceiver.Type.TASK;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.client.gui.GuiHandler;
import com.gamebuster19901.superiorquesting.client.gui.GuiTrueGameOver;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.questing.Quest;
import com.gamebuster19901.superiorquesting.common.questing.exception.MalformedTypeError;
import com.gamebuster19901.superiorquesting.common.questing.exception.NonExistantKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.PacketException;
import com.gamebuster19901.superiorquesting.common.questing.exception.SerializationException;
import com.gamebuster19901.superiorquesting.common.questing.exception.VersioningError;
import com.gamebuster19901.superiorquesting.common.questing.reward.Reward;
import com.gamebuster19901.superiorquesting.common.questing.task.Task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, Debuggable{
	private final TypeWrapper typeObject = new TypeWrapper();
	public ClientPacketReceiver(Class<Message> type) {debug(type);};
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {
		ByteBuf b = Unpooled.buffer();
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		debug(message.getType());
		TypeWrapper wrap;
		try {
			switch(message.getType()) {
				/*
				 * 
				 * 
				 * LIFE
				 * 
				 * 
				 */
				case LIFE_TOTAL:
					message.toBytes(b);
					double life = b.getDouble(0);
					if(p != null) {
						proxy.getLifeHandler().setLives(p, life, false);
					}
					return null;
				case LIFE_MAXIMUM:
					message.toBytes(b);
					proxy.getLifeHandler().setMaxLives(b.getDouble(0));
					return null;
				case LIFE_STARTING_TOTAL:
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
					wrap = ensureValidType(b, ctx, QUEST, true);
					break;
				case NEW_TASK:
					wrap = ensureValidType(b, ctx, TASK, true);
					break;
				case NEW_REWARD:
					wrap = ensureValidType(b, ctx, REWARD, true);
					break;
					
				/*
				 * 
				 * 
				 * UPDATION
				 * 
				 * 
				 */
					
				case UPDATE_QUEST:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).deserializeNBT(wrap.nbt);
					break;
				case UPDATE_TASK:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task) wrap.u).deserializeNBT(wrap.nbt);
					break;
				case UPDATE_REWARD:
					wrap = ensureValidType(b, ctx, REWARD, false);
					((Reward) wrap.u).deserializeNBT(wrap.nbt);
					break;
				
				/*
				 * 
				 * 
				 * REMOVAL
				 * 
				 * 
				 */
					
				case REMOVE_QUEST:
					wrap = ensureValidType(b, ctx, QUEST, false);
					Main.proxy.getGlobalQuestHandler().removeQuest(wrap.u.getUUID());
					break;
				case REMOVE_TASK:
					wrap = ensureValidType(b, ctx, TASK, false);
					Main.proxy.getGlobalQuestHandler().removeTask(wrap.u.getUUID());
					break;
				case REMOVE_REWARD:
					wrap = ensureValidType(b, ctx, TASK, false);
					Main.proxy.getGlobalQuestHandler().removeReward(wrap.u.getUUID());
					break;
					
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
					ctx.getClientHandler().handleDisconnect(new SPacketDisconnect(new TextComponentString("Don't know how to handle packet " + "")));
					return null;
			}
		}
		catch(PacketException e) {
			
		}
		return null;
	}
	
	private TypeWrapper ensureValidType(ByteBuf b, MessageContext ctx, Type type, boolean isNew) throws PacketException{
		return typeObject.ensureValidType(b, ctx, type, isNew);
	}
	
	protected static enum Type{
		QUEST,
		TASK,
		REWARD;
		
		public static Type getType(Unique u) {
			if(u instanceof Quest) {
				return QUEST;
			}
			if(u instanceof Task) {
				return TASK;
			}
			if(u instanceof Reward) {
				return REWARD;
			}
			throw new IllegalArgumentException();
		}
	}
	
	protected class TypeWrapper{
		Type t = null;
		NBTTagCompound nbt = null;
		Unique u;
		
		public TypeWrapper ensureValidType(ByteBuf b, MessageContext ctx, Type type, boolean isNew) throws PacketException {
			NBTTagCompound nbt = ByteBufUtils.readTag(b);
			String erroredClass = null;
			try {
				if(nbt != null) {
					this.nbt = nbt;
					if(isNew) {
						switch(type) {
							case QUEST:
								Quest q = new Quest(nbt);
								break;
							case TASK:
								try {
									Class<? extends Task> clazz;
									clazz = (Class<? extends Task>) Class.forName(nbt.getString("CLASS"));
									erroredClass = clazz.getCanonicalName();
									Constructor c = clazz.getConstructor(NBTTagCompound.class);
									Task t = (Task) c.newInstance(nbt);
								}
								catch (NoSuchMethodException e) {
									NoSuchMethodError e2 = new NoSuchMethodError(erroredClass + " is missing a required constructor, contact the mod author");
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (InstantiationException e) {
									InstantiationError e2 = new InstantiationError("an error ocurred when trying to instantiate " + erroredClass);
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (IllegalAccessException e) {
									IllegalAccessError e2 = new IllegalAccessError("the called constructor in " + erroredClass + " is not public");
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (IllegalArgumentException e) {
									Assert(false, "This should not be possible.", e);
								} catch (InvocationTargetException e) {
									throw new ExceptionInInitializerError(e);
								}
								break;
							case REWARD:
								try {
									Class <? extends Reward> clazz;
									clazz = (Class<? extends Reward>) Class.forName(nbt.getString("CLASS"));
									erroredClass = clazz.getCanonicalName();
									Constructor c = clazz.getConstructor(NBTTagCompound.class);
									c.newInstance(nbt);
								} catch (NoSuchMethodException e) {
									NoSuchMethodError e2 = new NoSuchMethodError(erroredClass + " is missing a required constructor, contact the mod author");
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (InstantiationException e) {
									InstantiationError e2 = new InstantiationError("an error ocurred when trying to instantiate " + erroredClass);
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (IllegalAccessException e) {
									IllegalAccessError e2 = new IllegalAccessError("the called constructor in " + erroredClass + " is not public");
									e2.initCause(e);
									throw new MalformedTypeError(e2);
								} catch (IllegalArgumentException e) {
									Assert(false, "This should not be possible.", e);
								} catch (InvocationTargetException e) {
									throw new ExceptionInInitializerError(e);
								}
								break;
							default:
								throw new AssertionError();
						}
					}
					else {
						UUID id = UUID.fromString(nbt.getString("UUID"));
						Unique u;
						switch(type) {
							case QUEST:
								u = Main.proxy.getGlobalQuestHandler().getQuest(id);
								break;
							case TASK:
								u = Main.proxy.getGlobalQuestHandler().getTask(id);
								break;
							case REWARD:
								u = Main.proxy.getGlobalQuestHandler().getReward(id);
								break;
							default:
								throw new AssertionError();
						}
						if(u != null) {
							this.t= type;
							this.u = u;
							return this;
						}
						else {
							throw new NonExistantKeyException("Cannot update " + ("" + type).toLowerCase() + " that doesn't exist(" + id + ")");
						}
					}
				}
				else {
					throw new PacketException("Malformed nbt: Missing nbt");
				}
			}
			catch(MalformedTypeError | ExceptionInInitializerError | VersioningError | SerializationException | ClassNotFoundException | NonExistantKeyException | IllegalArgumentException e) {
				throw new PacketException(e);
			}
			return null;
		}
	}

}
