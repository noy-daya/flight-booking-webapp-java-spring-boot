package com.example.SwipeFlight.booking_stages.passenger;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Repository layer of Passenger Stage in booking process,
 * including passengers insertion in case the booking was confirmed by the user.
 * (interacting with the database)
 */
@Repository
public class BookingPassengerRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method inserts an entry into Passenger table, under the given bookingID.
	 * @param passenger - to insert
	 * @param bookingID - reference to the booking in which the passenger appears in
	 * @throws IllegalArgumentException
	 */
	public void insertIntoPassenger(Passenger passenger, Long bookingID) throws IllegalArgumentException {
		try {
			// get passenger's fields
			// (excluding flightAndSeats and flightAndLuggage which relate to Passenger_Flight table)
			String passportId = passenger.getPassportID();
			String firstName = passenger.getFirstName();
			String lastName = passenger.getLastName();
			String gender = passenger.getGender();
			
			// insert an entry
			String sql = "INSERT INTO passenger " +
							"(booking_id, passport_id, first_name, last_name, gender) " +
						 "VALUES (?, ?, ?, ?, ?); ";
			jdbcTemplate.update(sql, new Object[] { bookingID, passportId, firstName, lastName, gender});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingPassengerRepository\t Method: insertIntoPassenger "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert into Passenger table.", e);
		}
	}
	
	/**
	 * The method inserts an entry into Passenger_Flight table, using the given parameters.
	 * @param bookingID - reference to the booking in which the passenger appears in
	 * @param flightId - current flight
	 * @param seatStr - passenger's seat at the given flight (for example: "1-B")
	 * @param luggageIdsStr - passenger's luggage at the given flight (for example: "1,2,3")
	 * @throws IllegalArgumentException
	 */
	public void insertIntoPassengerFlight(Long bookingID, String passportId,
										  Long flightId, String seatStr, String luggageIdsStr) throws IllegalArgumentException
	{
		try {
			String sql = "INSERT INTO passenger_flight " +
						 	"(booking_id, passport_id, flight_id, seat_num, luggage_ids) " +
						 "VALUES (?, ?, ?, ?, ?); ";
			jdbcTemplate.update(sql, new Object[] { bookingID, passportId, flightId, seatStr, luggageIdsStr });
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingPassengerRepository\t Method: insertIntoPassengerFlight "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert into Passenger_Flight table.", e);
		}
	}
	
	/**
	 * The method checks if the passenger is already booked for the given route.
	 * @param passportID - of the passenger
	 * @param routeNum
	 * @return True - the passenger has a booking, otherwise- False.
	 * @throws IllegalArgumentException
	 */
	public boolean isPassengerBookedToRouteNum(String passportID, Long routeNum) throws IllegalArgumentException
	{
		int count_result = 0;
		try {
			String sql = "SELECT COUNT(*) AS passenger_exists " +
						 "FROM passenger_flight pf " +
						 "JOIN route r ON pf.flight_id = r.flight_id " +
						 "WHERE pf.passport_id = '" + passportID + "' " +
						 		"AND r.route_number = ?; ";
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, routeNum);
	        Long countResultLong = (Long) rows.get(0).get("passenger_exists");
	        count_result = countResultLong.intValue();
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingPassengerRepository\t Method: isPassengerBookedToRouteNum "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed counting from Passenger_Flight table.", e);
		}
	    if (count_result == 0)
	        return false;
	    return true;
	}
	
}
