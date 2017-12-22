package com.gamebuster19901.superiorquesting.common.questing.exception;

public class ShapeOutOfBoundsException extends IllegalShapeException{

	public ShapeOutOfBoundsException(Throwable cause) {
		super(cause);
	}

	public ShapeOutOfBoundsException(String message) {
		super(message);
	}

	public ShapeOutOfBoundsException(String message, Throwable cause) {
		super(message, cause);
	}

}
