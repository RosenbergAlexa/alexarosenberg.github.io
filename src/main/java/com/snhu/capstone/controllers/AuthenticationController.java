/*
 * Class: AuthenticationController.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the controller to control system behavior in response to actions made by the user interacting
 *    with the User Login window
 *    
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 */

package com.snhu.capstone.controllers;

import com.snhu.capstone.model.user.User;
import com.snhu.capstone.service.StageManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
	
	/**
	 * Define the actions to take place when the 'Log In' button is clicked on the user
	 *    login window
	 */
	@FXML
	protected void userLogin( ActionEvent event ) {
		
		//User authentication actions go here. 
		
		//Retrieve the current stage context
		Stage currentStage = ( Stage )( ( Node ) event.getSource() ).getScene().getWindow();
		
		//TODO: Update to include call to Model to confirm user name & password values when db is integrated
		User loggedInUser;
		String userPassword = password.getText();
		String userUsername = username.getText();
		
		if( userUsername.equals( "admin" ) && userPassword.equals( "admin" ) ) {
			
			//Allow login with admin rights
			loggedInUser = new User( "admin", "admin", "fake@email.com", "ADMIN" );
			username.clear();
			password.clear();
			handleLogin( loggedInUser, currentStage );
			
			
		}else if( userUsername.equals( "user" ) && userPassword.equals( "user" ) ) {
			
			//Allow login with non-admin rights
			loggedInUser = new User( "user", "user", "fakeUser@email.com", "USER" );
			username.clear();
			password.clear();
			this.handleLogin( loggedInUser, currentStage );
			
		}else {
			
			errorMessage.setVisible( true );
			username.clear();
			password.clear();
			
		}
		
	}

	/**
	 * Define behavior which should be executed when the 'X' close window button is clicked on the 
	 *    user login window 
	 */
	public void exitProgram() {
		
		Platform.exit();
		
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

}
