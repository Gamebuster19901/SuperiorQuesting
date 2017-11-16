package com.gamebuster19901.superiorquesting.common.questing.exception;

/**
 * 
 * Thrown when SuperiorQuesting detects that a class that extends Reward or Task is not implemented correctly
 * 
 */

public class MalformedTypeError extends VerifyError{
	public MalformedTypeError(Throwable cause){
		super();
		initCause(cause);
	}
	
	public MalformedTypeError(String message){
		super(message);
	}
	
	public MalformedTypeError(String message, Throwable cause){
		this(message);
		initCause(cause);
	}
}
