/*
 * Class: AnimalFactory
 * Project: CS-499 Capstone
 * Purpose: The RescueAnimal factory designed to create and return a generic animal object to the caller based off of 
 *    data pulled from the integrated inventory database
 * 
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * 
 */

package com.snhu.capstone.model.animal;

import com.snhu.capstone.model.AnimalType;

public class AnimalFactory{
	
	/**
	 * Create a RescueAnimal object of the correct concrete type based on the data captured by the 
	 *    argument object. The ComponentConfig object should be created based on data stored in the inventory database
	 */
	public static RescueAnimal createAnimal( ComponentConfig config ) {
		
		if( config.getAnimalType() == AnimalType.DOG ) {
			
			return new Dog( config );
			
		}else if( config.getAnimalType() == AnimalType.MONKEY ) {
			
			return new Monkey( config );
			
		}else {
			
			return null;
			
		}
		
	}

}
