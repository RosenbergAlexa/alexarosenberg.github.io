/*
 * Class: InvalidAnimalDataException.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a custom exception which a class/method may define and throw. Intended use case is to represent instances in which the 
 *    creation of a rescue animal cannot be completed because of invalid data
 *    
 * Version History:
 */
package com.snhu.capstone.model.exceptions;

public class InvalidAnimalDataException extends Exception{
	
	/**
	 * Required for classes that implement the Serializable interface (inherited from Exception)
	 */
	private static final long serialVersionUID = 5232309120962027520L;

	public InvalidAnimalDataException( String message ) {
		
		super( message );
		
	}

	public InvalidAnimalDataException( String message, Throwable cause ) {
		
		super( message, cause );
		
	}
	
}
