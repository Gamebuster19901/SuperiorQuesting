package com.gamebuster19901.superiorquesting.common.questing.exception;

/**
 * Thrown to indicate that the questing config is of a future config version that
 * did not exist when this version of SuperiorQuesting was created
 */

public class FutureVersionError extends VersioningError{
	
	public FutureVersionError(Throwable cause){
		super(cause);
	}
	
	public FutureVersionError(String message){
		super(message);
	}
	
	public FutureVersionError(String message, Throwable cause){
		super(message, cause);
	}
}
