package com.example.SwipeFlight.entities.seat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;

/**
 * The class displays views related to seats.
 */
@Controller
public class SeatController {
	
	// dependency injections
	@Autowired
	private FlightService flightService;
	@Autowired
	private SeatService seatService;
	
	/**
	 * The method displays the current state of the seats in the given flightId.
	 * (the return value of this method = response body of the request)
	 * @param flightId
	 * @return List of seats.
	 */
	@GetMapping("/fetchSeatAvailability{flightId}")
	@ResponseBody
	public List<Seat> fetchSeatAvailability(@RequestParam("flightId") Long flightId) {
	    List<Seat> seatAvailabilityList = new ArrayList<>();
	    
	    // retrieve flight information for flightId
	    Flight flight = flightService.getFlightByID(flightId);
	    if (flight != null) {
	        int numRows = flight.getPlane().getNumOfRows();
	        int numSeatsPerRow = flight.getPlane().getNumOfSeatsPerRow();
	        List<String> unavailableSeats = seatService.getUnavailableSeatsForFlightID(flightId);
	        
	        // Array to map seat numbers to letters
	        char[] seatLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	        
	        // Iterate over each seat in the flight and determine its availability
	        for (int i = 0; i < numRows; i++) {
	            for (int j = 0; j < numSeatsPerRow; j++) {
	                int rowNumber = i + 1;
	                char seat = seatLetters[j];
	                String seatId = rowNumber + "-" + String.valueOf(seat);
	                boolean isSeatAvailable = !(unavailableSeats.contains(seatId));
	                seatAvailabilityList.add(new Seat(rowNumber, seat, isSeatAvailable));
	            }
	        }
	    }
	    return seatAvailabilityList;
	}

}
