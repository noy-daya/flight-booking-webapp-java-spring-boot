package com.example.SwipeFlight.booking_stages.trip;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * The class represents Service layer of Trip Stage in booking process.
 * (validation functions)
 */
@Service
public class BookingTripService {
	
	/**
	 * The method checks validity of passengers form entered by the user.
	 * According to the following conditions:
	 * @param passengerList - the values entered by the user, for each passenger
	 * @param routeNumber - the selected route number
	 * @param result - BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validateTripForm(TripForm tripForm, BindingResult result)
	{
		Long departureAirportID = tripForm.getDepartureAirportID();
		Long arrivalAirportID = tripForm.getArrivalAirportID();
		int numOfPassengers = tripForm.getNumOfPassengers();
		
		if (numOfPassengers == 0)
			result.rejectValue("numOfPassengers", "error.tripForm", "Field is required.");
		if (departureAirportID == null || departureAirportID == 0)
			result.rejectValue("departureAirportID", "error.tripForm", "Field is required.");
		if (arrivalAirportID == null || arrivalAirportID == 0)
			result.rejectValue("arrivalAirportID", "error.tripForm", "Field is required.");
		if (tripForm.getDepartureDate() == null)
			result.rejectValue("departureDate", "error.tripForm", "Field is required.");
		
		return result;
    }
}
