package com.example.SwipeFlight.user_dashboard.booking.cancel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer behind user's ability to cancel an existing booking.
 * (interacting with the database)
 */
@Repository
public class UserBookingCancelRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method executes all the actions as a result of booking cancellation.
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void cancelBooking(Long bookingID) throws IllegalArgumentException {
		try {
			// update is_canceled field
			String sql = "UPDATE booking " + " SET is_canceled = 1, last_modify_date = NOW() " + " WHERE id = ?; ";
			jdbcTemplate.update(sql, new Object[] { bookingID });
	
			// delete rows from Passenger_Flight
			sql = "DELETE FROM passenger_flight " + "WHERE booking_id = ?; ";
			jdbcTemplate.update(sql, new Object[] { bookingID });
	
			// delete rows from Passenger
			sql = "DELETE FROM passenger " + "WHERE booking_id = ?; ";
			jdbcTemplate.update(sql, new Object[] { bookingID });
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserBookingCancelRepository\t Method: cancelBooking "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to cancel booking.", e);
		}
	}
}
