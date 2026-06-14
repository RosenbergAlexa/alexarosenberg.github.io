/*
 * Class: Role.java
 * Project: CS-499 Capstone
 * Purpose: Defines the possible categories of user authorization levels allowed by the system
 * 
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Enum defined
 * 
 */

package com.snhu.capstone.model;

public enum Role{
	
	ADMIN( "Administrator" ),
	USER( "User" ),
	GUEST( "Guest" );
	
	private String displayName;
	
	Role( String displayName ) {	
		this.displayName = displayName;	
	}

	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public String toString() {
		return this.displayName;
	}
	
	public static Role fromString( String text ) {
		
		for( Role role : Role.values() ) {
			if( role.displayName.equalsIgnoreCase( text ) ||
				role.name().equalsIgnoreCase( text ) ) {
				return role;
			}
		}
	
		throw new IllegalArgumentException( "No role found for: " + text );
	}
	
}
