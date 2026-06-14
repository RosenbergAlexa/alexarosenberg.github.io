/*
 * Class: AddAnimalController.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the controller for the 'New Animal' window. Sets and executes dialog appearence, reactions to user interaction, and 
 *    data validation.
 *    
 * Version History:
 * - 1.0.0     26 May 2026     ARosenberg     Class defined
 */

package com.snhu.capstone.controllers;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import com.snhu.capstone.model.AnimalType;
import com.snhu.capstone.model.Gender;
import com.snhu.capstone.model.TrainingStatus;
import com.snhu.capstone.model.animal.AnimalFactory;
import com.snhu.capstone.model.animal.ComponentConfig;
import com.snhu.capstone.model.animal.RescueAnimalModel;
import com.snhu.capstone.model.exceptions.InvalidAnimalDataException;
import com.snhu.capstone.model.exceptions.InvalidAnimalStatusException;
import com.snhu.capstone.model.exceptions.InvalidAnimalTypeException;
import com.snhu.capstone.service.DatabaseService;
import com.snhu.capstone.service.StageManager;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AddAnimalController{
	
	@FXML private Button cancelButton;
	@FXML private Button addButton;
	@FXML private ComboBox< String > animalType;
	@FXML private ComboBox< String > animalGender;
	@FXML private ComboBox< String > animalTrainingStatus;
	@FXML private DatePicker animalGotchaDate;
	@FXML private ToggleButton animalReserved;
	@FXML private TextField animalName;
	@FXML private TextField animalAge;
	@FXML private TextField animalWeight;
	@FXML private TextField animalCountryOfOrigin;
	@FXML private TextField animalServiceCountry;
	@FXML private TextField animalSubType;
	@FXML private Label subTypeLabel;
	@FXML private Label errorMessage;
	
	//Monkey specific fields
	@FXML private TextField monkeyHeight;
	@FXML private TextField monkeyBodyLength;
	@FXML private TextField monkeyTailLength;
	
	private final String dateFormat = "yyyy-MM-dd";
	
	
	/**
	 * Initializes FXML components that must be populated first before modifying their contents
	 */
	@FXML 
	void initialize() {
		
		//Ensure no initial error message is displayed
		errorMessage.setText( "" );
		
		//Set a date formatter on the DatePicker field
		setDateFormatting();
		
		//Set field formatters for the animal Age and Weight fields to limit them to numerical values only
		setNumericFieldFormatting();
		
		//Set the values to appear in the animal gender drop-down
		setComboBoxDropdowns();
		
		//Set the values to appear in the animal training status drop-down
		setTrainingStatusDropdown();
		
		//Set a listener for the reservation status toggle button to make sure the correct text is displayed
		animalReserved.selectedProperty().addListener( ( _, _, isSelected ) -> {
			
			if( isSelected ) { 
				animalReserved.setText( "Yes" ); 
			}else { 
				animalReserved.setText( "No" ); 
			}
			
		});
		
	}
	
	/**
	 * Define a configuration method to dictate how dates are converted into text and vice versa
	 *    for the Animal Acquisition date field
	 */
	private void setDateFormatting() {
		
		//Create a DateTime formatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( dateFormat );
		
		//Set a date converter for the DatePicker field to convert date strings to a format which can be consumed by the system
		animalGotchaDate.setConverter( new StringConverter< LocalDate >() {
			
			//Handles converting a date selected via the calendar into a correctly formatted string
			@Override
			public String toString( LocalDate date ) {
				
				if( date != null ) {
					
					try {
						errorMessage.setText( "" );
						return formatter.format( date );	
					}catch( DateTimeException e ) { 
						errorMessage.setText( "Invalid animal acquisition date detected, please follow the format yyyy-MM-dd" ); 
						return "";
					}
					
				}else {
					return "";
				}
				
			}
			
			//Handles converting a date manually entered by the user
			@Override
			public LocalDate fromString( String string ) {
				
				if( string != null && !string.isEmpty() ) {
					
					try {
						errorMessage.setText( "" );
						return LocalDate.parse( string, formatter );
					}catch( DateTimeException e ) {
						errorMessage.setText( "Invalid animal acquisition date detected, please follow the format yyyy-MM-dd" );
						return null;
					}	
					
				}else {
					return null;
				}
				
			}
			
		});		
		
	}
	
	/**
	 * Defines gatekeeping setter methods for the animal age and weight fields fields to limit entered age values to whole numbers 
	 *    and weight, monkey height, monkey tail length, and monkey body length to floating-point numbers only
	 */
	private void setNumericFieldFormatting() {
		
		//Set a text formatter for the animalAge text field to limit it to numbers only
		animalAge.setTextFormatter( new TextFormatter<>( change -> {
			
			if( change.getControlNewText().matches( "\\d" ) ) { return change; }
			return null; //If the condition fails and something other than digits was entered, reject the change
			
		} ) );
		
		
		//Define a regex string to utilize in the following text formatter
		String regex = "-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?";
		
		//Use a UnaryOperator to create a custom regex filter which checks a text format field's changed data against a regex format string
		UnaryOperator< TextFormatter.Change > filter = change -> {
			
			String weight = change.getControlNewText();
			if( weight.matches( regex ) ) { return change; }
			return null;
			
		};
		
		//Set a text formatter for the all numeric text fields to limit them to floating point values only
		animalWeight.setTextFormatter( new TextFormatter<>( filter ) );		
		monkeyHeight.setTextFormatter( new TextFormatter<>( filter ) );
		monkeyBodyLength.setTextFormatter( new TextFormatter<>( filter ) );
		monkeyTailLength.setTextFormatter( new TextFormatter<>( filter ) );
		
	}
	
	/**
	 * Sets the collection of selectable animal genders for the drop-down field
	 */
	private void setComboBoxDropdowns() {
		
		ObservableList< String > animalGenders = FXCollections.observableArrayList();
		ObservableList< String > animalTypes = FXCollections.observableArrayList();
		
		//Add all the allowed animal genders to the observable list of drop-down options
		for( Gender gender : Gender.values() ) {
			animalGenders.add( gender.toString() );
		}
		
		//Add all the allowed animal types to the observable list of drop-down options
		for( AnimalType type : AnimalType.values() ) {
			animalTypes.add( type.toString() );
		}
		
		//Set the list to the correct drop-down field
		animalGender.setItems( animalGenders );
		animalType.setItems( animalTypes );
		
	}
	
	/**
	 * Sets the collection of selectable animal training statuses for the drop-down field
	 */
	private void setTrainingStatusDropdown() {
		
		ObservableList< String > trainingStatus = FXCollections.observableArrayList();
		
		//Add all the allowed training statuses to the observable list of drop-down options
		for( TrainingStatus level : TrainingStatus.values() ) {
			trainingStatus.add( level.toString() );
		}
		
		//Set the list to the correct drop-down field
		animalTrainingStatus.setItems( trainingStatus );
		
	}
	
	/**
	 * Defines the actions to perform when the user changes what type of animal they are adding to the company's collection of
	 *    rescue animals
	 * @param event
	 */
	@FXML
	public void animalTypeSelected( ActionEvent event ) {
		
		String selectedType = animalType.getValue();
		
		//If Dog selected, disable height, tail length, and body length fields. Set subtype label text to 'Breed'
		if( selectedType != null && selectedType == "Dog" ) {
			
			monkeyHeight.setDisable( true );
			monkeyHeight.clear();
			
			monkeyBodyLength.setDisable( true );
			monkeyBodyLength.clear();
			
			monkeyTailLength.setDisable( true );
			monkeyTailLength.clear();
			
			subTypeLabel.setText( "Breed" );
			
		}
		
		//If Monkey selected, enable height, tail length, and body length fields. Set subtype label to 'species'
		if( selectedType != null && selectedType == "Monkey" ) {
			
			monkeyHeight.setDisable( false );
			monkeyBodyLength.setDisable( false );
			monkeyTailLength.setDisable( false );
			subTypeLabel.setText( "Species" );
			
		}
		
	}
		
	/**
	 * Defines what should happen when users click the "Add" button on the window
	 * @param event
	 */
	@FXML
	public void addNewAnimal( ActionEvent event ) {
	
		String selectedAnimalType = animalType.getValue();
		String gender, trainingStatus, name, originCountry, serviceCountry, subType;
		LocalDate receivedDate;
		boolean reserved;
		int age = -1;
		float weight = -1.0f, mHeight = -1.0f, mBodyLength = -1.0f, mTailLength = -1.0f;
		ComponentConfig newCfg;
		
		
		if( selectedAnimalType != null ) {
			
			//Check animal gender
			gender = animalGender.getValue();
			if( gender == null ) {
				errorMessage.setText( "Incomplete animal data found - You must select a gender for the animal being added" );
				return;
			}
			
			//Check animal training status
			trainingStatus = animalTrainingStatus.getValue(); 
			if( trainingStatus == null ) {
				errorMessage.setText( "Incomplete animal data found - You must select a training status for the animal being added" );
				return;
			}
			
			//Check the animal acquired date
			receivedDate = animalGotchaDate.getConverter().fromString( animalGotchaDate.getEditor().getText() );
			if( receivedDate == null ) {
				errorMessage.setText( "Incomplete animal data found - You must indicate a date upon which the new animal was acquired" );
				return;
			}
			
			//ToggleButtons always returns a value because they start off with a default state, will be false if never clicked
			reserved = animalReserved.isSelected();
			
			//Check animal name
			name = animalName.getText().trim();
			if( name == null ||  name.isEmpty() ) {
				errorMessage.setText( "Incomplete animal data found - You must provide a name for the animal being added" );
			}
			
			//Check animal age
			try{
				age = Integer.parseInt( animalAge.getText().trim() );
			}catch( NumberFormatException e ) {
				errorMessage.setText( "Incomplete/invalid animal data found - You must provide a valid age for the animal being added" );
				return;
			}
			
			//Check animal weight
			try{
				weight = Float.parseFloat( animalWeight.getText().trim() );
			}catch( NullPointerException | NumberFormatException e ) {
				errorMessage.setText( "Incomplete/invalid animal data found - You must provide a weight value for the animal being added" );
				return;
			}
			
			//Check animal country of origin
			originCountry = animalCountryOfOrigin.getText().trim();
			if( originCountry == null || originCountry.isEmpty() ) {
				errorMessage.setText( "Incomplete/invalid animal data found - You must provide a country of origin for the animal being added" );
				return;
			}
			
			//Check animal service country
			serviceCountry = animalServiceCountry.getText().trim();
			if( serviceCountry == null || serviceCountry.isEmpty() ) {
				errorMessage.setText( "Incomplete/invalid animal data found - You must provide a service country for the animal being added" );
				return;
			}
			
			//Check animal subType - aka breed or species
			subType = animalSubType.getText().trim();
			if( subType == null || subType.isEmpty() ) {
				errorMessage.setText( "Incomplete/invalid animal data found - You must provide a " + selectedAnimalType + " " + subTypeLabel.getText() );
				return;
			}
			
			//Validate fields specific to Monkeys only
			if( selectedAnimalType == "Monkey" ) {
				
				try {
					mHeight = Float.parseFloat( monkeyHeight.getText() );
				}catch( NullPointerException e ) {
					errorMessage.setText( "Incomplete animal data found - You must provide a height value for the monkey being added" );
					return;
				}
				
				try{
					mBodyLength = Float.parseFloat( monkeyBodyLength.getText() );
				}catch( NullPointerException e ) {
					errorMessage.setText( "Incomplete animal data found - You must provide a tail length value for the mokey being added" );
					return;
				}
				
				try {
					mTailLength = Float.parseFloat( monkeyTailLength.getText() );
				}catch( NullPointerException e ) {
					errorMessage.setText( "Incomplete animal data found - You must provide a tail length value for the monkey being added" );
				}
					
			}	
			
		}else {
			errorMessage.setText( "No rescue animal type selected, please select a valid animal type first" );
			return;
		}
		
		//Create the ComponentConfig required to use the AnimalFactory && then add the new animal to the system collections
		try {
						
			newCfg = new ComponentConfig( name, selectedAnimalType, gender, age, weight, receivedDate, originCountry, trainingStatus, 
					reserved, serviceCountry, subType, mHeight, mTailLength, mBodyLength );
			
		}catch( InvalidAnimalDataException e ) {
			errorMessage.setText( e.getMessage() );
			return;
		}
		
		//Add animal info to database, need to create the DatabaseService	
		this.saveAnimal( newCfg );

	}
	
	/**
	 * Return to the previous window without saving any of the entered new animal information
	 * @param event
	 */
	@FXML
	public void cancelNewAnimal( ActionEvent event ) {
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to route to the reserve animal window
		StageManager.getInstance().returnToPreviousWindow( currentStage );
		
	}
	
	/**
	 * 
	 * @param newCritter
	 */
	private void saveAnimal( ComponentConfig newCritter ) {
		
		Scene scene = errorMessage.getScene();
		
		//Create a task to handle execution of the async DatabaseService call
		Task< Void > addAnimalTask = new Task< Void >() {
			
			@Override
			protected Void call() throws Exception{
				return DatabaseService.addAnimal( newCritter );
			}
			
		};
		
		//Set the mouse cursor to appear as a spinning wheel while the task is running so users know the program is "thinking"
		scene.cursorProperty().bind( 
				Bindings.when( addAnimalTask.runningProperty() )
				.then( Cursor.WAIT )
				.otherwise( Cursor.DEFAULT ) );
		
		//Set what happens if the thread completes successfully
		addAnimalTask.setOnSucceeded( _ -> {
			
			RescueAnimalModel.getInstance().addAnimal( AnimalFactory.createAnimal( newCritter ) );
			Stage currentStage = ( Stage ) scene.getWindow();
			StageManager.getInstance().returnToPreviousWindow( currentStage );
			
		});
		
		//Set what happens if the thread activity fails - make sure to sanitize exception messages to prevent security leaks
		addAnimalTask.setOnFailed( _ -> {
			Throwable exception = addAnimalTask.getException();
			
			if( exception != null && exception instanceof SQLException ) {
				errorMessage.setText( "Something went wrong, animal not added - Please try again or contact support" );
			}else if( exception != null && ( exception instanceof InvalidAnimalDataException || 
					exception instanceof InvalidAnimalTypeException || exception instanceof InvalidAnimalStatusException ) ) {
				errorMessage.setText( exception.getMessage() );
			}else {
				errorMessage.setText( "Something went wrong, animal not added - Please try again or contact support" );
			}
				
		});
		
		//Launch a background thread from the thread pool to keep the UI functional/unblocked
		Thread backgroundThread = new Thread( addAnimalTask );
		backgroundThread.setDaemon( true );
		backgroundThread.start();
		
	}

}
