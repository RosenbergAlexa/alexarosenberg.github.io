/*
 * Class: User.java
 * Project: CS-499 Capstone
 * Purpose: Defines the identifying information and behavior of a single authenticated user
 * 
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * 
 */

package com.snhu.capstone.model.user;

import com.snhu.capstone.model.Role;

public class User{
	
	private String firstName;
	private String lastName;
	private String email;
	private Role role;
	
	
	public User( String fName, String lName, String email, String role ) {
		
		this.firstName = fName;
		this.lastName = lName;
		this.email = email;
		try{ this.role = Role.fromString( role ); }
		catch( IllegalArgumentException e ){
			throw e;
		}
		
	}
	
	public String getFirstName() {
		
		return this.firstName;
		
	}
	
	public String getLastName() {
		
		return this.lastName;
		
	}
	
	public String getEmail() {
		
		return this.email;
		
	}
	
	/**
	 * return whether or not the user has system admin rights
	 */
	public boolean isAdmin() {
		
		if( this.role == Role.ADMIN ) { return true; }
		else { return false; }
		
	}
	
	/**
	 * return whether or not the user has system user rights
	 */
	public boolean isUser() {
		
		if( this.role == Role.USER || this.role == Role.ADMIN ) { return true; }
		else { return false; }
		
	}

}
