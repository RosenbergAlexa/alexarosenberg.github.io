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
