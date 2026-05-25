package com.snhu.capstone;

import com.snhu.capstone.model.animal.RescueAnimalModel;
import com.snhu.capstone.service.StageManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Driver extends Application {
	
	/**
	 * Automatically called by JavaFX, acts as the application entry point for the UI
	 */
	@Override
	public void start( Stage primaryStage ) {
		
		//Initialize collection of rescue animal data so it is available when needed
		RescueAnimalModel.getInstance();
				
		//Hand off control to the StageManager to route the view to the user login screen
		StageManager.getInstance().openWindow( null, "/view/userLogin.fxml", null );
		
	}	
	
	public static void main( String[] args ) {
		
		launch( args );
		
	}
	
}

