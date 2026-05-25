package com.snhu.capstone.model;

public enum AnimalType{
	
	MONKEY("Monkey"),
	DOG("Dog");
	
	private String displayName;
	
	AnimalType( String displayName ) {	
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
