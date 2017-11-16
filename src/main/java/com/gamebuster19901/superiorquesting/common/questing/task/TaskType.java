package com.gamebuster19901.superiorquesting.common.questing.task;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;
import com.gamebuster19901.superiorquesting.common.questing.exception.MalformedTypeError;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public final class TaskType {
	public Class<? extends Task> task;
	public ResourceLocation image;
	public String name;
	public String description;
	
	private TaskType(Class<? extends Task> rew, ResourceLocation loc, String nam, String desc) {
		this.task = rew;
		this.image = loc;
		this.name = nam;
		this.description = desc;
	}
	
	private static final ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
	
	/**
	 * 
	 * @param task the task class to register
	 * @param image the image to use when selecting a task type in the quest editor
	 * @param name the name of the task
	 * @param description the description of the task
	 * @throws DuplicateKeyException if the task is already registered
	 * @throws IllegalArgumentException if task does not extend Task.class
	 * @throws MalformedTypeError if the task to register is not implemented correctly
	 */
	public static final void registerTaskType(Class<? extends Task> task, ResourceLocation image, String name, String description) {
		for(TaskType r : taskTypes) {
			if(r.task.equals(task)) {
				throw new DuplicateKeyException("Cannot register a task type more than once: " + task.getName());
			}
		}
		if(task == Task.class || !(task.isAssignableFrom(Task.class))) {
			throw new IllegalArgumentException(Task.class + " does not extend " + Task.class);
		}
		validateTask(task);
		taskTypes.add(new TaskType(task, image, name, description));
		Main.LOGGER.info("Registered new task type: " + name);
	}
	
	/**
	 * returns the task type of the task if it is registered, null if it isn't
	 * @param task1 the task to get the task type of
	 */
	public static final TaskType getTaskType(Task task1) {
		for(TaskType task2 : taskTypes) {
			if(task1.getClass() == task2.task) {
				return task2;
			}
		}
		return null;
	}
	
	private static final void validateTask(Class<? extends Task> task) {
		try {
			Constructor<?>[] constructors = task.getConstructors();
			Constructor nbtConstructor = task.getConstructor(MinecraftServer.class, NBTTagCompound.class);
			if(constructors.length != 3) {
				throw new MalformedTypeError(task.getName() + " must have 3 constructors");
			}
		}
		catch(NoSuchMethodException e) {
			throw new MalformedTypeError("Missing NBT Constructor", e);
		}
	}	
}

