package com.example.SwipeFlight.user_dashboard.booking.modify.seat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Repository layer behind user's ability to modify seats of existing booking.
 * (interacting with the database)
 */
@Repository
public class UserBookingModifySeatRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method updates the seat numbers of entries in Passenger_Flight,
	 * for the given passenger and bookingID.
	 * @param passenger - includes the new seats selection
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void updateSeatsForPassenger(Passenger passenger, Long bookingID) throws IllegalArgumentException
	{
		try {
			String passportId = passenger.getPassportID();
			Map<Long, String> flightAndSeats = passenger.getFlightAndSeats();
	
			// iterate through passenger's flights (in a route)
			for (Map.Entry<Long, String> entry : flightAndSeats.entrySet()) {
				Long curFlightId = entry.getKey();
				String curSeatNum = entry.getValue();
				String sql = "UPDATE passenger_flight " +
							 " SET seat_num = ?" +
							 " WHERE booking_id = ? AND flight_id = ? AND passport_id = ?; " ;
				jdbcTemplate.update(sql, new Object[] { curSeatNum, bookingID, curFlightId, passportId});
			}
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserBookingModifySeatRepository\t Method: updateSeatsForPassenger "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update passenger's seats.", e);
		}
	}
}
