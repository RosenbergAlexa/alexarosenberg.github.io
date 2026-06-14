/*
 * Class: Filterable.java
 * Project: CS-499 Capstone
 * Purpose: Acts as an inheritable interface defining functionality which much be implemented by child classes. The intended use case is inheriting 
 *   classes apply filters to the shared collection of RescueAnimal data. The required class forces all controllers to clear filters to ensure the 
 *   correct data is displayed in the various windows
 *   
 * Version History:
 * - 1.0.0     28 May 2026     ARosenberg     Interface defined
 * 
 */

package com.snhu.capstone.model;

public interface Filterable{
	
	void clearFilters();

}
