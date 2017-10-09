package com.gamebuster19901.superiorquesting.common.questing;

import java.util.ArrayList;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;

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
	 */
	public static final void registerTaskType(Class<? extends Task> task, ResourceLocation image, String name, String description) {
		for(TaskType r : taskTypes) {
			if(r.task.equals(task)) {
				throw new DuplicateKeyException("Cannot register a task type more than once: " + task.getName());
			}
			if(r.task.equals(Task.class)) {
				throw new IllegalArgumentException("YOU'RE AN IDIOT, DON'T DO THAT");
			}
		}
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
}
