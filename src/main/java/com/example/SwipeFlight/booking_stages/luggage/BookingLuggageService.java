package com.example.SwipeFlight.booking_stages.luggage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Service layer of Luggage Stage in booking process.
 */
@Service
public class BookingLuggageService {
	
	/**
	 * The method sets flightAndLuggage attribute, for the given passenger.
	 * @param passenger
	 * @param flightList
	 * @param passengerLuggageIds
	 */
	public void setPassengersLuggage(Passenger passenger, List<Flight> flightList, String[] passengerLuggageIds)
	{
		Map<Long, List<String>> passengerFlightAndLuggage = new HashMap<>();
        if (passengerLuggageIds != null) {
            for (Flight curFlight : flightList) {
                List<String> selectedLuggage = new ArrayList<>();
                for (String luggageId : passengerLuggageIds) {
                    selectedLuggage.add(luggageId);
                }
                // Add selected luggage for the passenger and flight
                passengerFlightAndLuggage.put(curFlight.getId(), selectedLuggage);
            }
        }
        passenger.setFlightAndLuggage(passengerFlightAndLuggage);
	}

}
