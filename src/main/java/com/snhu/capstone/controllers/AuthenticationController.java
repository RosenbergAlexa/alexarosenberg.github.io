/*
 * Class: AuthenticationController.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the controller to control system behavior in response to actions made by the user interacting
 *    with the User Login window
 *    
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * - 1.1.0     03 Jun 2026     ARosenberg     Class updated to connect user validation to the back-end database
 */

package com.snhu.capstone.controllers;

import java.sql.SQLException;

import com.snhu.capstone.model.exceptions.InvalidUserException;
import com.snhu.capstone.model.user.User;
import com.snhu.capstone.service.DatabaseService;
import com.snhu.capstone.service.StageManager;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthenticationController{
	
	@FXML private Button loginButton;
	@FXML private PasswordField password;
	@FXML private TextField username;
	@FXML private Label errorMessage;
	
	private User user;
	private Stage currentStage;
	
	/**
	 * Define the actions to take place when the 'Log In' button is clicked on the user
	 *    login window
	 */
	@FXML
	private void userLogin( ActionEvent event ) {
		
		//User authentication actions go here
		currentStage = ( Stage )( ( Node ) event.getSource() ).getScene().getWindow();
		validateUserLogin();
		
	}

	/**
	 * Define behavior which should be executed when the 'X' close window button is clicked on the 
	 *    user login window 
	 */
	public void exitProgram() {
		
		Platform.exit();
		
	}
	
	/**
	 * Initializes FXML components that must be populated first before modifying their contents
	 */
	@FXML 
	void initialize() {
		
		errorMessage.setText( "" );
		errorMessage.setVisible( true );
		
	}
	
	/**
	 * Route the view to the inventory window. Requires the user login validate step to have been successful
	 * @param validUser
	 * @param currentStage
	 */
	private void handleLogin( User validUser, Stage currentStage ) {
		
		//Use the StageManager service to route to the requested view. Pass User information using a lambda expression
		StageManager.getInstance().openWindow( 
				currentStage, 
				"/view/systemInventory.fxml", 
				( InventoryController controller ) -> {
					controller.setData( validUser );
			});
		
	}
	
	/**
	 * Controls the user authentication process, marshals it on a background thread, and handles success and failure behavior response
	 *    for the UI
	 */
	private void validateUserLogin() {
		
		String userPassword = password.getText();
		String userUsername = username.getText();
		Scene scene = username.getScene();
		
		//Tasks are JavaFX's background thread class - use to create a non-UI thread to run db actions on without blocking the main UI thread
		Task< User > validateTask = new Task< User >() {
			
			@Override
			protected User call() throws Exception{
				return DatabaseService.validateUser( userUsername, userPassword );
			}
			
		};
		
		//Set the mouse cursor to turn into a spinning wheel while the task is running so users know the program is "thinking"
		scene.cursorProperty().bind(
				Bindings.when( validateTask.runningProperty() )
				.then( Cursor.WAIT )
				.otherwise( Cursor.DEFAULT ));
		
		//Set what should happen if the thread completes successfully
		validateTask.setOnSucceeded( _ -> {
			this.user = validateTask.getValue();
			username.clear();
			password.clear();

			if( user != null ) {
				this.handleLogin( user, currentStage );
			}
		});
		
		//Set failure behavior if the thread activity fails - make sure exception messages are sanitized to prevent security leaks
		validateTask.setOnFailed( _ -> {
			Throwable exception = validateTask.getException();
			username.clear();
			password.clear();
			
			if( exception != null && exception instanceof SQLException ) {
				errorMessage.setText( "Something went wrong - Please try again or contact support" );
			}else if( exception != null && exception instanceof InvalidUserException ) {
				errorMessage.setText( exception.getMessage() );
			}else {
				errorMessage.setText( "Something went wrong - Please try again or contact support" );
			}
		});
		
		//Launch a background thread from the thread pool to keep the UI functional
		Thread backgroundThread = new Thread( validateTask );
		backgroundThread.setDaemon( true );
		backgroundThread.start();
		
	}

}
