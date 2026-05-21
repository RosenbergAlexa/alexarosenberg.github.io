package capstone.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthenticationController{
	
	@FXML private Button loginButton;
	@FXML private PasswordField password;
	@FXML private TextField username;
	
	@FXML
	protected void userLogin( ActionEvent event ) {
		
		//User authentication actions go here. 
		//TODO: Update to include call to Model to confirm user name & password values when db is integrated
		//String userPassword = password.getText();
		//String userUsername = username.getText();
		
		return;
		
	}

	/*
	 * Method: exitProgram
	 * Purpose: define behavior which should be executed when the 'X' close window button is clicked on the 
	 *    user login window 
	 */
	public void exitProgram() {
		
		Platform.exit();
		
	}

}
