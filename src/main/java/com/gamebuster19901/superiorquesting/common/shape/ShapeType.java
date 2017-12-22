package com.gamebuster19901.superiorquesting.common.shape;

import java.util.ArrayList;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.questing.exception.DuplicateKeyException;

import scala.actors.threadpool.Arrays;

public class ShapeType {
	public Class<? extends Shape> shape;
	public String name;
	public String description;
	public Class<?>[] specialParams;
	
	private ShapeType(Class<? extends Shape> shape, String name, String description, Class<?>[] parameters) {
		this.shape = shape;
		this.name = name;
		this.description = description;
	}
	
	private static final ArrayList<ShapeType> shapeTypes = new ArrayList<ShapeType>();
	
	public static final void registerShapeType(Class<? extends Shape> shape, String name, String description) {
		registerShapeType(shape, name, description, new Class[]{});
	}
	
	/**
	 * 
	 * @param shape the shape class to register
	 * @param image the image to use when selecting a shape type in the quest editor
	 * @param name the name of the shape
	 * @param description the description of the shape
	 */
	public static final void registerShapeType(Class<? extends Shape> shape, String name, String description, Class<?>... specialParams) {
		for(ShapeType s : shapeTypes) {
			if(s.shape.equals(shape)) {
				throw new DuplicateKeyException("Cannot register a shape type more than once: " + shape.getName());
			}
			if(s.shape.equals(Shape.class)) {
				throw new IllegalArgumentException("YOU'RE AN IDIOT, DON'T DO THAT");
			}
		}
		System.out.println(specialParams.length);
		for(Class c : specialParams) {
			if(c.isPrimitive()) {
				continue;
			}
			throw new IllegalArgumentException(c.getSimpleName() + " is not primitive!");
		}
		shapeTypes.add(new ShapeType(shape, name, description, specialParams));
		if(specialParams.length == 0) {
			Main.LOGGER.info("Registered new shape type: " + name);
		}
		else {
			Main.LOGGER.info("Registered new shape type: " + name + " with special parameters " + Arrays.toString(specialParams));
		}
	}
	
	/**
	 * returns the shape type of the shape if it is registered, null if it isn't
	 * @param shape1 the shape to get the shape type of
	 */
	public static final ShapeType getShapeType(Shape shape1) {
		for(ShapeType shape2 : shapeTypes) {
			if(shape1.getClass() == shape2.shape) {
				return shape2;
			}
		}
		return null;
	}
	
}
