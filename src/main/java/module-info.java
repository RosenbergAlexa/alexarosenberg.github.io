module com.snhu.capstone{
	exports com.snhu.capstone.model.user;
	exports com.snhu.capstone.controllers;
	exports com.snhu.capstone.model.exceptions;
	exports com.snhu.capstone.service;
	exports com.snhu.capstone.model;
	exports com.snhu.capstone.model.animal;
	exports com.snhu.capstone;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	
	opens com.snhu.capstone.controllers to javafx.fxml;
	
	opens com.snhu.capstone.model to javafx.base;
	
}