/*
 * Class: InventoryController.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the controller to control system behavior on the main inventory window, respond to user interactions,
 *    and make display setting decisions based on authentication level
 *    
 * Version History:
 * - 1.0.0     21 May 2026     ARosenberg     Class defined
 * - 1.0.1     23 May 2026     ARosenberg     Updated to utilize RescueAnimalModel for retrieving animal data
 * - 1.1.0     02 Jun 2026     ARosenberg     Class updated to connect initial display of animal collection to the back-end database
 */

package com.snhu.capstone.controllers;

import com.snhu.capstone.model.animal.RescueAnimal;
import com.snhu.capstone.model.animal.RescueAnimalModel;
import com.snhu.capstone.model.user.User;
import com.snhu.capstone.service.StageManager;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InventoryController {
	
	@FXML private Button logoutButton;
	@FXML private Button reserveAnimalButton;
	@FXML private Button newAnimalButton;
	@FXML private Label welcomeTitle;
	@FXML private TextField searchKeyword;
	@FXML private ListView< RescueAnimal > inventoryView;
	
	private User currentUser;
	
	/**
	 * Identify the collection of known animals in the inventory and create cards for them in the ListView
	 */
	private void setListView() {
		
		//Use the sorted list of Rescue Animals to build the ListView
		inventoryView.setItems( RescueAnimalModel.getInstance().getSortedAnimals() );
		
		//Set the ListView cell factory controller. Necessary to ensure each animal card is created correctly
		inventoryView.setCellFactory( _ -> new AnimalCardCell( this.currentUser.isAdmin() ) );
		
	}
	
	@FXML
	void initialize() {
		
		//Set a listener for the search text field to respond to users entering text
		searchKeyword.textProperty().addListener( ( _, _, newValue ) -> {
			applySearchFilter( newValue );
		});
		
	}
	
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
			newAnimalButton.setVisible( currentUser.isAdmin() );
			
		}else { System.out.println( "Provided null user data, please contact support" ); }
				
	}
	
	
	private void applySearchFilter( String text ) {
		
		FilteredList< RescueAnimal > filteredAnimals = RescueAnimalModel.getInstance().getFilteredAnimals();
		
		//Set a predicate filter on the list
		filteredAnimals.setPredicate( animal -> {
			
			//Conditional check for an empty search bar - keep the entire list
			if( text == null || text.trim().isEmpty() ) {
				return true;
			}
			
			String query = text.trim().toLowerCase();
			
			//Apply the property checks for animal name and/or animal type
			boolean nameMatch = animal.getName() != null && animal.getName().toLowerCase().contains( query );
			boolean typeMatch = animal.getAnimalTypeStr() != null && animal.getAnimalTypeStr().toLowerCase().contains( query );
			
			//Return whether a name or animal type match was found. Returning false removes the animal from the filtered list
			return nameMatch || typeMatch;
			
		});
		
	}
	
	/**
	 * Defines the actions to take when the 'Log Out' button is clicked on the main inventory window
	 * @param event
	 */
	@FXML
	private void userLogout( ActionEvent event ) {
		
		searchKeyword.clear();
		
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to route back to the user login window. Close all open windows to start afresh
		StageManager.getInstance().resetToLogin( currentStage, "/view/userLogin.fxml" );
		
	}
	
	@FXML
	private void addNewAnimal( ActionEvent event ) {
		
		searchKeyword.clear();
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to hide the main inventory window and show the Add Animal window
		StageManager.getInstance().openWindow( currentStage, "/view/addAnimal.fxml", null );
		
	}
	
	/**
	 * Defines the actions to take place when the 'Reserve Animal' button is clicked on the main inventory window
	 * @param event
	 */
	@FXML
	private void reserveAnimal( ActionEvent event ) {
		
		searchKeyword.clear();
		Stage currentStage = ( Stage ) ( ( Node ) event.getSource()).getScene().getWindow();
		
		//Use the StageManager service to route to the reserve animal window
		StageManager.getInstance().openWindow( currentStage, "/view/reserveAnimal.fxml", null );
		
	}

}
