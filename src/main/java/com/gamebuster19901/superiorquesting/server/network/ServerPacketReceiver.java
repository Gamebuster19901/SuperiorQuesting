package com.gamebuster19901.superiorquesting.server.network;

import static com.gamebuster19901.superiorquesting.server.network.ServerPacketReceiver.Type.PAGE;
import static com.gamebuster19901.superiorquesting.server.network.ServerPacketReceiver.Type.QUEST;
import static com.gamebuster19901.superiorquesting.server.network.ServerPacketReceiver.Type.REWARD;
import static com.gamebuster19901.superiorquesting.server.network.ServerPacketReceiver.Type.TASK;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Assertable;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.Unique;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket;
import com.gamebuster19901.superiorquesting.common.network.packet.GenericQuestingPacket.PacketType;
import com.gamebuster19901.superiorquesting.common.questing.Page;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class ServerPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, Debuggable{
	private final TypeWrapper typeObject = new TypeWrapper();
	public ServerPacketReceiver(Class<Message> type) {debug(type);}
	
	@Override
	public IMessage onMessage(Message message, MessageContext ctx) { 
		EntityPlayerMP p = ctx.getServerHandler().player;
		try {
			ByteBuf b = Unpooled.buffer();
			NBTTagCompound nbt;
			TypeWrapper wrap;
			if(p.getServer().canUseCommand(2, "")) {	//the server should never receive a packet from a non-op unless they are collecting a quest
				
				switch(message.getType()) {
					/*
					 * 
					 * 
					 * CREATION
					 * 
					 * 
					 */
					case NEW_PAGE:
						ensureValidType(b, ctx, PAGE, true, false);
						break;
					case NEW_QUEST:
						ensureValidType(b, ctx, QUEST, true, false);
						break;
						
					case NEW_TASK:
						ensureValidType(b, ctx, TASK, true, false);
						break;
						
					case NEW_REWARD:
						ensureValidType(b, ctx, REWARD, true, false);
						break;
						
					/*
					 * 
					 * 
					 * UPDATION
					 * 
					 * 
					 */
					case UPDATE_PAGE:
						wrap = ensureValidType(b, ctx, PAGE, false, false);
						((Page) wrap.u).deserializeNBT(wrap.nbt);
						sendToAll(message);
						break;
	
					case UPDATE_QUEST:
						wrap = ensureValidType(b, ctx, QUEST, false, false);
						((Quest) wrap.u).deserializeNBT(wrap.nbt);
						sendToAll(message);
						break;
						
					case UPDATE_TASK:
						wrap = ensureValidType(b, ctx, TASK, false, false);
						((Task) wrap.u).deserializeNBT(wrap.nbt);
						sendToAll(message);
						break;
						
					case UPDATE_REWARD:
						wrap = ensureValidType(b, ctx, REWARD, false, false);
						((Reward) wrap.u).deserializeNBT(wrap.nbt);
						sendToAll(message);
						break;
						
					/*
					 * 
					 * 
					 * REMOVAL
					 * 
					 * 
					 */
						
					case REMOVE_PAGE:
						wrap = ensureValidType(b, ctx, PAGE, false, false);
						Main.proxy.getGlobalQuestHandler().removePage(wrap.u.getUUID());
						sendToAll(message);
						break;
						
					case REMOVE_QUEST:
						wrap = ensureValidType(b, ctx, QUEST, false, false);
						Main.proxy.getGlobalQuestHandler().removeQuest(wrap.u.getUUID());
						sendToAll(message);
						break;
						
					case REMOVE_TASK:
						wrap = ensureValidType(b, ctx, TASK, false, false);
						Main.proxy.getGlobalQuestHandler().removeTask(wrap.u.getUUID());
						sendToAll(message);
						break;
						
					case REMOVE_REWARD:
						wrap = ensureValidType(b, ctx, REWARD, false, false);
						Main.proxy.getGlobalQuestHandler().removeReward(wrap.u.getUUID());
						sendToAll(message);
						break;
						
					/*
					 * 
					 * 
					 * PLAYER PAGE DATA
					 * 
					 * 
					 */
						
					case PAGE_HIDE:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).hide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Page)wrap.u).hide(affectedOfflinePlayer);
						}
						break;
						
					case PAGE_UNHIDE:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).unhide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Page)wrap.u).unhide(affectedOfflinePlayer);
						}
						break;
						
					case PAGE_LOCK:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).lock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Page)wrap.u).lock(affectedOfflinePlayer);
						}
						break;
						
					case PAGE_UNLOCK:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).unlock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Page)wrap.u).unlock(affectedOfflinePlayer);
						}
						break;
						
					case PAGE_NOTIFY:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).notify(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						break;
						
					case PAGE_UNNOTIFY:
						wrap = ensureValidType(b, ctx, PAGE, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Page)wrap.u).markUnnotified(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Page)wrap.u).markUnnotified(affectedOfflinePlayer);
						}
						break;
						
						
					/*
					 * 
					 * 
					 * PLAYER QUEST DATA
					 * 
					 * 
					 */
						
					case QUEST_UNCOLLECT:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).markUncollected(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).markUncollected(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_FINISH:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).finish(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).finish(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_UNFINISH:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).markUnfinished(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).markUnfinished(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_HIDE:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).hide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).hide(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_UNHIDE:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).unhide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).unhide(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_LOCK:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).lock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).lock(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_UNLOCK:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).unlock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).unlock(affectedOfflinePlayer);
						}
						break;
						
					case QUEST_NOTIFY:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).notify(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						break;
						
					case QUEST_UNNOTIFY:
						wrap = ensureValidType(b, ctx, QUEST, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Quest)wrap.u).markUnnotified(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Quest)wrap.u).markUnnotified(affectedOfflinePlayer);
						}
						break;
						
					/*
					 * 
					 * 
					 * PLAYER REWARD DATA
					 * 
					 * 
					 */
						
					case REWARD_UNCOLLECT:
						wrap = ensureValidType(b, ctx, REWARD, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Reward)wrap.u).markUncollected(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Reward)wrap.u).markUncollected(affectedOfflinePlayer);
						}
						break;
						
					/*
					 * 
					 * 
					 * PLAYER TASK DATA
					 * 
					 * 
					 */
					
					case TASK_FINISH:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).finish(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).finish(affectedOfflinePlayer);
						}
						break;
					case TASK_HIDE:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).hide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).hide(affectedOfflinePlayer);
						}
						break;
					case TASK_LOCK:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).lock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).lock(affectedOfflinePlayer);
						}
						break;
					case TASK_NOTIFY:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).notify(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						break;
					case TASK_UNFINISH:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).markUnfinished(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).markUnfinished(affectedOfflinePlayer);
						}
						break;
					case TASK_UNHIDE:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).unhide(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
							for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).unhide(affectedOfflinePlayer);
						}
						break;
					case TASK_UNLOCK:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).unlock(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).unlock(affectedOfflinePlayer);
						}
						break;
					case TASK_UNNOTIFY:
						wrap = ensureValidType(b, ctx, TASK, false, true);
						nbt = wrap.nbt;
						for(EntityPlayerMP affectedOnlinePlayer : wrap.onlinePlayers){
							((Task)wrap.u).markUnnotified(affectedOnlinePlayer);
							Main.proxy.NETWORK.sendTo(message, affectedOnlinePlayer);
						}
						for(UUID affectedOfflinePlayer : wrap.offlinePlayers){
							((Task)wrap.u).markUnnotified(affectedOfflinePlayer);
						}
						break;
						
					/*
					 * 
					 * 
					 * PACKET SENDER ONLY
					 * 
					 * 
					 */
						case QUEST_COLLECT:
							wrap = ensureValidType(b, ctx, QUEST, false, false);
							((Quest)wrap.u).collect(ctx.getServerHandler().player);
							return null;
					/*
					 * 
					 * 
					 * INVALID
					 * 
					 * 
					 */
					
					
					case REWARD_COLLECT: //cannot collect rewards because the player may not always be online
					case FINAL_DEATH: //Server only packet, the server is not a player and cannot die
					case LIFE_MAXIMUM: //Server only packet, clients shouldn't set life totals
					case LIFE_STARTING_TOTAL: //Server only packet, clients shouldn't set life totals
					case LIFE_TOTAL: //Server only packet, clients shouldn't set life totals
						throw new PacketException("Illegal Packet received, client should not have sent packet " + message.getType());
					default:
						throw new PacketException("Unknown packet received (" + message.getType() + ')');
				}
			}
			else if (message.getType() == PacketType.QUEST_COLLECT) {
				wrap = ensureValidType(b, ctx, QUEST, false, false);
				((Quest)wrap.u).collect(ctx.getServerHandler().player);
				return null;
			}
			else {
				throw new PacketException("Unauthorized packet received, not enough permission (" + message.getType() + ')');
			}
		}
		catch(PacketException e) {
			if(e.getCause() != null) {
				ctx.getServerHandler().disconnect(new TextComponentString(e.getCause().toString()));
			}
			else {
				ctx.getServerHandler().disconnect(new TextComponentString(e.toString()));
			}
		}
		return null;
	}

	private void sendToAll(GenericQuestingPacket m) {
		Main.proxy.NETWORK.sendToAll(m);
	}
	
	private TypeWrapper ensureValidType(ByteBuf b, MessageContext ctx, Type type, boolean isNew, boolean hasPlayers) throws PacketException {
		return typeObject.ensureValidType(b, ctx, type, isNew, hasPlayers);
	}
	
	protected static enum Type{
		PAGE,
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
		ArrayList<EntityPlayerMP> onlinePlayers;
		ArrayList<UUID> offlinePlayers;
		
		public TypeWrapper ensureValidType(ByteBuf b, MessageContext ctx, Type type, boolean isNew, boolean hasPlayers) throws PacketException{
			NBTTagCompound nbt = ByteBufUtils.readTag(b);
			EntityPlayer p = ctx.getServerHandler().player;
			String erroredClass = null;
			try {
				if(nbt != null) {
					this.nbt = nbt;
					if(isNew) {
						switch(type) {
						case PAGE:
							Page page = new Page(nbt);
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
							case PAGE:
								u = Main.proxy.getGlobalQuestHandler().getPage(id);
								break;
							case QUEST:
								u = Main.proxy.getGlobalQuestHandler().getQuest(id);
								break;
							case REWARD:
								u = Main.proxy.getGlobalQuestHandler().getReward(id);
								break;
							case TASK:
								u = Main.proxy.getGlobalQuestHandler().getTask(id);
								break;
							default:
								throw new AssertionError(); //should never happen, do not catch
						}
						if(u != null) {
							this.t = type;
							this.u = u;
							if(hasPlayers) {
								for(NBTBase base : nbt.getTagList("PLAYERS", 8)) {
									UUID playerID = UUID.fromString(((NBTTagString)base).getString());
									EntityPlayer player = Main.proxy.getPlayerQuestHandler().getPlayerEntityFromUUID(playerID);
									if(p != null) {
										onlinePlayers.add((EntityPlayerMP) player);
									}
									else if (Main.proxy.getPlayerQuestHandler().hasQuestNBT(playerID) && Main.proxy.getPlayerQuestHandler().hasQuestNBT(id, playerID)) { //should only get players that have quest data (players that have logged in before)
										offlinePlayers.add(playerID);
									}
								}
							}
							return this;
						}
						else {
							throw new NonExistantKeyException("Cannot update " + ("" + type).toLowerCase() + " that doesn't exist (" + id + ")");
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