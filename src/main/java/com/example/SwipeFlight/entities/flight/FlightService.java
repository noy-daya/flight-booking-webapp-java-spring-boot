package com.example.SwipeFlight.entities.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.airport.Airport;
import com.example.SwipeFlight.entities.airport.AirportService;
import com.example.SwipeFlight.entities.plane.Plane;
import com.example.SwipeFlight.entities.plane.PlaneService;

/**
 * The class represents Service layer of Flight.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class FlightService {

	// dependency injections
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private AirportService airportService;
	@Autowired
	private PlaneService planeService;
	
	/**
	 * The method retrieves a flight by its id.
	 * The method sets properties: plane(object), departureAirport(object), arrivalAirport(object) of the element.
	 * @param flightID
	 * @return flight object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Flight getFlightByID(Long flightID) throws IllegalArgumentException
	{
		// get Flight object
		Flight flight = flightRepository.getFlightByID(flightID);
		// update properties which are objects
		setPlaneOfFlight(flight);
		setDepartureAirportOfFlight(flight);
		setArrivalAirportOfFlight(flight);
		return flight;
	}
	
	/**
	 * The method retrieves a list of all the active (=not canceled) flights which belong to given routeID.
	 * The method sets properties: plane(object), departureAirport(object), arrivalAirport(object) of each element.
	 * @param routeID
	 * @return list of flights, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Flight> getAllFlightsInRouteNum(Long routeID) throws IllegalArgumentException
	{
		List<Flight> result = flightRepository.getAllFlightsInRouteNum(routeID);
		for (Flight flight: result)
		{
			// update properties which are objects
			setPlaneOfFlight(flight);
			setDepartureAirportOfFlight(flight);
			setArrivalAirportOfFlight(flight);
		}
		return result;
	}
	
	/**
	 * The method retrieves planeID for a given flightID.
	 * @param flightID
	 * @return plane id, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public void setPlaneOfFlight(Flight flight) throws IllegalArgumentException
	{
		Long planeID = flightRepository.getPlaneIdOfFlightByID(flight.getId());
		Plane plane = (Plane)planeService.getPlaneByID(planeID);
		flight.setPlane(plane);
	}
	
	/**
	 * The method sets the departure airport of the given flight
	 * @param flight
	 * @throws IllegalArgumentException
	 */
	public void setDepartureAirportOfFlight(Flight flight) throws IllegalArgumentException
	{
		Long departureAirportID = flightRepository.getDepartureAirportIdOfFlightByID(flight.getId());
		Airport airport = (Airport)airportService.getAirportByID(departureAirportID);
		flight.setDepartureAirport(airport);
	}
	
	/**
	 * The method sets the arrival airport of the given flight
	 * @param flight
	 * @throws IllegalArgumentException
	 */
	public void setArrivalAirportOfFlight(Flight flight) throws IllegalArgumentException
	{
		Long arrivalAirportID = flightRepository.getArrivalAirportIdOfFlightByID(flight.getId());
		Airport airport = (Airport)airportService.getAirportByID(arrivalAirportID);
		flight.setArrivalAirport(airport);
	}

}
