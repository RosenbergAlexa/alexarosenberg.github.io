/*
 * Class: RescueAnimal.java
 * Project: CS-499 Capstone
 * Purpose: Define the skeleton of what is known about a rescue animal in the Grazioso Salvare inventory, and what
 *   it should be able to do
 *   
 * Version History:
 * - 2.0.0     21 May 2026     ARosenberg     Class re-written to be abstract, data types for age, weight, and acquisitionDate updated
 * 
 */

package com.snhu.capstone.model.animal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.*;
import com.snhu.capstone.model.AnimalType;
import com.snhu.capstone.model.Gender;


public abstract class RescueAnimal {

    // Instance variables
    private final StringProperty name = new SimpleStringProperty( this, "name" );
    private final ObjectProperty< AnimalType > animalType = new SimpleObjectProperty<>( this, "animalType" );
    private final ObjectProperty< Gender > gender = new SimpleObjectProperty<>( this, "gender" );
    private final IntegerProperty age = new SimpleIntegerProperty( this, "age", 0 );
    private final FloatProperty weight = new SimpleFloatProperty( this, "weight", 0.0f );
    private final ObjectProperty< LocalDate > acquisitionDate = new SimpleObjectProperty<>( this, "acquisitionDate" );
    private final StringProperty acquisitionCountry = new SimpleStringProperty( this, "acquisitionCountry" );
	private final StringProperty trainingStatus = new SimpleStringProperty( this, "trainingStatus" );
    private final BooleanProperty reserved = new SimpleBooleanProperty( this, "reserved", false );
	private final StringProperty inServiceCountry = new SimpleStringProperty( this, "inServiceCountry" );
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );


    // Constructor
    protected RescueAnimal() {
    }

    //Property getter, standard getter, standard setter for each property
    
    //Name
    public StringProperty nameProperty() { return this.name; }
    public String getName() { return name.get(); }
    public void setName( String name ) { this.name.set( name ); }

    //Animal Type
	public ObjectProperty< AnimalType > animalTypeProperty(){ return this.animalType; }
	public AnimalType getAnimalType() { return animalType.get(); }
	public void setAnimalType( AnimalType type ) { this.animalType.set( type ); }

	//Gender
	public ObjectProperty< Gender > genderProperty(){ return this.gender; }
	public String getGender() { return gender.get() != null ? gender.get().toString() : null; }
	public void setGender( Gender gender ) { this.gender.set( gender ); }

	//Age
	public IntegerProperty ageProperty() { return this.age; }
	public int getAge() { return age.get(); }
	public void setAge( int age ) { this.age.set( age ); }
	
	//Weight
	public FloatProperty weightProperty() { return this.weight; }
	public float getWeight() { return weight.get(); }
	public void setWeight( float weight ) { this.weight.set( weight ); }

	//AcquisitionDate
	public ObjectProperty< LocalDate > acquisitionDateProperty(){ return this.acquisitionDate; }
	public String getAcquisitionDate() { return acquisitionDate.get() != null ? acquisitionDate.get().format( formatter ) : ""; }
	public void setAcquisitionDate( LocalDate date ) { this.acquisitionDate.set( date ); }

	//AcquisitionLocation
	public StringProperty acquisitionLocationProperty() { return this.acquisitionCountry; }
	public String getAcquisitionLocation() { return acquisitionCountry.get(); }
	public void setAcquisitionLocation( String acquisitionCountry ) { this.acquisitionCountry.set( acquisitionCountry ); }

	//Reserved
	public BooleanProperty reservedProperty() { return this.reserved; }
	public boolean getReserved() { return reserved.get(); }
	public void setReserved( boolean reserved ) { this.reserved.set( reserved ); }
	
	//InServiceLocation
	public StringProperty inServiceCountryProperty() { return this.inServiceCountry; }
	public String getInServiceCountry() { return inServiceCountry.get(); }
	public void setInServiceCountry( String inServiceCountry ) { this.inServiceCountry.set( inServiceCountry ); }

	//TrainingStatus
	public StringProperty trainingStatusProperty() { return this.trainingStatus; }
	public String getTrainingStatus() { return trainingStatus.get(); }
	public void setTrainingStatus( String trainingStatus ) { this.trainingStatus.set( trainingStatus ); }
	
	public abstract String getAnimalSubtype();
	public abstract StringProperty animalSubtypeProperty();
}