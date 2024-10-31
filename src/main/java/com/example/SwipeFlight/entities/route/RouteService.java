package com.example.SwipeFlight.entities.route;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;

/**
 * The class represents Service layer of Route.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class RouteService {

	// dependency injections
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private FlightService flightService;
	
	/**
	 * The method retrieves a list of all the routes, including the flightIDs belong to each one.
	 * 		if no rows are found -> returns null
	 */
	public List<Route> getAllRoutes() {
		
		List<Route> result = routeRepository.getAllRoutes();
		for (Route route: result)
		{
			// set flights list
			setFlightsListOfRoute(route);
		}
		return result;
	}
	
	/**
	 * The method retrieves a list of all the upcoming routes, including the flightIDs belong to each one.
	 *		if no rows are found -> returns null.
	 * Definition: Upcoming route =
	 * 		Relate to a flight which its sequence_num = 1 AND its departure date + time has not passed yet.
	 */
	public List<Route> getAllUpcomingRoutes() {
		List<Route> result = routeRepository.getAllUpcomingRoutes();
		for (Route route: result)
		{
			// set flights list
			setFlightsListOfRoute(route);
		}
		return result;
	}
	
	/**
	 * The method retrieves all entries in Route table, that meet the requirements
	 * of the given parameters. (filters)
	 * According to the conditions described.
     * @param departureAirportID
     * @param arrivalAirportID
     * @param departureDate
     * @param arrivalDate
     * @param numOfPassengers
	 * @return list of entries, or empty list if no rows exist.
	 */
	public List<Route> getRoutesByFilters(Long routeNumber, Long departureAirportID, Long arrivalAirportID, Date departureDate, Date arrivalDate)
	{
		List<Route> result = routeRepository.getRoutesByFilters(routeNumber, departureAirportID, arrivalAirportID, departureDate, arrivalDate);
		for (Route route: result)
		{
			// set flights list
			setFlightsListOfRoute(route);
		}
		return result;
	}

	/**
	 * The method retrieves a route by its routeNumber.
	 * 		if no rows are found -> returns null
	 * The method sets properties: flights(list of objects), totalPrice of the element.
	 */
	public Route getRouteByRouteNumber(Long routeNumber)
	{
		// get Route object
		List<Route> routes = getAllRoutes();
		for (Route route:routes)
		{
			if(route.getRouteNumber().equals(routeNumber))
			{
				// set flights list
				setFlightsListOfRoute(route);
				return route;
			}
		}
		return null;
	}
	
	/**
	 * The method sets the flights list of the given route
	 * @param flight
	 */
	public void setFlightsListOfRoute(Route route)
	{
        List<Flight> flights = route.getFlightIDs().stream()
                .map(flightService::getFlightByID)
                .collect(Collectors.toList());
        		route.setFlights(flights);
	}
	
	/**
	 * The method updates is_booking_allowed field for the given route.
	 * @param routeNumber
	 * @param allowBooking
	 */
	public void updateAllowBooking(Long routeNumber, boolean allowBooking)
	{
		routeRepository.updateAllowBooking(routeNumber, allowBooking);
	}

}
