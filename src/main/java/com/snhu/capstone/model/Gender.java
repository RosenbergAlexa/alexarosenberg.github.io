/*
 * Enum: Gender.java
 * Project: CS-499 Capstone
 * Purpose: Defines the allowed animal gender types
 * 
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Enum defined
 * 
 */

package com.snhu.capstone.model;

public enum Gender{

	MALE( "Male" ),
	FEMALE( "Female" );
	
	private String displayName;
	
	Gender( String displayName ){
		this.displayName = displayName;
	}
	
	public String getDisplayName() { 
		return this.displayName; 
	}
	
	@Override
	public String toString() {
		return this.displayName;
	}
	
}
