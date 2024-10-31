package com.example.SwipeFlight.entities.flight;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.server.utils.Constants;

/**
 * The class represents Repository layer of Flight.
 * (interacting with the database to perform operations on Flight table)
 */
@Repository
public class FlightRepository
{
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a flight by its id.
	 * @param flightID
	 * @return flight object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Flight getFlightByID(Long flightID) throws IllegalArgumentException {
		try {
			String sql = "SELECT * FROM flight WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, flightID);
			List<Flight> result = new ArrayList<Flight>();
			if (rows.isEmpty()) {
				return null;
			}
			for (Map<String, Object> row : rows) {
				Flight flight = createFlightObjectFromRow(row);
				result.add(flight);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightRepository\t Method: getFlightByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve flight.", e);
		}
	}
	
	/**
	 * The method retrieves a list of all the active (=not canceled) flights which belong to given routeID.
	 * @param routeID
	 * @return list of flights, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Flight> getAllFlightsInRouteNum(Long routeID) throws IllegalArgumentException
	{
		List<Flight> result = new ArrayList<Flight>();
		try {
			String sql = 	"SELECT * FROM flight " +
				 	 		"WHERE status_id != " + Constants.FLIGHT_STATUS_CANCELED + // active (=not canceled)
				 	 			" AND id IN (SELECT flight_id " +
				 	 						" FROM route " +
				 	 						" WHERE route_number = ?) ;";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, routeID);
			for (Map<String, Object> row : rows) {
				Flight flight = createFlightObjectFromRow(row);
				result.add(flight);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightRepository\t Method: getAllFlightsInRouteNum "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve flights of given route.", e);
		}
	}
	
	/**
	 * The method retrieves planeID for a given flightID.
	 * @param flightID
	 * @return plane id, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getPlaneIdOfFlightByID(Long flightID) throws IllegalArgumentException {
		try {
			String sql = "SELECT plane_id FROM flight WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, flightID);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("plane_id");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightRepository\t Method: getPlaneIdOfFlightByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve plane id for flight id.", e);
		}
	}
	
	/**
	 * The method retrieves departureAirportID for a given flightID.
	 * @param flightID
	 * @return departureAirportID, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getDepartureAirportIdOfFlightByID(Long flightID) throws IllegalArgumentException
	{
		try {
			String sql = "SELECT departure_airport_id FROM flight WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, flightID);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("departure_airport_id");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightRepository\t Method: getDepartureAirportIdOfFlightByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve plane id for flight id.", e);
		}
	}
	
	/**
	 * The method retrieves arrivalAirportID for a given flightID.
	 * @param flightID
	 * @return arrivalAirportID, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getArrivalAirportIdOfFlightByID(Long flightID) throws IllegalArgumentException
	{
		try {
			String sql = "SELECT arrival_airport_id FROM flight WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, flightID);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("arrival_airport_id");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightRepository\t Method: getArrivalAirportIdOfFlightByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve plane id for flight id.", e);
		}
	}
	
	/**
	 * The method creates a Flight object from a database entry.
	 * @param row- entry from the database
	 * @return flight
	 */
	private Flight createFlightObjectFromRow(Map<String, Object> row) {
		Flight flight = new Flight();
		flight.setId((Long)row.get("id"));
		
		// (plane will be set from service layer)
		// (departure airport will be set from service layer)
		// (arrival airport will be set from service layer)
		
		flight.setDepartureDate((Date) row.get("departure_date"));
		flight.setDepartureTime((Time) row.get("departure_time"));
		flight.setArrivalDate((Date) row.get("arrival_date"));
		flight.setArrivalTime((Time) row.get("arrival_time"));
		flight.setTicketPrice((double) row.get("ticket_price"));
		flight.setDuration((Duration) Duration.ofSeconds((long) row.get("duration")));
		flight.setStatusID((Long) row.get("status_id"));
		return flight;
	}
	 
}
