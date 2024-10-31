package com.example.SwipeFlight.user_dashboard.booking.modify.luggage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Service layer behind user's ability to modify luggage of existing booking.
 */
@Service
public class UserBookingModifyLuggageService {

	@Autowired // dependency injection
	private UserBookingModifyLuggageRepository userBookingModifyLuggageRepository;
	
	/**
	 * The method updates luggage of the given passenger, for the given booking.
	 * @param passenger - consists the updated luggage values
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void updateLuggageForPassenger(Passenger passenger, Long bookingID) throws IllegalArgumentException
	{
		// iterate through each flight (key of the map),
		// for each one:
		//      1. retrieve passenger's luggageIDs string (for example: "1,2,3")
		//		2. update entry in Passenger_flight table
		// get passenger's fields
		String passportId = passenger.getPassportID();
		Map<Long, String> flightAndSeats = passenger.getFlightAndSeats();
		Map<Long, List<String>> flightAndLuggage = passenger.getFlightAndLuggage();

		// iterate through each flight (key of the map)
		for (Map.Entry<Long, String> entry : flightAndSeats.entrySet()) {
			
			Long flightId = entry.getKey();
			
			// step (1)
			List<String> luggageIds = flightAndLuggage.getOrDefault(flightId, List.of());
			String luggageIdsStr = String.join(",", luggageIds);

			// step (2)
			userBookingModifyLuggageRepository.updateLuggageForPassenger(passportId, bookingID, luggageIdsStr);
		}
	}
}
