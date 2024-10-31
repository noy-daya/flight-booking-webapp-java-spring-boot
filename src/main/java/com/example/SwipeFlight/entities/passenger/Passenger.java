package com.example.SwipeFlight.entities.passenger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class represents Entity layer of Passenger
 * (the properties relate to Passenger and Passenger_Flight tables)
 * The class implements Serializable interface
 * 		(because session stores Passenger objects, so object conversion to byte array for storage is necessary)
 */
@SuppressWarnings("serial")
public class Passenger implements Serializable
{
    private String passportID;
    private String firstName;
    private String lastName;
    private String gender;
    private Map<Long, String> flightAndSeats = new HashMap<>(); 		// key: flightID,  value: seat (for example: "1-A")
    private Map<Long, List<String>> flightAndLuggage = new HashMap<>(); // key: flightID,  value: [luggage id,luggage id,...] (can have multiple in one flight) 
    
	/* The method returns value of passportID */
	public String getPassportID() {
		return passportID;
	}
	
	/* The method updates value of passportID */
	public void setPassportID(String passportID) {
		this.passportID = passportID;
	}
	
    /* The method returns value of id */
	public String getFirstName() {
		return firstName;
	}
	
	/* The method updates value of firstName */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/* The method returns value of lastName */
	public String getLastName() {
		return lastName;
	}
	
	/* The method updates value of lastName */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/* The method returns value of gender */
	public String getGender() {
		return gender;
	}
	
	/* The method updates value of gender */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/* The method returns value of flightAndSeats */
	public Map<Long, String> getFlightAndSeats() {
		return flightAndSeats;
	}
	
	/* The method updates value of flightAndSeats */
	public void setFlightAndSeats(Map<Long, String> flightAndSeats) {
		this.flightAndSeats = flightAndSeats;
	}
	
	/* The method returns value of flightAndLuggage */
	public Map<Long, List<String>> getFlightAndLuggage() {
		return flightAndLuggage;
	}
	
	/* The method updates value of flightAndLuggage */
	public void setFlightAndLuggage(Map<Long, List<String>> flightAndLuggage) {
		this.flightAndLuggage = flightAndLuggage;
	}
    
    
}
