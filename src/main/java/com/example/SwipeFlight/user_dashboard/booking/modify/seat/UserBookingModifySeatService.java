package com.example.SwipeFlight.user_dashboard.booking.modify.seat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.booking.BookingService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.seat.SeatService;

/**
 * The class represents Service layer behind user's ability to modify seats of existing booking.
 */
@Service
public class UserBookingModifySeatService {

	@Autowired
	private UserBookingModifySeatRepository userbookingModifySeatRepository;
	
	// dependency injection
	@Autowired
	private SeatService seatService;
	@Autowired
	private BookingService bookingService;
	
	/**
	 * The method checks if the seats in selectedSeatArray can be updated immediately.
	 * It is called during booking modification process:
	 * 	- bookingID has a value.
	 * 	- the requested seats will be updated immediately. (in case it is legal)
	 * @param passengerList
	 * @param flightList
	 * @param selectedSeatArray - the requested seats
	 * @return True - the selected seats are legal, otherwise- they are already taken.
	 * @throws IllegalArgumentException
	 */
	public boolean updateSelectedSeats(List<Passenger> passengerList, List<Flight> flightList,
										String[] selectedSeatArray, Long bookingID)
											throws IllegalArgumentException
	{
	    int seatIndex = 0;
	    for (Passenger passenger: passengerList)
	    {
	    	for (Flight flight: flightList)
	    	{
	    		List<String> unavailableSeats = seatService.getUnavailableSeatsForFlightID(flight.getId());
	    		boolean isSeatAvailable = !(unavailableSeats.contains(selectedSeatArray[seatIndex]));
	    		// seat is available or passenger currently owns it anyways -> available
	    		if (isSeatAvailable ||
	    				passenger.getFlightAndSeats().get(flight.getId()).equals(selectedSeatArray[seatIndex]))
	    			passenger.getFlightAndSeats().put(flight.getId(), selectedSeatArray[seatIndex]);
	    		else // not legal
	    			return false;
	    		seatIndex++;
	    	}
	    	// update seat immediately
	    	userbookingModifySeatRepository.updateSeatsForPassenger(passenger, bookingID);
	    	bookingService.updateLastModify(bookingID);
	    }
	    return true; // legal
	}
}
