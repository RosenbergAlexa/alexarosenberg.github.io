/*
 * Class: Monkey.java
 * Project: CS-499 Capstone
 * Purpose: Define the identity and actions that can be taken with a Monkey rescue animal 
 * 
 * Version History:
 * - 2.0.0     21 May 2026     ARosenberg     Class rewritten
 * 
 */

package com.snhu.capstone.model.animal;

import javafx.beans.property.*;

public class Monkey extends RescueAnimal {
	
	//Class fields
	private final StringProperty species = new SimpleStringProperty( this, "species" );
	private final FloatProperty height = new SimpleFloatProperty( this, "height", 0.0f );
	private final FloatProperty tailLength = new SimpleFloatProperty( this, "tailLength", 0.0f );
	private final FloatProperty bodyLength = new SimpleFloatProperty( this, "bodyLength", 0.0f );
	
	/*
	 * Method: Monkey
	 * Purpose: Acts as the constructor for a Monkey object. The required ComponentConfig is created and provided by the 
	 *    associated factory class
	 */
	public Monkey( ComponentConfig config ) {
		
		this.setName( config.getName() );
		this.setAnimalType( config.getAnimalType() );
		this.setGender( config.getGender() );
		this.setAge( config.getAge() );
		this.setWeight( config.getWeight() );
		this.setAcquisitionDate( config.getAcquisitionDate() );
		this.setAcquisitionLocation( config.getAcquisitionCountry() );
		this.setTrainingStatus( config.getTrainingStatus() );
		this.setReserved( config.getReserved() );
		this.setInServiceCountry( config.getInServiceCountry() );
		this.setSpecies( config.getSpecies() );
		this.setHeight( config.getHeight() );
		this.setTailLength( config.getTailLength() );
		this.setBodyLength( config.getBodyLength() );
		
	}
	
	//Define accessor methods
	//Species
	public StringProperty speciesProperty() { return this.species; }
	public String getSpecies() { return species.get(); }
	public void setSpecies( String species ) { this.species.set( species ); }
	
	//height
	public FloatProperty heightProperty() { return this.height; }
	public float getHeight() { return height.get(); }
	public void setHeight( float height ) { this.height.set( height ); }
	
	//TailLength
	public FloatProperty tailLengthProperty() { return this.tailLength; }
	public float getTailLength() { return tailLength.get(); }
	public void setTailLength( float tailLength ) { this.tailLength.set( tailLength ); }
	
	//BodyLength
	public FloatProperty bodyLengthProperty() { return this.bodyLength; }
	public float getBodyLength() { return bodyLength.get(); }
	public void setBodyLength( float bodyLength ) { this.bodyLength.set( bodyLength ); }

	@Override
	public String getAnimalSubtype() { return getSpecies(); }
	
	@Override
	public StringProperty animalSubtypeProperty() { return this.species; }
	
}
