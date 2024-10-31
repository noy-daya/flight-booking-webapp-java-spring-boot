package com.example.SwipeFlight.admin_dashboard.flight;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.SwipeFlight.entities.airport.Airport;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.plane.Plane;
import com.example.SwipeFlight.entities.plane.PlaneService;
import com.example.SwipeFlight.server.utils.DateTimeUtils;

/**
 * The class represents Service layer behind admin's ability add a new flight.
 */
@Service
public class AdminFlightService {
	
	// dependency injections
	@Autowired
	private AdminFlightRepository adminFlightRepository;
	@Autowired
	private PlaneService planeService;
	
	/**
	 * The method inserts a new Flight into the database.
	 * @param flight- the flight to insert
	 * @param calculatedArrivalDateTime- calculation of the correct arrivalDateTime of the flight,
	 * 		  (based on the departureDate, departureTime and Duration entered by the user)
	 * @throws IllegalArgumentException
	 */
	public void insertFlight(Flight flight) throws IllegalArgumentException
	{
		// calculate the correct arrivalDateTime of the flight
		LocalDateTime newArrivalDateTime = DateTimeUtils.calculateDateTimeAfterDuration(flight.getDepartureDate(), flight.getDepartureTime(), flight.getDuration());
		
		// insert flight and get the ID of the new inserted row
		adminFlightRepository.insertFlight(flight, newArrivalDateTime);
		
		// update plane available_quantity 
		planeService.updateAvailableQuantity(flight.getPlane().getId(), true); // (true=decrement)
	}
	
	/**
	 * The method checks validity of flight insertion form fields.
	 * @param flight- the values entered by the user (admin)
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validateFlightInsertion(Flight flight, BindingResult result)
	{
		Plane plane = flight.getPlane();
		Airport departureAirport = flight.getDepartureAirport();
		Airport arrivalAirport = flight.getArrivalAirport();
		
		if (plane == null || plane.getId() == 0)
			result.rejectValue("plane.id", "error.flight", "Field is required.");
		if (departureAirport == null || departureAirport.getId() == 0)
			result.rejectValue("departureAirport.id", "error.flight", "Field is required.");
		if (arrivalAirport == null || arrivalAirport.getId() == 0)
			result.rejectValue("arrivalAirport.id", "error.flight", "Field is required.");
		
		return result;
    }

}
