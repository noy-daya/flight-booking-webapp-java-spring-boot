package com.example.SwipeFlight.booking_stages.summary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Service layer of Summary Stage in booking process.
 */
@Service
public class BookingSummaryService {

	@Autowired // dependency injection
	private BookingSummaryRepository bookingSummaryRepository;
	
	/**
	 * The method inserts an entry into Booking table in the database,
	 * and returned the generated booking ID.
	 * @param passengerList
	 * @param routeNumber
	 * @param totalPrice
	 * @param sessionUserID
	 * @return generated booking ID.
	 * @throws IllegalArgumentException
	 */
	public Long insertIntoBooking(List<Passenger>passengerList,
									Long routeNumber, double totalPrice, Long sessionUserID)
										throws IllegalArgumentException
	{
		return bookingSummaryRepository.insertIntoBooking(passengerList, routeNumber, totalPrice, sessionUserID);
	}
}
