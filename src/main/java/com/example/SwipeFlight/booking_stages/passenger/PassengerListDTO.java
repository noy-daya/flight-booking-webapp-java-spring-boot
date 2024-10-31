package com.example.SwipeFlight.booking_stages.passenger;

import java.io.Serializable;
import java.util.List;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class is a helper, allowing us to bind a List object in Thymeleaf.
 * Explanation:
 * 		allowing us to retrieve a list of Passenger objects submitted from the view to the controller,
 * 		in which every Passenger object was modified by the user.
 * The class implements Serializable interface
 * 		(because session stores some of the fields, so object conversion to byte array for storage is necessary)
 */
public class PassengerListDTO implements Serializable {
	
    private List<Passenger> passengerList;

	public List<Passenger> getPassengerList() {
		return passengerList;
	}

	public void setPassengerList(List<Passenger> passengerList) {
		this.passengerList = passengerList;
	}
}
