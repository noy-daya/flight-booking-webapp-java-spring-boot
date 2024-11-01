package com.example.SwipeFlight.admin_dashboard.route.add;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.route.Route;

/**
 * The class represents Service layer behind admin's ability to add flights to an existing route.
 */
@Service
public class AdminRouteAddFlightService {

	 // dependency injections
	@Autowired
	private AdminRouteAddFlightRepository adminRouteAddFlightRepository;
	@Autowired
	private FlightService flightService;
	
	/**
	 * The method retrieves the next sequence number (for the new flight added to the route),
	 * sets plane, departureAirport, arrivalAirport.
	 * @param routeNumber
	 * @return the next sequence number, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Flight> getSuggestedFlightsToAdd(Route route) throws IllegalArgumentException
	{
		List<Flight> result = adminRouteAddFlightRepository.getSuggestedFlightsToAdd(route);
		for (Flight flight: result)
		{
			flightService.setPlaneOfFlight(flight);
			flightService.setDepartureAirportOfFlight(flight);
			flightService.setArrivalAirportOfFlight(flight);
		}
		return result;
	}
	
	/**
	 * The method adds a flight into the given route in the database. (the flight will become last in sequence)
	 * Meaning: the route becomes a sequence of flights (=connection flight)
	 * @param route- the route (the flight to add appears in route.getFlights().get(0))
	 * @throws IllegalArgumentException
	 */
	public void addFlightToRoute(Route route) throws IllegalArgumentException {
		// Retrieve the sequence number for the additional flight (generated by database)
		Long nextSequenceNumber = adminRouteAddFlightRepository.retrieveNextSequenceNumber(route.getRouteNumber());
		
		if (nextSequenceNumber == null || route.getFlights() == null)
			return;
		
		Long flightIDToAdd = route.getFlights().get(0).getId();
		
		// flight to add is no longer direct (it's now added to an existing route) 
		// 1. remove its initial row from route
		adminRouteAddFlightRepository.breakDirectRouteForFlight(flightIDToAdd);
		
		// 2. insert new line in sequence into route
		adminRouteAddFlightRepository.addFlightToSequence(route.getRouteNumber(), flightIDToAdd, nextSequenceNumber);
	}
	
	/**
	 * The method checks validity of adding a flight to route.
	 * @param user- the values entered by the user (admin)
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validateAddFlightToRoute(Route route, BindingResult result)
	{
		Flight flightToAdd = route.getFlights().get(0);
		
		if(flightToAdd == null || flightToAdd.getId() == null || flightToAdd.getId() == 0)
			result.rejectValue("flights[0].id", "error.route", "Please select a flight.");
		
		return result;
    }
}
