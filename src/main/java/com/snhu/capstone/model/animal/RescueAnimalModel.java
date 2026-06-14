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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import java.util.Comparator;
import java.util.List;

import com.snhu.capstone.service.DatabaseService;

public class RescueAnimalModel{
	
	//String property to capture, hold, and expose database/system errors
	private final StringProperty errorMessage = new SimpleStringProperty( "" );
	
	//Initialize the observable list with an extractor targeting fields which are expected to be updated AND a visual part of the UI
	private final ObservableList< RescueAnimal > animals = FXCollections.observableArrayList( animal -> 
			new Observable[] { 
					animal.trainingStatusProperty(),
					animal.reservedProperty(),
					animal.nameProperty(),
					animal.animalTypeProperty() 
					} 
			);
	
	//Wrap the observable list in a filtered list
	private final FilteredList< RescueAnimal > filteredAnimals = new FilteredList<>( this.animals );
	
	//Wrap the observable list into a SortedList. This makes it possible to automatically add new animals in a sorted fashion
	private final SortedList< RescueAnimal > sortedAnimals = new SortedList<>( this.filteredAnimals );
	
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
	 * Getters and Setters for the error message property
	 * @return
	 */
	public StringProperty errorMessageProperty() { return this.errorMessage; }
	public String getErrorMessage() { return this.errorMessage.get(); }
	public void clearErrorMessage() { this.errorMessage.set( "" ); }
	
	/**
	 * Returns the collection of filtered animals
	 */
	public FilteredList< RescueAnimal > getFilteredAnimals(){ return this.filteredAnimals; }
	
	/**
	 * Returns the list of sorted animals
	 */
	public SortedList< RescueAnimal > getSortedAnimals(){ return this.sortedAnimals; }
	
	/**
	 * Adds a rescue animal to the held collection of rescue animal objects. Using this ensures that the filtered list and sorted lists are
	 *   also updated properly
	 */
	public void addAnimal( RescueAnimal animal ) {
		if( animal != null ) {
			
			this.animals.add( animal );
			
		}
	}
	
	/**
	 * Initialize the collection of rescue animal data
	 */
	private RescueAnimalModel() {	
		
		Comparator< RescueAnimal > optimizedSort = ( animal1, animal2 ) -> {
			
			//Compare the names of the 2 animals first
			int compareResult = animal1.getName().compareToIgnoreCase( animal2.getName() );
			if( compareResult != 0 ) {
				return compareResult;
			}
			
			//If the names are the same, compare their animal types
			return animal1.getAnimalTypeStr().compareToIgnoreCase( animal2.getAnimalTypeStr() );
		};
		
		sortedAnimals.setComparator( optimizedSort );
		
		//Initialize collection of data from the remote database asynchronously
		loadAnimalsFromDatabase();
		
	}	
	
	/**
	 * Collate the collection of known rescue animals from the database. Executes asynchronously to prevent UI lag
	 */
	private void loadAnimalsFromDatabase() {
		
		Task< List< RescueAnimal > > loadTask = new Task<>() {
			
			@Override
			protected List< RescueAnimal > call() throws Exception{
				return DatabaseService.fetchAnimals();
			}
			
		};
		
		//Set all the retrieved animals if the thread activity completes successfully
		loadTask.setOnSucceeded( _ -> {
			List< RescueAnimal > results = loadTask.getValue();
			if( results != null && !results.isEmpty() ){
				animals.setAll( results );
			}
		});
		
		//Set failure behavior if the thread activity fails
		loadTask.setOnFailed( _ ->{
			Throwable exception = loadTask.getException();
			
			String msg = ( exception != null && exception.getMessage() != null ) 
					? exception.getMessage()
					: "A timeout or network drop occurred with the database";
			
			//Set the current error message so that callers can check and update on their end
			this.errorMessage.set( msg );
			
		});
		
		//Launch background thread from the thread pool to keep the UI functional
		Thread backgroundThread = new Thread( loadTask );
		backgroundThread.setDaemon( true );
		backgroundThread.start();
		
	}

}
