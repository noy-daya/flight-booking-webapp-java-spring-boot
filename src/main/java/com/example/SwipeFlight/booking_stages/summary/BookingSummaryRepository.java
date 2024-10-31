package com.example.SwipeFlight.booking_stages.summary;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Repository layer of Summary Stage in booking process,
 * (interacting with the database)
 */
@Repository
public class BookingSummaryRepository {

	// dependency injection
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * The method inserts an entry into Booking table in the database,
	 * and returned the generated booking ID.
	 * @param passengerList
	 * @param routeNumber
	 * @param totalPrice
	 * @param sessionUserID
	 * @return generated booking ID.
	 * @throws IllegalArgumentException
	 */
	public Long insertIntoBooking(List<Passenger>passengerList,
			Long routeNumber, double totalPrice, Long sessionUserID) throws IllegalArgumentException {
		try {
			String sql = "INSERT INTO booking "
						+ "(user_id, route_number, num_of_tickets, total_price, booking_date, last_modify_date, is_canceled) "
						+ "VALUES (?, ?, ?, ?, NOW(), NOW(), 0)";

			KeyHolder keyHolder = new GeneratedKeyHolder(); // to store the auto-generated keys

			// use update method that takes KeyHolder as a parameter
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, sessionUserID);
				ps.setLong(2, routeNumber);
				ps.setInt(3, passengerList.size());
				ps.setDouble(4, totalPrice);
				return ps;
			}, keyHolder);

			// retrieve the auto-generated booking ID from the KeyHolder
			Long bookingId = keyHolder.getKey().longValue();
			return bookingId;
			
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingSummaryRepository\t Method: insertIntoBooking "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert into Booking table.", e);
		}
	}
}
