/*
 * Class: InvalidAnimalStatusException
 * Project: CS-499 Capstone
 * Purpose: Acts as a custom exception which a class/method may define and throw. Intended use case is to represent instances in which an error is 
 *     encountered when attempting to match a new animal with its status data in the remote back-end database. 
 *     
 * Version History:
 * - 1.0.0     06 Jun 2026     ARosenberg     Exception defined
 */

package com.snhu.capstone.model.exceptions;

public class InvalidAnimalStatusException extends Exception{

	/**
	 * Required for classes that implement the Serializable interface (inherited from Exception)
	 */
	private static final long serialVersionUID = 8855098672637343685L;
	
	public InvalidAnimalStatusException( String message ) {
		
		super( message );
		
	}
	
	public InvalidAnimalStatusException( String message, Throwable cause ) {
		
		super( message, cause );
		
	}
	
}
