package com.snhu.capstone.model;

public class Monkey extends RescueAnimal {
	//Class fields
	private String species;
	private String height;
	private String tailLength;
	private String bodyLength;
	
	//Define constructor
	public Monkey( String monkeyName, String monkeyGender, String monkeyAge,
			String monkeyWeight, String acquiredDate, String acquiredFromLocation, String trainingState, 
			boolean reservationStatus, String serviceCountry, String monkeySpecies, String monkeyHeight, String monkeyTailLen,
			String monkeyBodyLen ) {
		//Set inherited field values
		setName( monkeyName );
		setAnimalType( "Monkey" );
		setGender( monkeyGender );
		setAge( monkeyAge );
		setWeight( monkeyWeight );
		setAcquisitionDate( acquiredDate );
		setAcquisitionLocation( acquiredFromLocation );
		setTrainingStatus( trainingState );
		setReserved( reservationStatus );
		setInServiceCountry( serviceCountry );
		
		//Set monkey-specific field values
		setSpecies( monkeySpecies );
		setHeight( monkeyHeight );
		setTailLength( monkeyTailLen );
		setBodyLength( monkeyBodyLen );
	}
	
	//Define accessor methods
	public void setSpecies( String monkeySpecies ) {
		this.species = monkeySpecies;
	}
	
	public String getSpecies() {
		return this.species;
	}
	
	public void setHeight( String monkeyHeight ) {
		this.height = monkeyHeight;
	}
	
	public String getHeight() {
		return this.height;
	}
	
	public void setTailLength( String monkeyTailLen ) {
		this.tailLength = monkeyTailLen;
	}
	
	public String getTailLength() {
		return this.tailLength;
	}
	
	public void setBodyLength( String monkeyBodyLen ) {
		this.bodyLength = monkeyBodyLen;
	}
	
	public String getBodyLength() {
		return this.bodyLength;
	}
	
	//Print the monkey
	public void printMonkey() {
		
		//Print summary of monkey information
		System.out.printf( "%s is a %s %s aged %s and %s lbs. They are %s mm tall with a body length of %s and tail length of %s. Acquired from %s"
				+ " on %s, with service country %s. Current training status is %s.",
				getName(), getGender(), this.species, getAge(), getWeight(), this.height, this.bodyLength, this.tailLength,
				getAcquisitionLocation(), getAcquisitionDate(), getInServiceLocation(), getTrainingStatus() );
		
		//Print reservation status of monkey
		if( getReserved() ) {
			System.out.println( " Currently reserved" );
		}
		else {
			System.out.println( " Currently not reserved" );
		}
		
	}

}
