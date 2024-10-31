package com.example.SwipeFlight.entities.flight;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import com.example.SwipeFlight.entities.airport.Airport;
import com.example.SwipeFlight.entities.plane.Plane;

/**
 * The class represents Entity layer of Flight (the properties match with Flight table fields)
 * The class implements Serializable interface
 * 		(because session stores some of the fields, so object conversion to byte array for storage is necessary)
 */
public class Flight implements Serializable {
	
    private Long id;
    private Plane plane;
	private Airport departureAirport;
	private Airport arrivalAirport;
    private Date departureDate;
    private Time departureTime;
    private Date arrivalDate;
	private Time arrivalTime;
	private Duration duration;
    private double ticketPrice;
    private Long statusID;
    
    /* empty constructor */
	public Flight() { }

	/* The method returns value of id */
	public Long getId() {
		return id;
	}

	/* The method updates value of id */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* The method returns value of plane */
    public Plane getPlane() {
		return plane;
	}

    /* The method updates value of plane */
	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	/* The method returns value of departureAirport */
	public Airport getDepartureAirport() {
		return departureAirport;
	}

	/* The method updates value of departureAirport */
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	/* The method returns value of arrivalAirport */
	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	/* The method updates value of arrivalAirport */
	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	/* The method returns value of departureDate */
	public Date getDepartureDate() {
		return departureDate;
	}

	/* The method updates value of departureDate */
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	/* The method returns value of departureTime */
	public Time getDepartureTime() {
		return departureTime;
	}

	/* The method updates value of departureTime */
	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

	/* The method returns value of arrivalDate */
    public Date getArrivalDate() {
		return arrivalDate;
	}

    /* The method updates value of arrivalDate */
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	/* The method returns value of arrivalTime */
	public Time getArrivalTime() {
		return arrivalTime;
	}

	/* The method updates value of arrivalTime */
	public void setArrivalTime(Time arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	/* The method returns value of duration */
	public Duration getDuration() {
		return duration;
	}

	/* The method updates value of duration */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	/* The method returns value of ticketPrice */
	public double getTicketPrice() {
		return ticketPrice;
	}

	/* The method updates value of ticketPrice */
	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	/* The method returns value of statusID */
	public Long getStatusID() {
		return statusID;
	}

	/* The method updates value of statusID */
	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", plane=" + plane + ", departureAirport=" + departureAirport + ", arrivalAirport="
				+ arrivalAirport + ", departureDate=" + departureDate + ", departureTime=" + departureTime
				+ ", arrivalDate=" + arrivalDate + ", arrivalTime=" + arrivalTime + ", duration=" + duration
				+ ", ticketPrice=" + ticketPrice + ", statusID=" + statusID + "]";
	}

}
