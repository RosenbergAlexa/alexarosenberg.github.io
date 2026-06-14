/*
 * Class: UnknownAnimalException.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a custom exception which a class/method may define and throw. Intended use case is to represent instances in which the attempt 
 *    to query information about an animal from the back-end database failed for some reason
 *    
 * Version History:
 * - 1.0.0     04 Jun 2026     ARosenberg     Exception defined
 */

package com.snhu.capstone.model.exceptions;

public class UnknownAnimalException extends Exception{

	/**
	 * Required for classes that implement the Serializable interface (inherited from Exception)
	 */
	private static final long serialVersionUID = 7293494483044947196L;
	
	public UnknownAnimalException( String message ) {
		
		super( message );
		
	}
	
	public UnknownAnimalException( String message, Throwable cause ) {
		
		super( message, cause );
		
	}
	

}
