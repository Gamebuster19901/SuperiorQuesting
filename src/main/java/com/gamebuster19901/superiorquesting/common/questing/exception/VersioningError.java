package com.gamebuster19901.superiorquesting.common.questing.exception;
/**
 * Thrown to indicate that there was some issue or discrepancy when updating the questing config
 */
public class VersioningError extends Error {
	public VersioningError(Throwable cause){
		super(cause);
	}
	
	public VersioningError(String message){
		super(message);
	}
	
	public VersioningError(String message, Throwable cause){
		super(message, cause);
	}
}
