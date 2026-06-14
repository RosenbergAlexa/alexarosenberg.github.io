
/*
 * Class: TrainingStatus.java
 * Project: CS-499 Capstone
 * Purpose: Defines the allowed animal training statuses
 * 
 * Version History:
 * - 1.0.0     27 May 2026     ARosenberg     Enum defined
 */
package com.snhu.capstone.model;

public enum TrainingStatus{
	
	INTAKE( "Intake" ),
	PHASEI( "Phase I" ),
	PHASEII( "Phase II" ),
	PHASEIII( "Phase III" ),
	PHASEIV( "Phase IV" ),
	PHASEV( "Phase V" ),
	INSERVICE( "In-Service" ),
	RETIRED( "Retired" );
	
	private String displayName;
	
	TrainingStatus( String displayName ){
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public String toString() {
		return this.displayName;
	}
	
	/**
	 * Define how to translate a text version of one of the training status values into its matching enum value
	 * @param text
	 * @return
	 */
	public static TrainingStatus fromDisplayName( String text ) {
		
		if( text == null ) {
			return null;
		}
		
		for( TrainingStatus status : TrainingStatus.values() ) {
			if( status.getDisplayName().equalsIgnoreCase( text.trim() ) ) {
				return status;
			}
		}
		throw new IllegalArgumentException( "No matching TrainingStatus constant found for " + text );		
	}

}
