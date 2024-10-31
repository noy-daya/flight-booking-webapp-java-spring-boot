package com.example.SwipeFlight.entities.seat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;

/**
 * The class represents Service layer of Seat.
 */
@Service
public class SeatService {

	@Autowired // dependency injection
	private SeatRepository seatRepository;
	
	/**
	 * The method retrieves a list of the unavailable seats at the given flightID.
	 * @param flightId
	 * @return list of seat strings (for example: ["1A", "2B"]), or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<String> getUnavailableSeatsForFlightID(Long flightId) throws IllegalArgumentException {
		 return seatRepository.getUnavailableSeatsForFlightID(flightId);
	}
	
	/**
	 * The method receives a flight, and creates FlightDTO object,
	 * (mapping the flight to its seats matrix, according to each seat state)
	 * @param flight
	 * @return new FlightDTO object for the given flight
	 */
	public FlightDTO setFlightAndSeats(Flight flight)
	{
		Long flightID = flight.getId();
		int numRows = flight.getPlane().getNumOfRows();
		int numSeatsPerRow = flight.getPlane().getNumOfSeatsPerRow();
		List<String> unavailableSeats = getUnavailableSeatsForFlightID(flightID);
		
        // Array to map seat numbers to letters
        char[] seatLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        
		List<List<Seat>> seatList = new ArrayList<>();
		for (int i = 0; i < numRows; i++) {
			List<Seat> row = new ArrayList<>();
			for (int j = 0; j < numSeatsPerRow; j++) {
				 int rowNumber = i + 1;
				 char seat = seatLetters[j];
				 String seatId = rowNumber + "-" + String.valueOf(seat);
				boolean isSeatAvailable = !(unavailableSeats.contains(seatId));
				
				row.add(new Seat(rowNumber, seat, isSeatAvailable)); // Initialize with row, seat number, and not selected
			}
			seatList.add(row);
		}
		return new FlightDTO(flight, seatList);
	}
	
}
