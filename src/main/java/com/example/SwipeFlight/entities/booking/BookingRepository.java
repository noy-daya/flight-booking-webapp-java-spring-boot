package com.example.SwipeFlight.entities.booking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Booking.
 * (interacting with the database to perform operations on Booking table)
 */
@Repository
public class BookingRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of all the bookings of the given user.
	 * @param userID - owner of the bookings
	 * @return list of bookings, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Booking> getBookingsForUserID(Long userID) throws IllegalArgumentException
	{
		List<Booking> result = new ArrayList<Booking>();
		try {
			String sql = "SELECT * FROM booking WHERE user_id = " + userID;
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			
			for (Map<String, Object> row : rows) {
				Booking booking = createBookingObjectFromRow(row);
				result.add(booking);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: getBookingsForUserID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve bookings for user.", e);
		}
	}
	
	/**
	 * The method retrieves a list of all bookings.
	 * @return list of bookings, or an empty list if no rows exist.
	 * @param bookingID
	 * @param userID
	 * @param isCanceled
	 * @throws IllegalArgumentException
	 */
	public List<Booking> getBookingsByFilters(Long bookingID, Long userID, Boolean isCanceled) throws IllegalArgumentException
	{
		List<Booking> result = new ArrayList<Booking>();
		try {
			String sql = "SELECT * " +
						 "FROM booking " +
						 "WHERE is_canceled = " + isCanceled + " ";
			
			if (bookingID != null && bookingID != 0)
				sql += "AND id = " + bookingID + " ";
			if (userID != null && userID != 0)
				sql += "AND user_id = " + userID + " ";
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			
			for (Map<String, Object> row : rows) {
				Booking booking = createBookingObjectFromRow(row);
				result.add(booking);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: getBookingsByFilters "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve bookings by filters.", e);
		}
	}
	
	/**
	 * The method retrieves a booking by its id.
	 * @return booking object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Booking getBookingByID(Long bookingID) throws IllegalArgumentException
	{
		try {
			String sql = "SELECT * FROM booking WHERE id = ?;";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, bookingID);
			List<Booking> result = new ArrayList<Booking>();
			if (rows.isEmpty()) {
				return null;
			}
			for (Map<String, Object> row : rows) {
				Booking booking = createBookingObjectFromRow(row);
				result.add(booking);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: getBookingByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve booking.", e);
		}
	}
	
	/**
	 * The method retrieves routeID for a given bookingID.
	 * @return routeID, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getRouteNumberOfBookingByID(Long bookingID) throws IllegalArgumentException {
		try {
			String sql = "SELECT route_number FROM booking WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, bookingID);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("route_number");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: getRouteNumberOfBookingByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve route number for booking.", e);
		}
	}
	
	/**
	 * The method updates the total price of the given booking ID.
	 * @param bookingID
	 * @param newTotalPrice
	 * @throws IllegalArgumentException
	 */
	public void updateTotalPrice(Long bookingID, double newTotalPrice) throws IllegalArgumentException
	{
		try {
			String sql = "UPDATE booking " +
					 	" SET total_price = ? " +
					 	" WHERE id = ?; " ;
			jdbcTemplate.update(sql, new Object[] { newTotalPrice, bookingID});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: updateTotalPrice "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update total price.", e);
		}
	}
	
	/**
	 * The method last_modify_date for a given bookingID.
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void updateLastModify(Long bookingID) throws IllegalArgumentException
	{
		try {
			String sql = "UPDATE booking " +
						 "SET last_modify_date = NOW() " +
						 "WHERE id = ?; " ;
			jdbcTemplate.update(sql, new Object[] { bookingID});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: BookingRepository\t Method: updateLastModify "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update last modification date.", e);
		}
	}
	
	/**
	 * The method creates a Booking object from a database entry.
	 * @param row- entry from the database
	 * @return booking
	 */
	private Booking createBookingObjectFromRow(Map<String, Object> row) {
		Booking booking = new Booking();
		booking.setId((Long)row.get("id"));
		booking.setUserID((Long)row.get("user_id"));
		booking.setNumOfTickets((int)row.get("num_of_tickets"));
		booking.setTotalPrice((double)row.get("total_price"));
		booking.setBookingDate((Timestamp)row.get("booking_date"));
		booking.setLastModifyDate((Timestamp)row.get("last_modify_date"));
		booking.setCanceled((boolean)row.get("is_canceled"));
		
		// route will be set from service layer
		
		return booking;
	}

}
