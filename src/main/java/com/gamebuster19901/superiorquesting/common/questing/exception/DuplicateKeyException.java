package com.gamebuster19901.superiorquesting.common.questing.exception;

public class DuplicateKeyException extends IllegalStateException{
	public DuplicateKeyException(Throwable cause){
		super(cause);
	}
	
	public DuplicateKeyException(String message){
		super(message);
	}
	
	public DuplicateKeyException(String message, Throwable cause){
		super(message, cause);
	}
}
