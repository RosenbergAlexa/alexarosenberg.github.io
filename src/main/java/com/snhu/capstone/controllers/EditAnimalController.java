package com.snhu.capstone.controllers;

import java.sql.SQLException;

import com.snhu.capstone.model.TrainingStatus;
import com.snhu.capstone.model.animal.RescueAnimal;
import com.snhu.capstone.model.exceptions.InvalidAnimalDataException;
import com.snhu.capstone.model.exceptions.UnknownAnimalException;
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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class EditAnimalController{
	
	@FXML private ToggleButton reservedStatus;
	@FXML private ComboBox< String > trainingStatus;
	@FXML private Button editButton;
	@FXML private Button cancelButton;
	@FXML private Label message;
	@FXML private Label windowTitle;
	
	private RescueAnimal animalToEdit;
	
	/**
	 * Setter method to be called from any window which shows this one, used to set the identity of the animal being edited
	 * @param animal
	 */
	public void setData( RescueAnimal animal ) {
		this.animalToEdit = animal;
		
		ObservableList< String > trainingStatuses = FXCollections.observableArrayList();
		TrainingStatus[] statuses = TrainingStatus.values();
		
		if( this.animalToEdit != null ) {
			
			//Clear any label text
			message.setText( "" );
			
			//Set the initial value of the toggle button
			reservedStatus.setSelected( this.animalToEdit.getReserved() );
			if( reservedStatus.isSelected() ) {
				reservedStatus.setText( "Yes" );
			}else {
				reservedStatus.setText( "No" );
			}
			
			//Set a listener for the reservation status toggle button to make sure the correct text is displayed
			reservedStatus.selectedProperty().addListener( ( _, _, isSelected ) -> {
				
				if( isSelected ) { 
					reservedStatus.setText( "Yes" ); 
				}else { 
					reservedStatus.setText( "No" ); 
				}
				
			});
			
			//Set the available values of the comboBox
			for( int i = TrainingStatus.fromDisplayName( animalToEdit.getTrainingStatus() ).ordinal(); i < statuses.length ; ++i ) {
				trainingStatuses.add( statuses[ i ].toString() );
			}
			trainingStatus.setItems( trainingStatuses );
			trainingStatus.setValue( trainingStatuses.getFirst() );
			
			//Set the window title message
			windowTitle.setText( "Editing " +  animalToEdit.getAnimalTypeStr()+ " " + animalToEdit.getName() );
			
			
		}else {
			editButton.setDisable( true );
			message.setText( "Something went wrong - Unknown animal" );
			windowTitle.setText( "Unknown Animal" );
		}
		
	}
	
	@FXML
	public void initialize() {}
	
	/**
	 * Defines what should happen when the user clicks the 'Apply' button
	 * @param event
	 */
	@FXML
	private void editAnimal( ActionEvent event ) {
		
		saveEditData();
		
	}
	
	/**
	 * Defines what should happen when the user clicks the 'Cancel' button
	 */
	@FXML
	private void cancelEdit( ActionEvent event ) {
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource() ).getScene().getWindow();
		StageManager.getInstance().returnToPreviousWindow( currentStage );
		
	}
	
	/**
	 * Controls the animal status update functionality, marshals the record update process to a background thread, and handles 
	 *    failure and success response behavior 
	 */
	private void saveEditData() {
		
		Scene scene = trainingStatus.getScene();
		
		//Tasks are JavaFX's background thread class - use to create a non-UI thread to run db actions on without blocking the main UI thread
		Task< Void > editTask = new Task< Void >() {
			
			@Override
			protected Void call() throws Exception{
				return DatabaseService.editAnimal( animalToEdit, trainingStatus.getValue(), reservedStatus.isSelected() );
			}
			
		};
		
		//Set the mouse cursor to be a spinning wheel while the task is running so users know the program is thinking
		scene.cursorProperty().bind( 
				Bindings.when( editTask.runningProperty() ) 
				.then( Cursor.WAIT )
				.otherwise( Cursor.DEFAULT ) );
		
		//Set what should happen if the thread completes successfully
		editTask.setOnSucceeded( _ -> {
			animalToEdit.setReserved( reservedStatus.isSelected() );
			animalToEdit.setTrainingStatus( trainingStatus.getValue() );
			
			Stage currentStage = ( Stage )scene.getWindow();
			StageManager.getInstance().returnToPreviousWindow( currentStage );
		});
		
		//Set failure behavior if the thread fails - make sure exceptions messages are sanitized to prevent security leaks
		editTask.setOnFailed( _ -> {
			Throwable exception = editTask.getException();
			
			if( exception != null && exception instanceof SQLException ) {
				message.setText( "Something went wrong - Please try again or contact support" );
			}else if( exception != null && ( exception instanceof InvalidAnimalDataException || exception instanceof UnknownAnimalException ) ) {
				message.setText( exception.getMessage() );
			}else {
				message.setText( "Something went wrong - Please contact support" );
			}
		});
		
		//Launch a background thread from the thread pool to keep the UI functional
		Thread backgroundThread = new Thread( editTask );
		backgroundThread.setDaemon( true );
		backgroundThread.start();
		
	}

}
