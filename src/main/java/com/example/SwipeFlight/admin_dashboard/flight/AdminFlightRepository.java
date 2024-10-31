package com.example.SwipeFlight.admin_dashboard.flight;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.server.utils.Constants;

/**
 * The class represents Repository layer behind admin's ability add a new flight.
 * (interacting with the database)
 */
@Repository
public class AdminFlightRepository {
	
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method inserts a new Flight into the database.
	 * @param flight- the flight to insert
	 * @param calculatedArrivalDateTime- calculation of the correct arrivalDateTime of the flight,
	 * 		  (based on the departureDate, departureTime and Duration entered by the user)
	 * @throws IllegalArgumentException
	 */
	public void insertFlight(Flight flight, LocalDateTime calculatedArrivalDateTime) throws IllegalArgumentException {
		
		try {
	        // flight properties
	        Duration duration = flight.getDuration();
	        Time departureTime = flight.getDepartureTime();
	        Date departureDate = flight.getDepartureDate();
	
	        // Insert the flight
	        String sql = "INSERT INTO flight " +
	                "(plane_id, departure_airport_id, arrival_airport_id, departure_date, departure_time, " +
	                "arrival_date, arrival_time, ticket_price, duration, status_id) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        jdbcTemplate.update(sql, new Object[]{
	                flight.getPlane().getId(), flight.getDepartureAirport().getId(), flight.getArrivalAirport().getId(),
	                departureDate, departureTime,
	                Date.valueOf(calculatedArrivalDateTime.toLocalDate()),
	                Time.valueOf(calculatedArrivalDateTime.toLocalTime()), // Convert to sql.Date and sql.Time
	                flight.getTicketPrice(), duration.getSeconds(), // Duration in seconds for the rest of the parameters
	                Constants.FLIGHT_STATUS_SCHEDULED
	        });
	
	        // retrieve the generated ID (generated by database)
	        sql = "SELECT LAST_INSERT_ID()";
	        Long newFlightID = jdbcTemplate.queryForObject(sql, Long.class);
	
	        // insert a route for the new flight using generatedId
	        insertNewRouteForFlightID(newFlightID);
	        
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AdminFlightRepository\t Method: insertFlight "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert flight.", e);
		}
	}
	
	/**
	 * The method inserts a route entry for the new flight.
	 * 		- its routeNumber will be the max(route_number) + 1
	 * 		- the new entry will have sequence_num = 1
	 * (initially, every flight has its own route, which means it is considered as direct flight at first)
	 * @param newFlightID
	 * @throws IllegalArgumentException
	 */
	private void insertNewRouteForFlightID(Long newFlightID) throws IllegalArgumentException
	{
		try {
	        String sql = "INSERT INTO route (route_number, flight_id, sequence_number) " +
	                	 "SELECT COALESCE(MAX(route_number), 0) + 1, ?, 1 " +
	                	 "FROM (SELECT route_number FROM route) AS temp";
	        jdbcTemplate.update(sql, newFlightID);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AdminFlightRepository\t Method: insertNewRouteForFlightID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert route.", e);
		}
	}

}
