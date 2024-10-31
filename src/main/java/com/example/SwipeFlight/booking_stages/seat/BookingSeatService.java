package com.example.SwipeFlight.booking_stages.seat;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.seat.SeatService;

/**
 * The class represents Service layer of Seat Stage in booking process.
 */
@Service
public class BookingSeatService {

	// dependency injections
	@Autowired
	private BookingSeatRepository bookingSeatRepository;
	@Autowired
	private SeatService seatService;

	/**
	 * The method deletes an entry from Reserved_Seats table, for the given parameters.
	 * @param PassportID
	 * @param flightID
	 * @throws IllegalArgumentException
	 */
	public void deleteReservedSeatForPassAndFlight(String PassportID, Long flightID) throws IllegalArgumentException {
		bookingSeatRepository.deleteReservedSeatForPassAndFlight(PassportID, flightID);
	}

	 /**
	  * The method returns the remaining reservation time in Reserved_Seats table,
	  * for the given parameters.
	  * @param PassportID
	  * @param flightID
	  * @return remaining reservation time, or null if no rows exist.
	  * @throws IllegalArgumentException
	  */
	public Timestamp getReservedTimeForPassAndFlight(String PassportID, Long flightID) throws IllegalArgumentException {
		return bookingSeatRepository.getReservedTimeForPassAndFlight(PassportID, flightID);
	}
	
	/**
	 * The method checks if the seats in selectedSeatArray can be reserved.
	 * It is called during booking process (calledByBookingProcess == true):
	 * 	- bookingID value is not known at this point.
	 * 	- the requested seats will be reserved temporarily in Reserved_Seats table. (in case reservation is legal)
	 * @param passengerList
	 * @param flightList
	 * @param selectedSeatArray - the requested seats
	 * @return True - the selected seats are legal, otherwise- they are already taken.
	 */
	public boolean reserveSelectedSeats(List<Passenger> passengerList, List<Flight> flightList,
										String[] selectedSeatArray)
	{
	    int seatIndex = 0;
	    for (Passenger passenger: passengerList)
	    {
	    	for (Flight flight: flightList)
	    	{
	    		List<String> unavailableSeats = seatService.getUnavailableSeatsForFlightID(flight.getId());
	    		boolean isSeatAvailable = !(unavailableSeats.contains(selectedSeatArray[seatIndex]));
	    		if (isSeatAvailable)
	    			passenger.getFlightAndSeats().put(flight.getId(), selectedSeatArray[seatIndex]);
	    		else // not legal
	    			return false;
	    		
	    		//insert seat into Reserved_Seats table
	    		bookingSeatRepository.insertIntoReservedSeat(passenger.getPassportID(), flight.getId(), selectedSeatArray[seatIndex]);
	    		seatIndex++;
	    	}
	    }
	    return true; // legal
	}

}
