package com.example.SwipeFlight.entities.route;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.server.utils.Constants;

/**
 * The class represents Repository layer of Route.
 * (interacting with the database to perform operations on Route table)
 */
@Repository
public class RouteRepository
{
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of all the routes, including the flightIDs belong to each one.
	 * @return list of route, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Route> getAllRoutes() throws IllegalArgumentException {
		List<Route> result = new ArrayList<>();
		try {
		    String sql = "SELECT "
		    			+ "    r.route_number, r.is_booking_allowed, "
		    			+ "    SUM(f.ticket_price) AS total_ticket_price_for_one_pass, "
		    			+ "    GROUP_CONCAT(f.id) AS flight_ids "
		    			+ "FROM Route r "
		    			+ "JOIN Flight f ON r.flight_id = f.id "
		    			+ "GROUP BY r.route_number, r.is_booking_allowed; ";
		    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		    for (Map<String, Object> row : rows) {
		        Route route = new Route();
		        route.setRouteNumber((Long) row.get("route_number"));
		        
		        route.setBookingAllowed((boolean) row.get("is_booking_allowed"));
		        
		        String flightIdsStr = (String) row.get("flight_ids");
		        List<Long> flightIds = Arrays.stream(flightIdsStr.split(","))
		                                     .map(Long::parseLong)
		                                     .collect(Collectors.toList());
		        route.setFlightIDs(flightIds);
		        route.setRoutePriceForSinglePassenger((double)row.get("total_ticket_price_for_one_pass"));
		        
		        // flights list will be set in service
		        
		        result.add(route);
		    }
		    return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: RouteRepository\t Method: getAllRoutes "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to all routes.", e);
		}
	}
	
	/**
	 * The method retrieves a list of all the upcoming routes, including the flightIDs belong to each one.
	 * @return list of routes, or an empty list if no rows found.
	 * @throws IllegalArgumentException
	 */
	public List<Route> getAllUpcomingRoutes() throws IllegalArgumentException {
		
		 /* Definition: Upcoming route =
			Relate to a flight which its sequence_num = 1 AND its departure date + time has not passed yet. */
	    List<Route> result = new ArrayList<>();		 
		try {
		    String sql = "SELECT "
		    			+ "    r.route_number, "
		    			+ "	   r.is_booking_allowed, "
		    			+ "    GROUP_CONCAT(r.flight_id) AS flight_ids "
		    			+ "FROM route r "
		    			+ "JOIN flight f ON r.flight_id = f.id "
		    			+ "WHERE "
		    			+ "    (r.sequence_number = 1 AND TIMESTAMP(CONCAT(f.departure_date, ' ', f.departure_time)) > NOW()) "
		    			+ "    OR "
		    			+ "    (r.sequence_number > 1) "
		    			+ "GROUP BY r.route_number, r.is_booking_allowed; ";
		    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		    for (Map<String, Object> row : rows) {
		        Route route = new Route();
		        route.setRouteNumber((Long) row.get("route_number"));

				Integer isBookingAllowedString = (Integer) row.get("is_booking_allowed");
				boolean isBookingAllowed = (isBookingAllowedString.equals(1))?true : false;
				route.setBookingAllowed(isBookingAllowed);
		        
		        String flightIdsStr = (String) row.get("flight_ids");
		        List<Long> flightIds = Arrays.stream(flightIdsStr.split(","))
		                                     .map(Long::parseLong)
		                                     .collect(Collectors.toList());
		        route.setFlightIDs(flightIds);
		        
		        // flight object will be set in service
		        // route_price will be set in service
		        
		        result.add(route);
		    }
		    return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: RouteRepository\t Method: getAllUpcomingRoutes "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve upcoming routes.", e);
		}
	}
	
	/**
	 * The method retrieves all entries in Route table, that meet the requirements
	 * of the given parameters (filters).
	 * According to the conditions described.
     * @param departureAirportID
     * @param arrivalAirportID
     * @param departureDate
     * @param arrivalDate
	 * @return list of entries, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Route> getRoutesByFilters(Long routeNumber, Long departureAirportID, Long arrivalAirportID,
										Date departureDate, Date arrivalDate) throws IllegalArgumentException
	{
		List<Route> result = new ArrayList<>();
		/*
		* Retrieve all entries in Route, following the conditions:
		*	1.  if there is only one flight related to this route_number:
		*	     the flight_id is a flight with:
		*	     the given departure_airport_id, arrival_airport_id, departure_date.
		*	    
		*	2. if there are two flights related to this route_number:
		*	     a. the line with flight_id with sequence_number = 1 is a flight with:
		*	        the given departure_airport_id, departure_date
		*	     and
		*	     b. the line with flight_id with sequence_number = 2 is a flight with:
		*	        the given arrival_airport_id
		* Notation:
		* - each one of the values can be null or 0
		*/
		String sql = "SELECT route_number, "
				+ 	 		"is_booking_allowed, " 
				+			"GROUP_CONCAT(flight_id) AS flight_ids "
				+ "FROM ( "
				+ "    SELECT r.route_number, "
				+ "			  r.is_booking_allowed, "
				+ "			  r.flight_id "
				+ "    FROM route r "
				+ "    JOIN flight f ON r.flight_id = f.id "
				+ "	   WHERE f.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " ";
		
		if (routeNumber != null && routeNumber != 0)
			sql += "    	AND r.route_number =" + routeNumber + " ";
		if (departureAirportID != null && departureAirportID != 0)
			sql += "    	AND f.departure_airport_id =" + departureAirportID + " ";
		if (arrivalAirportID != null && arrivalAirportID != 0)
			sql += "    	AND f.arrival_airport_id =" + arrivalAirportID + " ";
		if (departureDate != null && !departureDate.equals(Date.valueOf("1970-01-01")))
			sql += "    	AND f.departure_date ='" + departureDate + "'" + " ";
		if (arrivalDate != null && !arrivalDate.equals(Date.valueOf("1970-01-01")))
			sql += "    	AND f.arrival_date ='" + arrivalDate + "'" + " ";
		
		sql += "    UNION "
				+ "     "
				+ "    SELECT r.route_number, r.is_booking_allowed, "
				+ "           CONCAT(f1.id, ',', f2.id) AS flight_ids "
				+ "    FROM route r "
				+ "    JOIN flight f1 ON r.flight_id = f1.id AND r.sequence_number = 1 "
				+ "    JOIN route r2 ON r.route_number = r2.route_number AND r2.sequence_number = 2 "
				+ "    JOIN flight f2 ON r2.flight_id = f2.id "
				+ "    WHERE EXISTS ( "
				+ "        SELECT 1 "
				+ "        FROM route r2 "
				+ "        WHERE r.route_number = r2.route_number "
				+ "        GROUP BY r2.route_number "
				+ "        HAVING COUNT(*) = 2 "
				+ "    ) ";
		if (departureAirportID != null && departureAirportID != 0)
			sql += "    AND f1.departure_airport_id =" + departureAirportID + " ";
		if (arrivalAirportID != null && arrivalAirportID != 0)
			sql += "    AND f2.arrival_airport_id =" + arrivalAirportID + " ";
		if (departureDate != null)
			sql	+= "    AND f1.departure_date ='" + departureDate + "'" + " ";
		if (arrivalDate != null && !arrivalDate.equals(Date.valueOf("1970-01-01")))
			sql += "    AND f2.arrival_date ='" + arrivalDate + "'" + " ";
		
		sql += "    AND f1.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " " 
				+ "    AND f2.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " "
				+ ") AS combined_results "
				+ "GROUP BY route_number, is_booking_allowed "
				+ "ORDER BY route_number, is_booking_allowed; ";
		
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Route route = new Route();
				route.setRouteNumber((Long) row.get("route_number"));
				
				Integer isBookingAllowedString = (Integer) row.get("is_booking_allowed");
				boolean isBookingAllowed = (isBookingAllowedString.equals(1))?true : false;
				route.setBookingAllowed(isBookingAllowed);
				
				String flightIdsStr = (String) row.get("flight_ids");
				List<Long> flightIds = Arrays.stream(flightIdsStr.split(",")).map(Long::parseLong)
						.collect(Collectors.toList());
				route.setFlightIDs(flightIds);
	
				// flight object will be set in service
				// route_price will be set in service
			
				result.add(route);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: RouteRepository\t Method: getRoutesByFilters "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve routes according to filters.", e);
		}
	}
	
	/**
	 * The method updates is_booking_allowed to the given value.
	 * @param routeNumber
	 * @param allowBooking
	 * @throws IllegalArgumentException
	 */
	public void updateAllowBooking(Long routeNumber, boolean allowBooking) throws IllegalArgumentException
	{
		try {
			String sql = "UPDATE route " +
				 		" SET is_booking_allowed = ? " +
				 		" WHERE route_number = ?; " ;
			jdbcTemplate.update(sql, new Object[] { allowBooking, routeNumber});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: RouteRepository\t Method: updateAllowBooking "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update is_booking_allowed field.", e);
		}
	}
	
}
