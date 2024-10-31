package com.example.SwipeFlight.admin_dashboard.route.cancel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.plane.PlaneService;

/**
 * The class represents Service layer behind admin's ability cancel route.
 */
@Service
public class AdminRouteCancelService {
	
	// dependency injections
	@Autowired
	private AdminRouteCancelRepository adminRouteCancelRepository;
	@Autowired
	private FlightService flightService;
	@Autowired
	private PlaneService planeService;
	
	/**
	 * The method handles route cancellation (cancels its flights)
	 * @param routeNumber - number of route to cancel
	 * @throws IllegalArgumentException
	 */
	public void cancelRoute(Long routeNumber) throws IllegalArgumentException {
		
		// iterate through route flights
		List<Flight> flights = flightService.getAllFlightsInRouteNum(routeNumber);
		for (Flight flight: flights)
		{
			// flight will become canceled
			adminRouteCancelRepository.cancelFlight(flight.getId());

			// delete all passenger's information relating to the flight
			adminRouteCancelRepository.deleteFromPassengerFlight(flight.getId());
			
			// update plane available_quantity 
			planeService.updateAvailableQuantity(flight.getPlane().getId(), false); // (false=increment)
		}
	}

}
