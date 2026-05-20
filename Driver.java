package com.Project2;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	private static ArrayList<Dog> dogList = new ArrayList<Dog>();
	private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();
	// Instance variables (if needed)

	public static void main(String[] args) {
		//Declare locally used variables
		Scanner scnr = new Scanner( System.in );
		String userInput;


		initializeDogList();
		initializeMonkeyList();

		// Add a loop that displays the menu, accepts the users input
		// and takes the appropriate action.
		do {
			//Display menu
			displayMenu();

			userInput = scnr.nextLine();

			//Check if user entry is not a single number or character (invalid entry)
			if( userInput.length() > 1 ) {
				System.out.println( "Entry not recgonized. Please enter a valid menu selection!" );
				continue;
			}

			switch( Character.getNumericValue( userInput.charAt( 0 ) ) ) {
			case 1: //If 1 selected, create new dog
				intakeNewDog( scnr );
				break;

			case 2: //If 2 selected, create new monkey
				intakeNewMonkey( scnr );
				break;

			case 3: //If 3 selected, reserve an animal
				reserveAnimal( scnr );
				break;

			case 4: //If 4 selected, print all dogs
				printAnimals( 1 );
				break;

			case 5: //If 5 selected, print all monkeys
				printAnimals( 2 );
				break;

			case 6: //If 6 selected, print all animals not reserved
				printAnimals( 3 );
				break;

			case 26: //Do nothing, exit case but shouldn't be caught by default case
				break;

			default: //Invalid user entry
				System.out.println( "\n\nEntry not recgonized. Please enter a valid menu selection!\n\n" );
			}

		}while( !userInput.equalsIgnoreCase("q") );

		//Close scanner
		scnr.close();
	}

	// This method prints the menu options
	public static void displayMenu() {
		System.out.println("\n\n");
		System.out.println("\t\t\t\tRescue Animal System Menu");
		System.out.println("[1] Intake a new dog");
		System.out.println("[2] Intake a new monkey");
		System.out.println("[3] Reserve an animal");
		System.out.println("[4] Print a list of all dogs");
		System.out.println("[5] Print a list of all monkeys");
		System.out.println("[6] Print a list of all animals that are not reserved");
		System.out.println("[q] Quit application");
		System.out.println();
		System.out.println("Enter a menu selection");
	}

	// Adds dogs to a list for testing
	public static void initializeDogList() {
		Dog dog1 = new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States");
		Dog dog2 = new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "in service", false, "United States");
		Dog dog3 = new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", true, "Canada");

		dogList.add(dog1);
		dogList.add(dog2);
		dogList.add(dog3);
	}


	// Adds monkeys to a list for testing
	public static void initializeMonkeyList() {
		Monkey monkey1 = new Monkey( "Astrid", "female", "3", "9.5", "07-01-2021", "Rwanda", "Phase I", false, "Africa", "Guenon", "17", "6", "10" );
		Monkey monkey2 = new Monkey( "Chomper", "male", "1", "0.5", "01-15-2022", "Brazil", "intake", false, "Brazil", "Marmoset", "5.25", "3.5", "3" );
		Monkey monkey3 = new Monkey( "Machine Gun", "female", "5", "17", "05-29-2019", "Japan", "Phase III", false, "Japan", "Macaque", "20.6", "0.5", "20.6" );
		Monkey monkey4 = new Monkey( "Rocket Launcher", "female", "6", "16.34", "05-29-2019", "Japan", "in service", false, "Japan", "Macaque", "20.6", "0.5", "20.6" );
		
		monkeyList.add( monkey1 );
		monkeyList.add( monkey2 );
		monkeyList.add( monkey3 );
		monkeyList.add( monkey4 );
	}


	// Complete the intakeNewDog method
	// The input validation to check that the dog is not already in the list
	// is done for you
	public static void intakeNewDog(Scanner scanner) {
		String[] userData = new String[ 10 ];
		String[] userPrompts = new String[] { "breed", "gender", "age", "weight", "acquisition date", "country of origin", "traning state", 
						"reservation status", "service country" };


		//Check to see if the dog is currently in the system. If so, warn user and return without creating duplicate dog entry
		System.out.println("What is the dog's name?");
		String name = scanner.nextLine();
		for(Dog dog: dogList) {
			if(dog.getName().equalsIgnoreCase(name)) {
				System.out.println("\n\nThis dog is already in our system\n\n");
				return; //returns to menu
			}
		}

		//Prompt user to enter rest of data for the new dog. Don't let the user enter invalid data for the reservation state
		for( int i = 0; i < userPrompts.length; ++i ) {
			System.out.println( "What is the dog's " + userPrompts[ i ] + "?" );
			userData[ i ] = scanner.nextLine();

			//Confirm true/false value entered for reservation status
			if( i == 7 ) {
				if( userData[ i ].equalsIgnoreCase( "true" ) || userData[ i ].equalsIgnoreCase( "false" ) ){
					continue;
				}
				else {
					System.out.println( "\n\nThe reservation status of a new dog must be \"true\" or \"false\"\n\n");
					return;
				}
			}
		}

		//Add the new dog to the dog list
		Dog newDog = new Dog( name, userData[ 0 ], userData[ 1 ], userData[ 2 ], userData[ 3 ], userData[ 4 ], userData[ 5 ], userData[ 6 ],
				Boolean.parseBoolean( userData[ 7 ] ), userData[ 8 ] );
		dogList.add( newDog );

	}

	// Complete intakeNewMonkey
	//Instantiate and add the new monkey to the appropriate list
	// For the project submission you must also  validate the input
	// to make sure the monkey doesn't already exist and the species type is allowed
	public static void intakeNewMonkey(Scanner scanner) {
		String[] userData = new String[ 11 ];
		String[] userPrompts = new String[]{ "gender", "age", "weight", "acquisition date", "country of origin", "training state", "reservation status",
				"service country", "height", "tail length", "body length" };

		System.out.println( "What is the monkey's name?" );
		String monkeyName = scanner.nextLine();
		System.out.println( "What is the monkey's species?");
		String monkeySpecies = scanner.nextLine();

		//validate monkey species
		if( !monkeySpecies.equalsIgnoreCase("Capuchin") && !monkeySpecies.equalsIgnoreCase( "Guenon" ) && !monkeySpecies.equalsIgnoreCase("Macaque" )
				&& !monkeySpecies.equalsIgnoreCase("Marmoset" ) && !monkeySpecies.equalsIgnoreCase( "Squirrel Monkey" ) 
				&& !monkeySpecies.equalsIgnoreCase( "Tamarin" ) ) {
			System.out.println( "\n\nWe do not accept this species of monkey for training\n\n" );
			return; //return to menu
		}

		//Check to see if monkey is already in the system. If so, return to menu without adding a new monkey
		for( Monkey knownMonkey : monkeyList ) {
			if( knownMonkey.getName().equalsIgnoreCase( monkeyName ) && knownMonkey.getSpecies().equalsIgnoreCase( monkeySpecies ) ) {
				System.out.println( "\n\nThis monkey is already in our system\n\n" );
				return; //return to menu
			}           	
		}

		//Get rest of data for the monkey to be added
		for( int i = 0; i < userPrompts.length; ++i ) {
			System.out.println( "What is the monkey's " + userPrompts[ i ] + "?" );
			userData[ i ] = scanner.nextLine();

			//Confirm true/false value entered for reservation status
			if( i == 6 ) {
				if( userData[ i ].equalsIgnoreCase( "true" ) || userData[ i ].equalsIgnoreCase( "false" ) ){
					continue;
				}
				else {
					System.out.println( "\n\nThe reservation status of a new monkey must be \"true\" or \"false\"\n\n");
					return;
				}
			}

		}

		//Add the new monkey to the monkeyList
		Monkey newMonkey = new Monkey( monkeyName, userData[ 0 ], userData[ 1 ], userData[ 2 ], userData[ 3 ], userData[ 4 ], userData[ 5 ],
				Boolean.parseBoolean( userData[ 6 ] ), userData[ 7 ], monkeySpecies, userData[ 8 ], userData[ 9 ], userData[ 10 ] );
		monkeyList.add( newMonkey );

	}

	// Complete reserveAnimal
	// You will need to find the animal by animal type and in service country
	public static void reserveAnimal(Scanner scanner) {
		String animalType;
		String country;

		//Get user to provide animal type of animal to reserve
		System.out.println("Please enter the type of animal desired (dog / monkey): " );
		animalType = scanner.nextLine();

		if( !( animalType.equalsIgnoreCase( "dog" ) || animalType.equalsIgnoreCase( "monkey" ) ) ) {
			System.out.println("\n\nThe requested animal type must be \"dog\" or \"monkey\"\n\n" );
			return; //return to menu
		}

		//Get user to provide service country of animal to reserve
		System.out.println( "Please enter the service country of the desired animal: " );
		country = scanner.nextLine();

		//Find the first monkey with a matching service country that is not already reserved, reserve it, then inform user
		if( animalType.equalsIgnoreCase( "monkey" ) ) {

			//Loop over all known monkeys
			for( int i = 0; i < monkeyList.size(); ++i ) {

				//Check if monkey is not reserved and has the correct service country
				if( !monkeyList.get( i ).getReserved() && monkeyList.get( i ).getInServiceLocation().equalsIgnoreCase( country ) ) {
					monkeyList.get( i ).setReserved( true );
					System.out.printf( "Monkey %s successfully reserved for %s\n", monkeyList.get( i ).getName(), monkeyList.get( i ).getInServiceLocation() );
					return; //Return to main menu
				}
			}

			//No available monkey found to reserve
			System.out.printf( "\nNo monkey available in %s\n", country );
		}

		//Find the first dog with a matching service country that is not already reserved, reserve it, then inform the user
		if( animalType.equalsIgnoreCase( "dog" ) ) {

			//Loop over all known dogs
			for( int i = 0; i < dogList.size(); ++i ) {

				//Check if dog is not reserved and has the correct service country
				if( !dogList.get( i ).getReserved() && dogList.get( i ).getInServiceLocation().equalsIgnoreCase( country ) ) {
					dogList.get( i ).setReserved( true );
					System.out.printf( "Dog %s successfully reserved for %s\n", dogList.get( i ).getName(), dogList.get( i ).getInServiceLocation() );
					return; //Return to main menu

				}
			}

			//No available dog found to reserve
			System.out.printf( "\nNo dog available in %s\n", country );
		}

	}

	// Complete printAnimals
	// Include the animal name, status, acquisition country and if the animal is reserved.
	// Remember that this method connects to three different menu items.
	// The printAnimals() method has three different outputs
	// based on the listType parameter
	// dog - prints the list of dogs
	// monkey - prints the list of monkeys
	// available - prints a combined list of all animals that are
	// fully trained ("in service") but not reserved 
	// Remember that you only have to fully implement ONE of these lists. 
	// The other lists can have a print statement saying "This option needs to be implemented".
	// To score "exemplary" you must correctly implement the "available" list.
	public static void printAnimals( int listType ) {

		switch( listType ) {

		case 1: //Print all known dogs - no restrictions
			for( Dog dog : dogList ) {
				dog.printDog();
			}
			break;

		case 2: //Print all known monkeys - no restrictions
			for( Monkey monkey : monkeyList ) {
				monkey.printMonkey();
			}
			break;

		case 3:
			//Print trained & available dogs
			for( Dog currDog : dogList ) {
				if( !currDog.getReserved() && currDog.getTrainingStatus().equalsIgnoreCase( "in service" ) ) {
					currDog.printDog();
				}
			}

			//Print trained & available monkeys
			for( Monkey currMonkey : monkeyList ) {
				if( !currMonkey.getReserved() && currMonkey.getTrainingStatus().equalsIgnoreCase( "in service" ) ) {
					currMonkey.printMonkey();
				}
			}
			break;

		default:
			System.out.println( "Unrecgonized option" );
		}

	}
}

