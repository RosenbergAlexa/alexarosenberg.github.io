/*
 * Class: ComponentConfig.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a unifying configuration container for all possible fields used to describe a single RescueAnimal subclass.
 *    
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * 
 */

package com.snhu.capstone.model.animal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.snhu.capstone.model.AnimalType;
import com.snhu.capstone.model.Gender;
import com.snhu.capstone.model.exceptions.InvalidAnimalDataException;

public class ComponentConfig {
	
    private String name;
    private AnimalType animalType;
    private Gender gender;
    private int age;
    private float weight;
    private LocalDate acquisitionDate;
    private String acquisitionCountry;
	private String trainingStatus;
    private boolean reserved;
	private String inServiceCountry;
	private String breed;
	private String species;
	private float height;
	private float tailLength;
	private float bodyLength;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
	
	/**
	 * Provide a constructor to set values for all the fields utilized by the Dog class
	 */
	public ComponentConfig( String name, String type, String gender, int age, 
			float weight, String gotchaDate, String originCountry, String trainingStatus, boolean reserved,
			String serviceCountry, String breed ) throws InvalidAnimalDataException {
		
		this.name = name;
		try { //Set the config animal type. If unable to determine type, throw an exception so the user can be warned

			this.animalType = AnimalType.valueOf( type.toUpperCase() );
			
		}catch( IllegalArgumentException | NullPointerException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal type" );
			
		}
		try{ //Set the config gender. If unable to determine gender, throw an exception so the user can be warned
			
			this.gender = Gender.valueOf( gender.toUpperCase() );
			
		}catch( IllegalArgumentException | NullPointerException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal gender" );
			
		}
		this.age = age;
		this.weight = weight;
		try{ //Set the animal's acquisition date. If it cannot be determined, throw an exception so the user can be warned
			
			this.acquisitionDate = LocalDate.parse(  gotchaDate, formatter );
			
		}catch( DateTimeParseException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal acquisition date format" );
			
		}
		this.acquisitionCountry = originCountry;
		this.trainingStatus = trainingStatus;
		this.reserved = reserved;
		this.inServiceCountry = serviceCountry;
		this.breed = breed;
		
	}
	
	/**
	 * Provide a constructor to set values for all the fields utilized by the Monkey class
	 */
	public ComponentConfig( String name, String type, String gender, int age, 
			float weight, String gotchaDate, String originCountry, String trainingStatus, boolean reserved,
			String serviceCountry, String species, float height, float tailLength, 
			float bodyLength ) throws InvalidAnimalDataException {
		
		this.name = name;
		try { //Set the config animal type. If unable to determine type, throw an exception so the user can be warned
			
			this.animalType = AnimalType.valueOf( type.toUpperCase() );
			
		}catch( IllegalArgumentException | NullPointerException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal type" );
			
		}
		try{ //Set the config gender. If unable to determine gender, throw an exception so the user can be warned
			
			this.gender = Gender.valueOf( gender.toUpperCase() );
			
		}catch( IllegalArgumentException | NullPointerException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal gender" );
			
		}
		this.age = age;
		this.weight = weight;
		try{ //Set the animal's acquisition date. If it cannot be determined, throw an exception so the user can be warned
			
			this.acquisitionDate = LocalDate.parse(  gotchaDate, formatter );
			
		}catch( DateTimeParseException e ) {
			
			throw new InvalidAnimalDataException( "Invalid or unknown animal acquisition date format" );
			
		}
		this.acquisitionCountry = originCountry;
		this.trainingStatus = trainingStatus;
		this.reserved = reserved;
		this.inServiceCountry = serviceCountry;
		this.species = species;
		this.height = height;
		this.tailLength = tailLength;
		this.bodyLength = bodyLength;
		
	}

	//Define config setters
	
	public String getName(){
		return name;
	}

	public AnimalType getAnimalType(){
		return animalType;
	}

	public Gender getGender(){
		return gender;
	}

	public int getAge(){
		return age;
	}

	public float getWeight(){
		return weight;
	}

	public LocalDate getAcquisitionDate(){
		return acquisitionDate;
	}

	public String getAcquisitionCountry(){
		return acquisitionCountry;
	}

	public String getTrainingStatus(){
		return trainingStatus;
	}

	public boolean getReserved(){
		return reserved;
	}

	public String getInServiceCountry(){
		return inServiceCountry;
	}

	public String getSpecies(){
		return species;
	}

	public float getHeight(){
		return height;
	}

	public float getTailLength(){
		return tailLength;
	}

	public float getBodyLength(){
		return bodyLength;
	}

	public String getBreed(){
		return breed;
	}

}
