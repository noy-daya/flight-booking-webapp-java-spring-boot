package com.example.SwipeFlight.entities.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer of Passenger.
 * (interacts with Repository layer)
 */
@Service
public class PassengerService {
	
	@Autowired // dependency injection
	private PassengerRepository passengerRepository;
	
	/**
	 * The method retrieves a list of passengers related to the given bookingID,
	 * from Passenger table.
	 * @param bookingID
	 * @return a list of passengers, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Passenger> getPassengersForBookingID(Long bookingID) throws IllegalArgumentException
	{
		return passengerRepository.getPassengersForBookingID(bookingID);
	}
	
	/**
	 * The method retrieves the number of passengers in the given flightID,
	 * reserved seats are taken into account.
	 * @param flightID
	 * @return number of passengers, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getNumOfPassengersInFlight(Long flightID) throws IllegalArgumentException
	{
		return passengerRepository.getNumOfPassengersInFlight(flightID);
	}

}
