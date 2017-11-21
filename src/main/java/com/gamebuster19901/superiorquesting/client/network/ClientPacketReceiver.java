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
import com.gamebuster19901.superiorquesting.common.IngameDebuggable;
import com.gamebuster19901.superiorquesting.common.NBTDebugger;
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

public class ClientPacketReceiver<Message extends GenericQuestingPacket> implements IMessageHandler<Message, IMessage>, Assertable, IngameDebuggable, NBTDebugger{
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
					proxy.getLifeHandler().setLives(p, life, false);
					debug(p, "New life total received from server: " + life);
					return null;
				case LIFE_MAXIMUM:
					message.toBytes(b);
					double maxLife = b.getDouble(0);
					proxy.getLifeHandler().setMaxLives(maxLife);
					debug(p, "New max life total received from server:" + maxLife);
					return null;
				case LIFE_STARTING_TOTAL:
					message.toBytes(b);
					double startingLife = b.getDouble(0);
					proxy.getLifeHandler().setStartingLives(startingLife);
					debug(p, "New starting life total received from server:" + startingLife);
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
					debug(p, "New quest recieved from server:" + (Quest)wrap.u);
					break;
				case NEW_TASK:
					wrap = ensureValidType(b, ctx, TASK, true);
					debug(p, "New task recieved from server:" + (Task)wrap.u);
					break;
				case NEW_REWARD:
					wrap = ensureValidType(b, ctx, REWARD, true);
					debug(p, "New reward received from server:" + (Reward)wrap.u);
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
					Quest q = ((Quest)wrap.u);
					q.deserializeNBT(wrap.nbt);
					debug(p, "Quest data update recieved " + q);
					break;
				case UPDATE_TASK:
					wrap = ensureValidType(b, ctx, TASK, false);
					Task t = ((Task)wrap.u);
					t.deserializeNBT(wrap.nbt);
					debug(p, "Task data update received " + t);
					break;
				case UPDATE_REWARD:
					wrap = ensureValidType(b, ctx, REWARD, false);
					Reward r = ((Reward)wrap.u);
					r.deserializeNBT(wrap.nbt);
					debug(p, "Reward data update recieved " + r);
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
					debug(p, "Server removed quest " + wrap.u);
					break;
				case REMOVE_TASK:
					wrap = ensureValidType(b, ctx, TASK, false);
					Main.proxy.getGlobalQuestHandler().removeTask(wrap.u.getUUID());
					debug(p, "Server removed task " + wrap.u);
					break;
				case REMOVE_REWARD:
					wrap = ensureValidType(b, ctx, TASK, false);
					Main.proxy.getGlobalQuestHandler().removeReward(wrap.u.getUUID());
					debug(p, "Server removed reward " + wrap.u);
					break;
					
				/*
				 * 
				 * 
				 * PLAYER QUEST DATA
				 * 
				 * 
				 */
					
				case QUEST_FINISH:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).finish(p);
					debug(p, "Server said you completed quest" + wrap.u);
					return null;
				case QUEST_UNFINISH:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).markUnfinished(p);
					debug(p, "Server says to mark the following quest as unfinished " + wrap.u);
					return null;
				case QUEST_COLLECT:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).collect(p);
					debug(p, "Server directs client to collect rewards from quest" + wrap.u);
					return null;
				case QUEST_NOTIFY:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).notify();
					debug(p, "Server notifies you of quest " + wrap.u);
					return null;
				case QUEST_UNNOTIFY:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).notify();
					debug(p, "Server says to mark the following quest as unnotifed " + wrap.u);
					return null;
				case QUEST_LOCK:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).lock(p);
					debug(p, "Server locked quest " + wrap.u);
					return null;
				case QUEST_UNLOCK:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).unlock(p);
					debug(p, "Server unlocked quest " + wrap.u);
					return null;
				case QUEST_HIDE:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).hide(p);
					debug(p, "Server hid quest " + wrap.u);
					return null;
				case QUEST_UNHIDE:
					wrap = ensureValidType(b, ctx, QUEST, false);
					((Quest)wrap.u).unhide(p);
					debug(p, "Server unhid quest " + wrap.u);
					return null;
					
				/*
				 * 
				 * 
				 * PLAYER TASK DATA 
				 * 
				 * 
				 */
				case TASK_FINISH:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).finish(p);
					debug(p, "Server said you finished task " + wrap.u);
					return null;
					
				case TASK_UNFINISH:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).markUnfinished(p);
					debug(p, "Server said to mark the following task as unfinished " + wrap.u);
					return null;
				case TASK_NOTIFY:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).notify();
					debug(p, "Server notified you of trask " + wrap.u);
					return null;
					
				case TASK_UNNOTIFY:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).markUnnotified(p);
					debug(p, "Server says to mark the following task as unnotified " + wrap.u);
					return null;
				case TASK_LOCK:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).lock(p);
					debug(p, "Server locked task " + wrap.u);
					return null;
				case TASK_UNLOCK:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).unlock(p);
					debug(p, "Server unlocked task " + wrap.u);
					return null;
				case TASK_HIDE:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).hide(p);
					debug(p, "Server hid task " + wrap.u);
					return null;
				case TASK_UNHIDE:
					wrap = ensureValidType(b, ctx, TASK, false);
					((Task)wrap.u).unhide(p);
					debug(p, "Server unhid task " + wrap.u);
					return null;
				
				/*
				 *
				 * 
				 * PLAYER REWARD DATA
				 * 
				 * 
				 */
					
				case REWARD_COLLECT:
					wrap = ensureValidType(b, ctx, REWARD, false);
					((Reward)wrap.u).collect(p);
					debug(p, "Server said to collect reward " + wrap.u);
					return null;
				case REWARD_UNCOLLECT:
					wrap = ensureValidType(b, ctx, REWARD, false);
					((Reward)wrap.u).collect(p);
					debug(p, "Server said to mark the folowing reward as uncollected " + wrap.u);
					return null;
					
				/*
				 * 
				 * 
				 * DEATH
				 * 
				 * 
				 */
					
				case FINAL_DEATH:
					b = Unpooled.buffer();
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
								u = new Quest(nbt);
								break;
							case TASK:
								try {
									Class<? extends Task> clazz;
									clazz = (Class<? extends Task>) Class.forName(nbt.getString("CLASS"));
									erroredClass = clazz.getCanonicalName();
									Constructor c = clazz.getConstructor(NBTTagCompound.class);
									u = (Task) c.newInstance(nbt);
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
									u = (Reward)c.newInstance(nbt);
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
