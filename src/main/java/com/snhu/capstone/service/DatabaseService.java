/*
 * Class: DatabaseService.java
 * Project: CS-499 Capstone
 * Purpose: Acts as the service which sends SQL commands to and parses the response to the remote MySQL database
 * 
 * Version History:
 * - 1.0.0     02 June 2026     ARosenberg     Service written. Credentials hardcoded for now, known security flaw
 */

package com.snhu.capstone.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.snhu.capstone.model.animal.AnimalFactory;
import com.snhu.capstone.model.animal.ComponentConfig;
import com.snhu.capstone.model.animal.RescueAnimal;
import com.snhu.capstone.model.exceptions.InvalidAnimalDataException;
import com.snhu.capstone.model.exceptions.InvalidAnimalStatusException;
import com.snhu.capstone.model.exceptions.InvalidAnimalTypeException;
import com.snhu.capstone.model.exceptions.InvalidUserException;
import com.snhu.capstone.model.exceptions.UnknownAnimalException;
import com.snhu.capstone.model.user.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DatabaseService{

	private static final HikariDataSource dataSource;
	
	static {
		
		HikariConfig config = new HikariConfig();
		Properties properties = new Properties();
		ClassLoader loader = DatabaseService.class.getClassLoader();
		try( InputStream input = loader.getResourceAsStream( "config/config.properties" ) ){
			if( input == null ) {
				System.out.println( "Missing config file, cannot connect to the database" );
			}
			
			properties.load( input );
			config.setJdbcUrl( properties.getProperty( "db.uri" ) );		
			config.setUsername( properties.getProperty( "db.username" ) );
			config.setPassword( properties.getProperty( "db.password" ) );
		}catch( IOException e ) {}
		
		//Performance optimizations
		config.addDataSourceProperty( "cachePrepStmts", true ); //Allows for the reuse of compiled SQL statements across queries instead of parsing every time
		config.addDataSourceProperty( "prepStmtCacheSize", "250" ); //Sets the number of compiled SQL statements cached by Hikari per connection to 250
		config.addDataSourceProperty( "prepStmtCacheSqlLimit", "2048" ); //Sets the max length of a SQL statement allowed in a cache
		
		
		//Timeout and pool configurations
		config.setConnectionTimeout( 5000 ); //Timeout db connection attempts after 5 seconds
		config.setIdleTimeout( 600000 );  //Retire connections left idling after 10 minutes
		config.setMaxLifetime( 1800000 ); //Set the maximum connection duration for a single connection in the pool
		config.setValidationTimeout( 3000 );  //Set the time in ms that a connection can be tested for aliveness before it is killed
		config.setMaximumPoolSize( 10 );
		config.setMinimumIdle( 2 );
		
		dataSource = new HikariDataSource( config );
		
	}
	
	public static User validateUser( String username, String password ) throws SQLException, InvalidUserException, IllegalArgumentException {
		
		String sql = "select password_hash from Users where user_name = ?";
		String sql2 = "select a.first_name, a.last_name, a.email, c.role_name "
				+ "from Users as a "
				+ "inner join Permissions as b on a.user_name = b.user_name "
				+ "inner join Roles as c on b.role_id = c.role_id "
				+ "where a.user_name = ?";
		boolean validUser = false;
		
		//Validate the username/password combination
		try( Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement( sql ) ){
			
			//Parameterize the SQL statement for security
			stmt.setString( 1, username );
			
			try( ResultSet rs = stmt.executeQuery() ){
				
				//Read the data pulled from the database and compare the stored password hash to the password entered by the user
				if( rs.next() ) {
					
					BCrypt.Result result = BCrypt.verifyer().verify( password.toCharArray(), rs.getString( 1 ) );
					validUser = result.verified;
					
				}else { //No matching records found in the database, meaning the username is invalid. Sanitize the error message reported
					throw new InvalidUserException( "Username or Password is Incorrect" );
				}
				
			}catch( SQLException | InvalidUserException e ) { throw e; }
			
			if( !validUser ) { //Username is known but password does NOT match. Sanitize the error message reported
				throw new InvalidUserException( "Username or Password is Incorrect" );
			}
			
		}catch( SQLException | InvalidUserException e ) { throw e; }
		
		//Capture information about the validated user to return to the caller
		try( Connection con = dataSource.getConnection();
			 PreparedStatement stmtTwo = con.prepareStatement( sql2 ) ){
			
			//Parameterize the SQL statement for security
			stmtTwo.setString( 1, username );
			
			try( ResultSet rsTwo = stmtTwo.executeQuery() ){
				
				//Read the data pulled from the database and set user information
				if( rsTwo.next() ) {
					
					return new User( rsTwo.getString( 1 ), rsTwo.getString( 2 ), rsTwo.getString( 3 ), rsTwo.getString( 4 ) );
					
				}else { //Something went wrong and somehow there is incomplete or now missing data for the validated user
					throw new InvalidUserException( "Missing user information, please contact support" );	
				}
				
			}catch( SQLException | IllegalArgumentException e ) { throw e; }
			
		}catch( SQLException | IllegalArgumentException e ) { throw e; }
		
	}
	
	/**
	 * Fetches the list of all rescue animals known to the company inventory
	 * @return
	 * @throws SQLException
	 * @throws InvalidAnimalDataException
	 */
	public static List< RescueAnimal > fetchAnimals() throws SQLException, InvalidAnimalDataException{
		
		List< RescueAnimal > inventory = new ArrayList<>();
		String sql = "select a.name, b.type_name, a.gender, a.age, a.weight, a.acquisition_date, a.acquisition_country, c.training_level, "
				+ "c.reservation, a.service_country, b.subtype_name, b.height, b.tail_length, b.body_length"
				+ " from Animals as a "
				+ "inner join AnimalTypes as b on a.type_details = b.type_id "
				+ "inner join Status as c on a.status_details = c.status_id";
		
		try( Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement( sql );
			 ResultSet rs = stmt.executeQuery() ){
			
			//Retrieve results for every matching record in the inventory
			while( rs.next() ) {
				
				//Create an animal config for the current animal record
				ComponentConfig config = new ComponentConfig(
						rs.getString( 1 ),
						rs.getString( 2 ),
						rs.getString( 3 ),
						rs.getInt( 4 ),
						rs.getFloat( 5 ),
						rs.getObject( "acquisition_date", LocalDate.class ),
						rs.getString( 7 ),
						rs.getString( 8 ),
						rs.getBoolean( 9 ),
						rs.getString( 10 ),
						rs.getString( 11 ),
						rs.getFloat( 12 ),
						rs.getFloat( 13 ),
						rs.getFloat( 14 ) );
				
				//Use the animal factory to create a concrete RescueAnimal object, add the result to the output list
				inventory.add( AnimalFactory.createAnimal( config ) );
				
			}
			
		}catch( SQLException | InvalidAnimalDataException e ) { throw e; }
		
		return inventory;
		
	}
	
	/**
	 * Update the reservation status of the provided animal to 'Reserved'
	 * @param animalToReserve
	 * @throws SQLException
	 * @throws UnknownAnimalException
	 */
	public static Void reserveAnimal( RescueAnimal animalToReserve ) throws SQLException, UnknownAnimalException {
		
		int animalId, statusId;
		String sql = "select a.animal_id, a.status_details from Animals as a "
				+ "inner join AnimalTypes as b on a.type_details = b.type_id "
				+ "where a.name = ? and b.type_name = ? and b.subtype_name = ?";
		String update = "update Animals set status_details = ? where animal_id = ?";
				
		//Figure out which animal should have it's reservation status changed
		try( Connection con = dataSource.getConnection(); 
			 PreparedStatement check = con.prepareStatement( sql ) ){
			
			//Parameterize the SQL statement for security
			check.setString( 1, animalToReserve.getName() );
			check.setString( 2, animalToReserve.getAnimalTypeStr() );
			check.setString( 3, animalToReserve.getAnimalSubtype() );
			
			try( ResultSet results = check.executeQuery() ){
				
				if( results.next() ) {
					
					animalId = results.getInt( 1 );
					statusId = results.getInt( 2 );					
					
				}else { //No matching record for the animal that should be 'reserved', flag the error. Sanitize the error message
					throw new UnknownAnimalException( "Something went wrong - Unable to identify the animal requested for reservation" );
				}
				
			}catch( SQLException | UnknownAnimalException e ) { throw e; }
			
		}catch( SQLException | UnknownAnimalException e ) {	throw e; }
		
		//Update the reservation status of the identified animal
		try( Connection conn = dataSource.getConnection();
			 PreparedStatement set = conn.prepareStatement( update ) ){
			
			//Parameterize the SQL statement for security
			set.setInt( 1, ( statusId + 1 ) );
			set.setInt( 2, animalId );
			
			int affectedRows = set.executeUpdate();
			if( affectedRows != 1 ) { //animal_id is unique, so check if 0 or <1 rows were affected by the update command
				
				throw new UnknownAnimalException( "Something went wrong - Unable to correctly update the reservation status of the requested animal" );
				
			}
			
			return null; //Return null since the return type is 'Void'
			
		}catch( SQLException | UnknownAnimalException e ) { throw e; }
		
	}
	
	/**
	 * 
	 * @param newAnimal
	 * @return Saves the new animal represented by the data provided by the ComponentConfig argument to the inventory
	 * @throws SQLException
	 * @throws InvalidAnimalTypeException
	 * @throws InvalidAnimalStatusException
	 * @throws InvalidAnimalDataException 
	 */
	public static Void addAnimal( ComponentConfig newAnimal ) 
			throws SQLException, InvalidAnimalTypeException, InvalidAnimalStatusException, InvalidAnimalDataException {
		
		int typeId = -1, statusId = -1;
		String animalSubType = ( newAnimal.getBreed() != null ) ? newAnimal.getBreed() : newAnimal.getSpecies();
		String findType, addType, findStatus, addAnimal;
		
		//Flag is true when height, tail, and body lengths are all negative
		boolean altAnimalTypeFlag = Math.signum( newAnimal.getHeight() ) < 0 && Math.signum( newAnimal.getTailLength() ) < 0 && Math.signum( newAnimal.getBodyLength() ) < 0;

		Connection con = null;
		PreparedStatement getT = null;
		PreparedStatement getS = null;
		PreparedStatement addT = null;
		PreparedStatement addA = null;
		
		//Set values for the sql command strings
		findStatus = "select status_id from Status where training_level = ? and reservation = ?";
		addAnimal = "insert into Animals ( name, type_details, gender, age, weight, acquisition_date, acquisition_country, service_country, status_details ) "
				+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		if( altAnimalTypeFlag ) {
			findType = "select type_id from AnimalTypes where type_name = ? and subtype_name = ?";
			addType = "insert into AnimalTypes ( type_name, subtype_name ) values ( ?, ? )";
		}else {
			findType = "select type_id from AnimalTypes where type_name = ? and subtype_name = ?"
					+ "and height = ? and tail_length = ? and body_length = ?";
			addType = "insert into AnimalTypes ( type_name, subtype_name, height, tail_length, body_length ) values ( ?, ?, ?, ?, ? )";
		}
		
		try {
			
			//Adding animal data should be put in a rollback statement because if any section fails, we don't want leftover data suck in any of the tables
			con = dataSource.getConnection();
			con.setAutoCommit( false );
			
			//Identify or create the animal's type_id
			while( typeId == -1 ) { 
				
				getT = con.prepareStatement( findType );
				getT.setString( 1, newAnimal.getAnimalType().toString() );
				getT.setString( 2, animalSubType );
				
				if( !altAnimalTypeFlag ) {
					getT.setFloat( 3, newAnimal.getHeight() );
					getT.setFloat( 4, newAnimal.getTailLength() );
					getT.setFloat( 5, newAnimal.getBodyLength() );
				}
				
				try( ResultSet results = getT.executeQuery() ){
					if( results.next() ) { 
						
						typeId = results.getInt( 1 );
						getT.close();
						break; //Exit the loop since typeId was found
						
					}
					
				}catch( SQLException e ) { throw e; }
				
				getT.close(); //Make sure to close the statement before attempting to overwrite it during the next loop cycle
				
				//Insert new type id since one does not already exist
				addT = con.prepareStatement( addType, Statement.RETURN_GENERATED_KEYS );
				
				addT.setString( 1, newAnimal.getAnimalType().toString() );
				addT.setString( 2, animalSubType );
				
				if( !altAnimalTypeFlag ) {

					addT.setFloat( 3, newAnimal.getHeight() );
					addT.setFloat( 4, newAnimal.getTailLength() );
					addT.setFloat( 5, newAnimal.getBodyLength() );
					
				}

				int affectedRows = addT.executeUpdate();
				if( affectedRows != 1 ) { //Check to make sure only one row was added to the table
					
					throw new InvalidAnimalTypeException( "Something went wrong - Unable to register the new animal's type data" );
					
				}
				
				//Identify the auto-generated value of the type_id column associated with the update command just executed
				try( ResultSet generatedKeys = addT.getGeneratedKeys() ){
					if( generatedKeys.next() ) {
						typeId = generatedKeys.getInt( 1 );
					}
				}catch( SQLException e ) {	/*Do nothing, allow typeID to be queried in next loop cycle*/ }
				
				addT.close();
				
			}
			
			//Identify which status id to assign
			getS = con.prepareStatement( findStatus );
			getS.setString( 1, newAnimal.getTrainingStatus() );
			getS.setBoolean( 2, newAnimal.getReserved() );
			
			try( ResultSet results = getS.executeQuery() ){
				
				if( results.next() ) {
					statusId = results.getInt( 1 );
					getS.close();
				}else {
					throw new InvalidAnimalStatusException( "Something went wrong - Unable to register the new animal's status data" );
				}
				
			}catch( SQLException | InvalidAnimalStatusException e ) { throw e; }
			
			getS.close();
			
			//Add animal
			addA = con.prepareStatement( addAnimal );
			
			addA.setString( 1, newAnimal.getName() );
			addA.setInt( 2, typeId );
			addA.setString( 3, newAnimal.getGender().toString() );
			addA.setInt( 4, newAnimal.getAge() );
			addA.setFloat( 5, newAnimal.getWeight() );
			addA.setDate( 6, Date.valueOf( newAnimal.getAcquisitionDate() ) );
			addA.setString( 7, newAnimal.getAcquisitionCountry() );
			addA.setString( 8, newAnimal.getInServiceCountry() );
			addA.setInt( 9, statusId );
			
			int newAnimalCount = addA.executeUpdate();
			if( newAnimalCount != 1 ) {
				throw new InvalidAnimalDataException( "Something went wrong - Unable to register the new animal" );
			}	
			
			addA.close();
			con.commit(); //Commit all changes to the remote database
			
			
		}catch( SQLException | InvalidAnimalTypeException | InvalidAnimalStatusException | InvalidAnimalDataException e ) {
			
			if( con != null ) {
				
				try { 
					con.rollback();
				}catch( SQLException ex ) {
					throw ex;
				}
				
				throw e; //Throw the original error message so the caller can inform the user properly
				
			}
			
		}finally {
			
			//Clean up prepared statement resources
			try {
				
				//Defensively try to close any still open PreparedStatements
				try { if( getT != null ) getT.close(); }catch( SQLException e ) {}
				try { if( getS != null ) getS.close(); }catch( SQLException e ) {}
				try { if( addT != null ) addT.close(); }catch( SQLException e ) {}
				try { if( addA != null ) addA.close(); }catch( SQLException e ) {}
				
				if( con!= null ) {
					
					con.setAutoCommit( true ); //Reset the connection pool state
					con.close();
										
				}
				
			}catch( SQLException e ) { throw e; }
			
		}

		return null; //return null required since the return type is 'Void'
		
	}
	
	
	public static Void editAnimal( RescueAnimal animalToEdit, String newTraining, boolean newReservation ) throws SQLException, UnknownAnimalException, InvalidAnimalDataException {
		
		String sql = "select a.animal_id from Animals as a "
				+ "inner join AnimalTypes as b on a.type_details = b.type_id "
				+ "where a.name = ? and b.type_name = ? and b.subtype_name = ?";
		String update = "update Animals a "
				+ "set a.status_details = ( "
					+ "select s.status_id from Status s "
					+ "where s.training_level = ? and s.reservation = ? ) "
				+ "where a.animal_id = ?";
		int animalId;
		
		//Figure out which animal should have it's reservation status changed
		try( Connection con = dataSource.getConnection();
			 PreparedStatement find = con.prepareStatement( sql ) ){
			
			//Parameterize the SQL statement for security
			find.setString( 1, animalToEdit.getName() );
			find.setString( 2, animalToEdit.getAnimalTypeStr() );
			find.setString( 3, animalToEdit.getAnimalSubtype() );
			
			try( ResultSet results = find.executeQuery() ){
				
				if( results.next() ) {
					
					animalId = results.getInt( 1 );
					
				}else { //No matching record for the animal that was 'edited', flag the error. Sanitize the error message
					throw new UnknownAnimalException( "Something went wrong - Unable to identify the animal requested for a status change" );
				}
				
			}catch( SQLException | UnknownAnimalException e ) { throw e; }
			
		}catch( SQLException | UnknownAnimalException e ) { throw e; }
		
		//Update the reservation and training level status of the indicated animal, as requested
		try( Connection conn = dataSource.getConnection();
			 PreparedStatement set = conn.prepareStatement( update ) ){
			
			//Parameterize the SQL statement for security
			set.setString( 1, newTraining );
			set.setBoolean( 2, newReservation );
			set.setInt( 3, animalId );
			
			int affectedRows = set.executeUpdate();
			if( affectedRows != 1 ) {
				
				throw new InvalidAnimalDataException( "Something went wrong - Unable to correctly update the status of the requested animal" );
				
			}
			
		}catch( SQLException | InvalidAnimalDataException e ) { throw e; }
		
		return null; //return null since the return type is 'Void'
	}
	
	
	/**
	 * Gracefully shut down all open connections in the connection pool when the application is closed or 
	 *    the user logs out
	 */
	public static void shutdown() {
		
		if( dataSource != null && !dataSource.isClosed() ) {
			dataSource.close();			
		}
		
	}
	
}
