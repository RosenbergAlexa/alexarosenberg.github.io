/*
 * Class: animalCardController
 * Project: CS-499 Capstone
 * Purpose: Con
 * 
 * 
 */

package com.snhu.capstone.controllers;

import java.io.IOException;

import com.snhu.capstone.model.animal.RescueAnimal;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class AnimalCardCell extends ListCell< RescueAnimal >{
	
	@FXML private Button editButton;
	@FXML private Label status;
	@FXML private Label subType;
	@FXML private Label species;
	@FXML private Label name;
	
	private boolean adminRights;
	private Node cellLayout;
	
	/**
	 * Class constructor, requires the admin rights flag to be provided
	 * @param isAdmin
	 */
	public AnimalCardCell( boolean isAdmin ) {
		
		this.adminRights = isAdmin;
		loadFXML();
		
	}
	
	/**
	 * Defines how the card class should be loaded
	 */
	private void loadFXML() {
		
		try {
			
			FXMLLoader loader = new FXMLLoader( getClass().getResource( "/view/animalCard.fxml" ) );
			loader.setController( this );
			this.cellLayout = loader.load(); //Save the root layout node returned by the loader
			
		}catch( IOException e ) {
			
			throw new RuntimeException( e );
			
		}
		
	}
	
	/**
	 * Required method that tells the cell builder how to build a single card in the listView. This
	 *    method MUST be overridden
	 */
	@Override
	protected void updateItem( RescueAnimal currAnimal, boolean empty ) {
		
		super.updateItem( currAnimal, empty );
		
		//clean up old bindings to prevent memory leaks and wrong updates on recycled cells
		name.textProperty().unbind();
		subType.textProperty().unbind();
		species.textProperty().unbind();
		status.textProperty().unbind();
		
		
		if( empty || currAnimal == null ) {
			
			setText( null );
			setGraphic( null );
			
		}else {
			
			//Bind text values dynamically to the object's properties
			name.textProperty().bind(  currAnimal.nameProperty() );
			subType.textProperty().bind( currAnimal.animalSubtypeProperty() );
			
			status.textProperty().bind( Bindings.when( currAnimal.reservedProperty() )
					.then( "Reserved" )
					.otherwise( currAnimal.trainingStatusProperty() ) );
			
			species.setText( currAnimal.getAnimalType().getDisplayName() );
			
			//Handle security authorization flags
			if( !this.adminRights ) {
				editButton.setDisable( true );
			}
			
			setGraphic( cellLayout );
			
		}
		
	}
	
	@FXML
	protected void editAnimal( ActionEvent event ) {
		
		RescueAnimal animalToEdit = getItem();
		
		if( animalToEdit != null ) {
			
			//TODO: link behavior to clicking the editAnimal button, admin rights only
			
		}
		
	}

}
