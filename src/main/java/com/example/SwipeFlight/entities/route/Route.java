package com.example.SwipeFlight.entities.route;

import java.util.List;

import com.example.SwipeFlight.entities.flight.Flight;

/**
 * The class represents Entity layer of Route (the properties match with Route table fields) 
 * A Route can consist a single flight, or two connected flights.
 */
public class Route {

    private Long routeNumber;
	private List<Flight> flights; // All flights in route
	private List<Long> flightIDs; // All flight IDs in route
	private boolean isBookingAllowed;
    private double routePriceForSinglePassenger;  // Total tickets price for all route flights (for 1 passenger)
    											  // (the field does not exist in database, it is used for display in booking)
    							
    /* empty constructor */
	public Route() { }
	
	/* The method returns value of routeNumber */
	public Long getRouteNumber() {
		return routeNumber;
	}

	/* The method updates value of routeNumber */
	public void setRouteNumber(Long routeNumber) {
		this.routeNumber = routeNumber;
	}

	/* The method returns flights */
	public List<Flight> getFlights() {
		return flights;
	}

	/* The method updates value of flights */
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	
	/* The method returns routePriceForSinglePassenger */
	public double getRoutePriceForSinglePassenger() {
		return routePriceForSinglePassenger;
	}

	/* The method updates value of routePriceForSinglePassenger */
	public void setRoutePriceForSinglePassenger(double routePriceForSinglePassenger) {
		this.routePriceForSinglePassenger = routePriceForSinglePassenger;
	}

	/* The method returns  a list of flight IDs in route */
	public List<Long> getFlightIDs() {
		return flightIDs;
	}

	/* The method updates value of flightIDs */
	public void setFlightIDs(List<Long> flightIDs) {
		this.flightIDs = flightIDs;
	}

	public boolean isBookingAllowed() {
		return isBookingAllowed;
	}

	public void setBookingAllowed(boolean isBookingAllowed) {
		this.isBookingAllowed = isBookingAllowed;
	}

	@Override
	public String toString() {
		return "Route [routeNumber=" + routeNumber + ", flights=" + flights + ", flightIDs=" + flightIDs
				+ ", isBookingAllowed=" + isBookingAllowed + ", routePriceForSinglePassenger="
				+ routePriceForSinglePassenger + "]";
	}

}
