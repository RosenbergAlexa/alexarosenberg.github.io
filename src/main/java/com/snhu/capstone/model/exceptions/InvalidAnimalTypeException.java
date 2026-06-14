/*
 * Class: InvalidAnimalTypeException.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a custom exception which a class/method may define and throw. Intended use case is to represent instances in which an issue is 
 *    encountered while attempting to register new Animal Type data to the remote back-end database. The message field should be used to provide sanitized 
 *    reasoning for the failure that can be displayed to the user
 *    
 * Version History:
 * - 1.0.0     06 Jun 2026     ARosenberg     Exception defined
 */

package com.snhu.capstone.model.exceptions;

public class InvalidAnimalTypeException extends Exception{

	/**
	 * Required for classes that implement the Serializable interface (inherited from Exception)
	 */
	private static final long serialVersionUID = -4487855909336819381L;
	
	public InvalidAnimalTypeException( String message ) {
		
		super( message );
		
	}
	
	public InvalidAnimalTypeException( String message, Throwable cause ) {
		
		super( message, cause );
		
	}

}
