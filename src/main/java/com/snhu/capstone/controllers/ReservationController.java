package com.snhu.capstone.controllers;

import com.snhu.capstone.model.AnimalType;
import com.snhu.capstone.model.animal.RescueAnimal;
import com.snhu.capstone.model.animal.RescueAnimalModel;
import com.snhu.capstone.service.StageManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReservationController{
	
	@FXML private Button reserveButton;
	@FXML private Button cancelReserve;
	@FXML private Label message;
	@FXML private ComboBox< String > typeDropdown;
	@FXML private TextField targetCountry;
	
	private FilteredList< RescueAnimal > knownAnimals;
	
	/**
	 * Automatically called by JavaFX when a scene with this controller is created and shown. Used to set data for the reserve animal window
	 */
	@FXML public void initialize() {
		
		ObservableList< String > animalTypes = FXCollections.observableArrayList();
		
		//Add all the allowed animal types to the list of drop-down options
		for( AnimalType type : AnimalType.values() ) {
			animalTypes.add( type.toString() );
		}
		
		//Set the observable list as the collection of items associated with the ComboBox
		typeDropdown.setItems( animalTypes );
		message.setText( "" );
		
		//Wrap the observable list in a filtered list
		knownAnimals = new FilteredList<>( RescueAnimalModel.getInstance().getAnimals() );
		
		//Set the listeners for the filtered list to react when either dropdown or country value is changed
		typeDropdown.valueProperty().addListener( ( _, _, _ ) -> updateFilter() );
		targetCountry.textProperty().addListener( ( _, _, _ ) -> updateFilter() );
		
	}	
	
	/**
	 * Returns the user to the main inventory window without making any (further) reservations
	 * @param event
	 */
	@FXML
	public void cancelAction( ActionEvent event ) {
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		StageManager.getInstance().returnToPreviousWindow( currentStage );
		
	}
	
	/**
	 * The listener for the action of the user clicking the 'Reserve' button
	 * @param event
	 */
	@FXML 
	public void reserveAnimal( ActionEvent event ) {
		
		//Validate selected item in combo box -aka user HAS to select an option
		String selectedType = typeDropdown.getValue();
		if( selectedType == null ) {
			
			message.setText( "You must choose an animal type to reserve a rescue animal" );
			return;
			
		}
		
		//Validate desired country - aka, user HAS to enter a value
		String selectedCountry = targetCountry.getText();
		if( selectedCountry == null || selectedCountry.trim().isEmpty() ) {
			
			message.setText( "You must enter a country to reserve a rescue animal" );
			return;
			
		}
		
		//Reserve animal:
		if( knownAnimals.isEmpty() ) {
			
			message.setText( "No rescue animal is available which matches your specified criteria" );
			return;
			
		}
		
		//Get the first animal matching the user's requested criteria
		RescueAnimal selectedAnimal = knownAnimals.getFirst();
		selectedAnimal.setReserved( true );
		
		//Use thread to send update command for animal using DatabaseService to update db data
		/*
		Thread saveAnimalThread = Thread.ofPlatform().daemon.unstarted(() -> {
		   try{
		      //Invoke call to DatabaseService
		      DatabaseService.updateAnimal( selectedAnimal );
		   }catch( Exception e ){
		      //TODO: LOGGER?????
		   }
		});
		saveAnimalThread.start();
		 */
		
		//Update message
		message.setText( selectedAnimal.getName() + " reserved for " + selectedAnimal.getInServiceCountry() );
		
	}
	
	/**
	 * The listener applied to the filtered list, which is just the observable list of rescue animals in the database, put in a filterable container.
	 * The applied filter acts as a search that is updated as users enter information. Results of the filter are processed in
	 * reserveAnimal()
	 */
	private void updateFilter() {
		
		String selectedCountry = targetCountry.getText() == null ? "" : targetCountry.getText().trim().toLowerCase();
		String selectedType = typeDropdown.getValue();
		
		knownAnimals.setPredicate( animal -> {
			
			//Condition 1 - match the animal type or skip if value not selected
			boolean matchType = false;
			if( selectedType != null ) {
				
				matchType = AnimalType.valueOf( selectedType.toUpperCase() ) == animal.getAnimalType();
				
			}
			
			//Condition 2 - match the requested country or skip of not entered
			boolean matchCountry = false;
			if( selectedCountry != null ) {
				
				matchCountry = selectedCountry.equalsIgnoreCase( animal.getInServiceCountry() );
				
			}
			
			boolean reservedStatus = animal.getReserved();
			
			//The item passes the filter only if it matches both animal type and selected country
			return !reservedStatus && ( matchType && matchCountry );
			
		});
		
	}

}
