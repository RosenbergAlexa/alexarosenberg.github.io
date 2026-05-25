/*
 * Class: InventoryController.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the controller to control system behavior on the main inventory window, respond to user interactions,
 *    and make display setting decisions based on authentication level
 *    
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * - 1.0.1     23 May 2026     ARosenberg     Updated to utilize RescueAnimalModel for retrieving animal data
 */

package com.snhu.capstone.controllers;

import com.snhu.capstone.model.animal.RescueAnimal;
import com.snhu.capstone.model.animal.RescueAnimalModel;
import com.snhu.capstone.model.user.User;
import com.snhu.capstone.service.StageManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InventoryController{
	
	@FXML private Button logoutButton;
	@FXML private Button reserveAnimalButton;
	@FXML private Button newAnimalButton;
	@FXML private Label welcomeTitle;
	@FXML private TextField searchKeyword;
	@FXML private ListView< RescueAnimal > inventoryView;
	
	private User currentUser;
	private ObservableList< RescueAnimal > observableList;
	
	/**
	 * Identify the collection of known animals in the inventory and create cards for them in the ListView
	 */
	private void setListView() {
		
		//Add array list items to the observable list, then set the observable list as the listView's item collection
		observableList = FXCollections.observableArrayList( RescueAnimalModel.getInstance().getAnimals() );
		inventoryView.setItems( observableList );
		
		//Set the ListView cell factory controller. Necessary to ensure each animal card is created correctly
		inventoryView.setCellFactory( _ -> new AnimalCardCell( this.currentUser.isAdmin() ) );
		
	}
	
	@FXML
	void initialize() {}
	
	/**
	 * Setter method to be called from the authentication controller to set the currently authenticated user
	 * @param user
	 */
	public void setData( User user ) {	
		if( user != null ) {
			
			this.currentUser = user;	
			
			//Construct the listView cards
			setListView();
			
			//Set the dialog title
			welcomeTitle.setText( "Welcome, " + currentUser.getFirstName() );
			newAnimalButton.setVisible(  currentUser.isAdmin() );
			
		}else { System.out.println( "Provided null user data, please contact support" ); }
		
		
			
	}
	
	/**
	 * Defines the actions to take when the 'Log Out' button is clicked on the main inventory window
	 * @param event
	 */
	@FXML
	protected void userLogout( ActionEvent event ) {
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to route back to the user login window. Close all open windows to start afresh
		StageManager.getInstance().resetToLogin( currentStage, "/view/userLogin.fxml" );
		
	}
	
	@FXML
	protected void addNewAnimal( ActionEvent event ) {
		
		//TODO: connect logic to show add animal screen. Should only be visible if the user has admin rights
		
	}
	
	/**
	 * Defines the actions to take place when the 'Reserve Animal' button is clicked on the main inventory window
	 * @param event
	 */
	@FXML
	protected void reserveAnimal( ActionEvent event ) {
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to route to the reserve animal window
		StageManager.getInstance().openWindow( currentStage, "/view/reserveAnimal.fxml", null );
		
	}
	

}
