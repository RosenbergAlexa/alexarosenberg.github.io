package com.Project2;

public class Dog extends RescueAnimal {

    // Instance variable
    private String breed;

    // Constructor
    public Dog(String name, String breed, String gender, String age,
    String weight, String acquisitionDate, String acquisitionCountry,
	String trainingStatus, boolean reserved, String inServiceCountry) {
        setName(name);
        setBreed(breed);
        setGender(gender);
        setAge(age);
        setWeight(weight);
        setAcquisitionDate(acquisitionDate);
        setAcquisitionLocation(acquisitionCountry);
        setTrainingStatus(trainingStatus);
        setReserved(reserved);
        setInServiceCountry(inServiceCountry);

    }

    // Accessor Method
    public String getBreed() {
        return breed;
    }

    // Mutator Method
    public void setBreed(String dogBreed) {
        breed = dogBreed;
    }
    
    //Prints the dog
    public void printDog() {
    	
    	//Print out summary of dog information
    	System.out.printf( "%s is a %s %s aged %s and %s lbs. Acquired from %s on %s, with service country %s. Current training status is %s.", 
    			getName(), getGender(), this.breed, getAge(), getWeight(), getAcquisitionLocation(), getAcquisitionDate(), getInServiceLocation(),
    			getTrainingStatus());
    	
    	//Print reservation status of dog
    	if( getReserved() ) {
    		System.out.println( " Currently reserved" );
    	}
    	else {
    		System.out.println( " Currently not reserved" );
    	}
    	
    }

}
