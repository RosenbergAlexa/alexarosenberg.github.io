/*
 * Class: RescueAnimalModel.java
 * Project: CS-499 Capstone
 * Purpose: The collection of rescue animals must be shared between multiple controllers. This class initializes that initial collection 
 *    and holds the collection so that all controllers may access it.
 *    
 * Version History:
 * - 1.0.0     23 May 2026     ARosenberg     Class defined, db engine not enabled
 */

package com.snhu.capstone.model.animal;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.snhu.capstone.model.exceptions.InvalidAnimalDataException;
import javafx.concurrent.Task;

public class RescueAnimalModel{
	
	//Initialize the observable list with an extractor targeting fields which are expected to be updated AND a visual part of the UI
	private final ObservableList< RescueAnimal > animals = FXCollections.observableArrayList( animal -> 
			new Observable[] { animal.trainingStatusProperty(), animal.reservedProperty() } );
	
	/**
	 * A private, internal class meant to handle thread-safe initialization of the RescueAnimalModel data.
	 * Follows the Initialization-On-Demand Holder idiom 
	 */
	private static class Holder{
		
		private static final RescueAnimalModel INSTANCE = new RescueAnimalModel();
		
	}
	
	/**
	 * Public access point to retrieve the class instance
	 * @return
	 */
	public static RescueAnimalModel getInstance() {
		
		//Return the class instance held by the Holder class
		return Holder.INSTANCE;		
		
	}
	
	/**
	 * Initialize the collection of rescue animal data
	 */
	private RescueAnimalModel() {
		
		//Initialize collection of data from the remote database asynchronously
		loadAnimalsFromDatabase();
		
	}
	
	/**
	 * Return the collection of rescue animals to the caller
	 * @return
	 */
	public ObservableList< RescueAnimal > getAnimals(){	
		return this.animals;	
	}
	
	
	/**
	 * Collate the collection of known rescue animals from the database. Executes asynchronously to prevent UI lag
	 */
	private void loadAnimalsFromDatabase() {
		
		//TODO: integrate database into system. Also need to create a DatabaseService
		/*
		Task< List< RescueAnimal > > loadTask = new Task() {
			
			@Override
			protected List< RescueAnimal > call() throws Exception{
				
				//return DatabaseService.fetchAnimals();
				
			}
			
		};
		
		//Set all the retrieved animals if the thread activity completes successfully
		loadTask.setOnSucceed( event -> animals.setAll( loadTask.getValue() ) );
		
		//Set failure behavior if the thread activity fails
		loadTask.setOnFailed( event ->{
			//TODO: LOGGER??????
			//loadTask.getException().printStackTrace();
		});
		*/
		
		//TODO: delete hard-coded animal collection once database integration is complete
		try{
			
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Spot", "Dog", "male", 1, 25.6F, "2019-05-12", "United States", "intake", false, "United States", "German Shepherd" ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Rex", "Dog", "male", 3, 35.2F, "2020-02-03", "United States", "in service", false, "United States", "Great Dane" ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Bella", "Dog", "female", 4, 9.5F, "2019-12-12", "Canada", "in service", true, "Canada", "Chihuahua" ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Astrid", "Monkey", "female", 3, 9.5F, "2021-07-01", "Rwanda", "Phase I", false, "Rwanda", "Guenon", 17.0F, 6.0F, 10.0F ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Chomper", "Monkey", "male", 1, 0.5F, "2022-01-15", "Brazil", "intake", false, "Brazil", "Marmoset", 5.25F, 3.5F, 3.0F ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Machine Gun", "Monkey", "female", 5, 17.0F, "2019-05-29", "Japan", "Phase III", false, "Japan", "Macaque", 20.6F, 0.5F, 20.6F ) ) );
			animals.add( AnimalFactory.createAnimal( new ComponentConfig(
					"Rocket Launcher", "Monkey", "female", 6, 16.34F, "2019-05-29", "Japan", "in service", false, "Japan", "Macaque", 20.6F, 0.5F, 20.6F ) ) );
			
		} catch( InvalidAnimalDataException e ){
			e.printStackTrace();
		}
		
	}

}
