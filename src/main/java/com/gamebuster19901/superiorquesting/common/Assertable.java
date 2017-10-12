package com.gamebuster19901.superiorquesting.common;

public interface Assertable {
	/**
	 * This interface's methods are capitalized because of java's assert keyword
	 */
	
	/**
	 * throws an AssertionError if the assertion is false
	 * @param assertion
	 * @throws AssertionError
	 * @return true
	 */
	public default boolean Assert(boolean assertion) throws AssertionError{
		if(!assertion){
			throw new AssertionError();
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified message if the assertion is false
	 * @param assertion
	 * @throws AssertionError
	 * @return true
	 */
	public default boolean Assert(boolean assertion, String failedMessage) throws AssertionError{
		if(!assertion){
			throw new AssertionError(failedMessage);
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified cause if the assertion is false
	 * @param assertion
	 * @param cause, may be null
	 * @throws AssertionError
	 * @return true
	 */
	public default boolean Assert(boolean assertion, Throwable cause) throws AssertionError{
		if(!assertion){
			throw new AssertionError(cause);
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified message and cause if the assertion is false
	 * @param assertion
	 * @param failedMessage
	 * @param cause, may be null
	 * @throws AssertionError
	 * @return true
	 */
	public default boolean Assert(boolean assertion, String failedMessage, Throwable cause) throws AssertionError{
		if(!assertion){
			throw new AssertionError(failedMessage, cause);
		}
		return true;
	}
	
	/**
	 * throws an AssertionError if the assertion is false
	 * @param assertion
	 * @throws AssertionError
	 * @return true
	 */
	public static boolean Assert(boolean assertion, Object unused) throws AssertionError{
		if(!assertion){
			throw new AssertionError();
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified message if the assertion is false
	 * @param assertion
	 * @throws AssertionError
	 * @return true
	 */
	public static boolean Assert(boolean assertion, String failedMessage, Object unused) throws AssertionError{
		if(!assertion){
			throw new AssertionError(failedMessage);
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified cause if the assertion is false
	 * @param assertion
	 * @param cause, may be null
	 * @throws AssertionError
	 * @return true
	 */
	public static boolean Assert(boolean assertion, Throwable cause, Object unused) throws AssertionError{
		if(!assertion){
			throw new AssertionError(cause);
		}
		return true;
	}
	
	/**
	 * throws an AssertionError with the specified message and cause if the assertion is false
	 * @param assertion
	 * @param failedMessage
	 * @param cause, may be null
	 * @throws AssertionError
	 * @return true
	 */
	public static boolean Assert(boolean assertion, String failedMessage, Throwable cause, Object unused) throws AssertionError{
		if(!assertion){
			throw new AssertionError(failedMessage, cause);
		}
		return true;
	}
}
