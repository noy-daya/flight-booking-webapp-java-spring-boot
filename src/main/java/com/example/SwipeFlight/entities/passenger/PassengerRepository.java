package com.example.SwipeFlight.entities.passenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Passenger.
 * (interacting with the database to perform operations on Passenger and Passenger_Flight tables)
 */
@Repository
public class PassengerRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of passengers related to the given bookingID,
	 * from Passenger table.
	 * @param bookingID
	 * @return a list of passengers, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Passenger> getPassengersForBookingID(Long bookingID) throws IllegalArgumentException
	{
		List<Passenger> result = new ArrayList<Passenger>();
		try {
			String sql = "SELECT * FROM passenger WHERE booking_id = ?";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, bookingID);
			for (Map<String, Object> row : rows) {
				Passenger passenger = createPassengerObjectFromRow(row, bookingID);
				result.add(passenger);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PassengerRepository\t Method: getPassengersForBookingID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve passengers for booking id.", e);
		}
	}

	/**
	 * The method creates a Passenger object from a database entry.
	 * @param row- entry from the database
	 * @return passenger
	 */
	private Passenger createPassengerObjectFromRow(Map<String, Object> row, Long bookingID) {
		Passenger passenger = new Passenger();
		passenger.setPassportID((String) row.get("passport_id"));
		passenger.setFirstName((String) row.get("first_name"));
		passenger.setLastName((String) row.get("last_name"));
		passenger.setGender((String) row.get("gender"));
		setPassengerFlightFromEntry(passenger, bookingID);
		return passenger;
	}
	
	/**
	 * The method retrieves values from Passenger_Flight table, for the given passenger and booking.
	 * (in order to set Passenger attributes: flightAndSeats, flightAndLuggage)
	 * @param passenger
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	private void setPassengerFlightFromEntry(Passenger passenger, Long bookingID) throws IllegalArgumentException
	{
		Map<Long, String> flightAndSeats = new HashMap<>();
		Map<Long, List<String>> flightAndLuggage = new HashMap<>();
		
		try {
			String sql = "SELECT * " +
						 "FROM passenger_flight " +
						 "WHERE booking_id = ? AND passport_id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, bookingID, passenger.getPassportID());
			for (Map<String, Object> row : rows)
			{
		        Long flightID = (Long)row.get("flight_id");
		        
		        // flightAndSeats
		        String seatNum = (String)row.get("seat_num");
		        flightAndSeats.put(flightID, seatNum);
		        
		        // flightAndLuggage
		        List<String> luggageIDsList = Arrays.asList(((String)row.get("luggage_ids")).split(","));
		        flightAndLuggage.put(flightID, luggageIDsList);
			}
			passenger.setFlightAndSeats(flightAndSeats);
			passenger.setFlightAndLuggage(flightAndLuggage);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PassengerRepository\t Method: setPassengerFlightFromEntry "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve passenger flight.", e);
		}
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
		try {
	        String sql = "SELECT COUNT(*) AS num_passengers " +
	                     "FROM (" +
	                     "    SELECT passport_id, flight_id " +
	                     "    FROM Passenger_Flight " +
	                     "    WHERE flight_id = ?" +
	                     "    UNION ALL " +
	                     "    SELECT passport_id, flight_id " +
	                     "    FROM Reserved_Seats " +
	                     "    WHERE flight_id = ?" +
	                     ") AS combined_results";
	        
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, flightID, flightID);
			if (rows.isEmpty()) {
				return (long) 0;
			}
			return (Long) rows.get(0).get("num_passengers");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PassengerRepository\t Method: getNumOfPassengersInFlight "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve num of passengers in flight.", e);
		}
	}

}
