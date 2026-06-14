/*
 * Class: StageManager.java
 * Project: CS-499 Capstone
 * Purpose: Acts as a controller which manages moving between windows. Acts akin to a navigation service, to provide each window 
 *    a way to move to a requested window without having to know anything about the view being asked for beyond it's fxml file path
 *    
 * Version History:
 * - 1.0.0     22 May 2026     ARosenberg     Class defined
 */

package com.snhu.capstone.service;

import java.io.IOException;
import java.util.Stack;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageManager{
	
	private static final StageManager instance = new StageManager();
	private final Stack< Stage > stageHistory = new Stack<>();
	
	/**
	 * Private class constructor. Should only be called once: the first time the class is instantiated
	 */
	private StageManager() {}
	
	/**
	 * Returns the StageManager object to the caller
	 * @return
	 */
	public static StageManager getInstance() {
		return instance;
	}
	
	/**
	 * Prompts the stage manager to open the requested window. The current window will be stored so that it can be
	 *   returned to if/when requested. A lambda method can be passed in to prompt the execution of a controller method,
	 *   which can allow data to be passed into the window being opened
	 * @param <T>
	 * @param currentStage
	 * @param fxmlPath
	 * @param controllerConfig
	 */
	public <T> void openWindow( Stage currentStage, String fxmlPath, Consumer< T > controllerConfig ) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader( StageManager.class.getResource( fxmlPath ) );
			Parent root = loader.load();
			
			//Inject data into the controller if a configuration lambda was provided
			if( controllerConfig != null ) {
				
				T controller = loader.getController();
				controllerConfig.accept( controller );
				
			}
			
			//Create the current stage using the loaded fxml layout
			Stage newStage = new Stage();
			newStage.setScene( new Scene( root ) );
			
			//Hide the current stage and save it to the history stack
			if( currentStage != null ) {
				
				currentStage.hide();
				stageHistory.push( currentStage );
				
			}
			
			//Set behavior of users clicking the OS 'X' button to close the window
			newStage.setOnCloseRequest( event -> {
				
				//Consume the click event to prevent default behavior
				event.consume();
				returnToPreviousWindow( newStage );
				
			});
			
			newStage.show();
			
		}catch( IOException e ) {
			//TODO: add system logging???
			e.printStackTrace();			
		}
		
	}
	
	/**
	 * Closes the current window and restores the last one from history
	 * @param currentStage
	 */
	public void returnToPreviousWindow( Stage currentStage ) {
		
		//Close the current window
		currentStage.close();
		
		//Check if there is a window to go back to, display it if one exists
		if( !stageHistory.isEmpty() ) {
			
			Stage previousStage = stageHistory.pop();
			previousStage.show();
			
		}
		
	}
	
	/**
	 * Closes all currently open but hidden windows and displays the requested window. Intention is to return users to the
	 *    user login screen
	 * @param currentStage
	 * @param loginFxmlPath
	 */
	public void resetToLogin( Stage currentStage, String loginFxmlPath ) {
		
		//Close the currently active windown
		if( currentStage != null ) {
			
			currentStage.close();
			
		}
		
		//Explicitly close all hidden windows in the history stack
		while( !stageHistory.isEmpty() ) {
			
			stageHistory.pop().close();
			
		}
		
		//Redirect to the fresh login window as the new root stage
		openWindow( null, loginFxmlPath, null );
		
	}

}
