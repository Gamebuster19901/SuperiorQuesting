package com.gamebuster19901.superiorquesting.common.questing.exception;

public class NonExistantKeyException extends IllegalStateException{
	public NonExistantKeyException(Throwable cause) {
		super(cause);
	}

	public NonExistantKeyException(String message) {
		super(message);
	}

	public NonExistantKeyException(String message, Throwable cause) {
		super(message, cause);
	}
}
