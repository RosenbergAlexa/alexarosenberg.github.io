/*
 * Class: InvalidUserException.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a custom exception which a class/method may define and throw. Intended use case is to represent instances in which the 
 *    validation of a user attempting to log into the system cannot be verified. The message field should be used to provide sanitized reasoning
 *    for the failure that can be displayed to the user
 *    
 * Version History:
 * - 1.0.0     03 June 2026     ARosenberg     Exception defined
 */

package com.snhu.capstone.model.exceptions;

public class InvalidUserException extends Exception{

	/**
	 * Required for classes that implement the Serializable interface (inherited from Exception)
	 */
	private static final long serialVersionUID = 7962547947104377477L;
	
	public InvalidUserException( String message ) {
		
		super( message );
		
	}
	
	public InvalidUserException( String message, Throwable cause ) {
		
		super( message, cause );
		
	}

}
