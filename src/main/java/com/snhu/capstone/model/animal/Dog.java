/*
 * Class: Dog.java
 * Project: CS-499 Capstone
 * Purpose: Define the identity and actions that can be taken with a Dog rescue animal 
 * 
 * Version History:
 * - 2.0.0     21 May 2026     ARosenberg     Class rewritten
 * 
 */

package com.snhu.capstone.model.animal;
import javafx.beans.property.*;

public class Dog extends RescueAnimal {

    // Instance variable
    private final StringProperty breed = new SimpleStringProperty( this, "breed" );
    
    /*
     * Method: Dog
     * Purpose: Acts as the constructor for a Dog object. The required ComponentConfig is created and provided by the 
     *    associated factory class
     */
    public Dog( ComponentConfig config ) {
    	
    	this.setName( config.getName() );
    	this.setAnimalType( config.getAnimalType() );
    	this.setBreed( config.getBreed() );
    	this.setGender( config.getGender() );
    	this.setAge( config.getAge() );
    	this.setWeight( config.getWeight() );
    	this.setAcquisitionDate( config.getAcquisitionDate() );
    	this.setAcquisitionLocation( config.getAcquisitionCountry() );
    	this.setTrainingStatus( config.getTrainingStatus() );
    	this.setReserved( config.getReserved() );
    	this.setInServiceCountry( config.getInServiceCountry() );
    	
    }

    //Define accessor methods
    //breed
    public StringProperty breedProperty() { return this.breed; }
    public String getBreed() { return breed.get(); }
    public void setBreed( String breed ) { this.breed.set( breed ); }
    
    @Override
    public String getAnimalSubtype() { return getBreed(); }
    
    @Override
    public StringProperty animalSubtypeProperty() { return this.breed; }

}
