package com.example.SwipeFlight.booking_stages.trip;

import java.io.Serializable;
import java.sql.Date;

/**
 * Helper class: for Trip form fields.
 * The class implements Serializable interface
 * 		(because session stores some of the fields, so object conversion to byte array for storage is necessary)
 */
public class TripForm implements Serializable {
	
	private Long departureAirportID;
	private Long arrivalAirportID;
    private Date departureDate;
    private int numOfPassengers;
    
    /* The method returns value of departureAirportID */
	public Long getDepartureAirportID() {
		return departureAirportID;
	}
	
	/* The method updates value of departureAirportID */
	public void setDepartureAirportID(Long departureAirportID) {
		this.departureAirportID = departureAirportID;
	}
	
	/* The method returns value of arrivalAirportID */
	public Long getArrivalAirportID() {
		return arrivalAirportID;
	}
	
	/* The method updates value of arrivalAirportID */
	public void setArrivalAirportID(Long arrivalAirportID) {
		this.arrivalAirportID = arrivalAirportID;
	}
	
	/* The method returns value of departureDate */
	public Date getDepartureDate() {
		return departureDate;
	}
	
	/* The method updates value of departureDate */
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
	/* The method returns value of numOfPassengers */
	public int getNumOfPassengers() {
		return numOfPassengers;
	}
	
	/* The method updates value of numOfPassengers */
	public void setNumOfPassengers(int numOfPassengers) {
		this.numOfPassengers = numOfPassengers;
	}
}
