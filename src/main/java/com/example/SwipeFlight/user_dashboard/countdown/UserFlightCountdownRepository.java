package com.example.SwipeFlight.user_dashboard.countdown;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.server.utils.Constants;

/**
 * The class represents Repository layer behind user's ability to view a countDown until its next flight.
 * (interacting with the database)
 */
@Repository
public class UserFlightCountdownRepository {
	
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves the ID of the closest route, for the given userID.
	 * @param userID
	 * @return ID of the closest route
	 * @throws IllegalArgumentException
	 */
	public Long getClosestRouteNumberForUser(Long userID) throws IllegalArgumentException
	{
		 /* According to the following conditions:
			 * 1. The given userID has an active (=not canceled) booking for this route.
			 * 2. The departure date+time of the 1st flight in this route has not passed yet.
			 * 3. The 1st flight is not canceled
			 * 4. The departure date+time is the closest one.
		*/
		try {
			String sql = "SELECT b.route_number "
						+ "FROM booking b "
						+ "JOIN route r ON b.route_number = r.route_number "
						+ "JOIN flight f ON r.flight_id = f.id "
						+ "WHERE b.user_id = ? "
							+ "AND b.is_canceled = 0 "		// condition (1)
							+ "AND r.sequence_number = 1 "	// focus on 1st flight on this route
							+ "AND CONCAT(f.departure_date, ' ', f.departure_time) >= NOW() " // condition (2)
							+ "AND f.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " " // condition (3)
							+ "ORDER BY ABS(DATEDIFF(f.departure_date, CURDATE())) " // condition (4)
						+ "LIMIT 1; ";
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userID);
		    if (rows.isEmpty()) {
		        return null; // No rows found, return null
		    }
		    return (Long)rows.get(0).get("route_number");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserFlightCountdownRepository\t Method: UserFlightCountdownRepository "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve closest route for user.", e);
		}
	}

}
