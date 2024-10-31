package com.example.SwipeFlight.user_dashboard.booking.modify.luggage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer behind user's ability modify luggage of existing booking.
 * (interacting with the database)
 */
@Repository
public class UserBookingModifyLuggageRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method updates luggage of the given passenger, for the given booking.
	 * @param passenger - consists the updated luggage values
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void updateLuggageForPassenger(String passportId, Long bookingID, String luggageIdsStr)
										throws IllegalArgumentException
	{
		try {
			String sql = "UPDATE passenger_flight " +
						 "SET luggage_ids = ? " +
						 "WHERE booking_id = ? AND passport_id = ?; " ;
			jdbcTemplate.update(sql, new Object[] { luggageIdsStr, bookingID, passportId});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserBookingModifyLuggageRepository\t Method: updateLuggageForPassenger "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update luggage for passenger.", e);
		}
	}
	
}
