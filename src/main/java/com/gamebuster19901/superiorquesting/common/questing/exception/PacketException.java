package com.gamebuster19901.superiorquesting.common.questing.exception;

public class PacketException extends Exception{
	public PacketException(Throwable cause) {
		super(cause);
	}

	public PacketException(String message) {
		super(message);
	}

	public PacketException(String message, Throwable cause) {
		super(message, cause);
	}
}
