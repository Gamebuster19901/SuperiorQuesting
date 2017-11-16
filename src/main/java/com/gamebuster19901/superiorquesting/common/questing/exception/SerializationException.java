package com.gamebuster19901.superiorquesting.common.questing.exception;

public class SerializationException extends RuntimeException{
	public SerializationException(Throwable cause) {
		super(cause);
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
