package com.example.SwipeFlight.entities.seat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Seat.
 * (interacting with the database)
 */
@Repository
public class SeatRepository {
	
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of the unavailable seats at the given flightID.
	 * @param flightId
	 * @return list of seat strings (for example: ["1A", "2B"]), or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
    public List<String> getUnavailableSeatsForFlightID(Long flightId) throws IllegalArgumentException
    {
    	List<String> unavailableSeats = new ArrayList<String>();
    	/*
    	 * Unavailable seat =
    	 *  1. appears in Passenger_Flight table (-> other user already booked it)
    	 *  OR
    	 *  2. appears in Reserved_Seats table 	(-> other user reserved it)
    	 */
        String sql = "SELECT seat_num "
        			+ "FROM passenger_flight "
        			+ "WHERE flight_id = ? "
        			+ "UNION "
        			+ "SELECT seat_num "
        			+ "FROM reserved_seats "
        			+ "WHERE flight_id = ?; ";
		try {
			unavailableSeats = jdbcTemplate.queryForList(sql, String.class, flightId, flightId);
			return unavailableSeats;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: SeatRepository\t Method: getUnavailableSeatsForFlightID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to unavailable seats for flight.", e);
		}
    }

}
