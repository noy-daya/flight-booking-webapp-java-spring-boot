package com.example.SwipeFlight.user_dashboard.feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.SwipeFlight.entities.feedback.Feedback;
import com.example.SwipeFlight.server.utils.Constants;

/**
 * The class represents Repository layer behind user's ability to fill a feedback form about his previous flights.
 * (interacting with the database)
 */
@Repository
public class UserFeedbackRepository {
	
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method inserts an entry into Feedback table in the database.
	 * @param ratingForm - feedback's ratings
	 * @param userID - the user which commits the insertion
	 * @param routeNumber - of the route which the feedback relates to
	 * @throws IllegalArgumentException
	 */
	public void insertFeedback(Feedback ratingForm, Long userID, Long routeNumber) throws IllegalArgumentException
	{
		try {
			String sql = "INSERT INTO feedback " +
			            	"(user_id, route_number, " +
			            	 "cleaning_rating, convenience_rating, service_rating) " +
			            "VALUES (?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, new Object[]{
					userID, routeNumber,
					ratingForm.getCleaningRating(), ratingForm.getConvenienceRating(), ratingForm.getServiceRating() });
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserFeedbackRepository\t Method: insertFeedback "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert feedback from user.", e);
		}
	}
   
	/**
	 * The method retrieves a list consists all the routeIDs that the given user can fill a feedback for.
	 * @param userID
	 * @return an ordered list which consists all the routeIDs, or an empty list
	 * @throws IllegalArgumentException
	 */
	public List<Long> getAllRouteNumbersAvilableForFeedbackFromUser(Long userID) throws IllegalArgumentException
	{
		 /* According to the following conditions:
			 * 1. The given userID has an active (=not canceled) booking for this route.
			 * 2. The user hasn't sent a feedback for this route yet (=there is no entry in Feedback with the route_number and user_id)
			 * 3. The route had already passed, meaning:
			 * 		- If there are two flights under the route in Route table ->
			 * 			Ensure that the flight_id with sequence_number = 2 has an arrival_date+time that had already passed.
			 * 		- If there is exactly one flight under the route in the Route table ->
			 *			Ensure that the flight_id with sequence_number = 1 has an arrival_date+time that had already passed.
		*/	 
		List<Long> result = new ArrayList<>();
		try {
			String sql =	"SELECT b.route_number " +
		               		"FROM booking b " +
		               		"JOIN route r ON b.route_number = r.route_number " +
		               		"JOIN flight f1 ON r.flight_id = f1.id AND r.sequence_number = 1 " +
		               		"LEFT JOIN flight f2 ON r.flight_id = f2.id AND r.sequence_number = 2 " +
		               		"WHERE b.user_id = ? " + 
		               			   "AND f1.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " " +
		               			   "AND (f2.status_id IS NULL OR f2.status_id != " + Constants.FLIGHT_STATUS_CANCELED + ") " +
		               			   "AND b.is_canceled = 0 "	+		// condition (1)
					               "AND b.route_number NOT IN ( " + // condition (2)
					               "    SELECT f2.route_number " +
					               "    FROM feedback f2 " +
					               "    WHERE f2.user_id = ? " +
					               ") " +
					               "AND ( " + // condition (3)
					               "    (f2.arrival_date IS NOT NULL " +
					               "        AND CONCAT(f2.arrival_date, ' ', f2.arrival_time) < NOW()) " +
					               "    OR " +
					               "    (f2.arrival_date IS NULL " +
					               "        AND CONCAT(f1.arrival_date, ' ', f1.arrival_time) < NOW()) " +
					               ") " +
					        "ORDER BY b.route_number; ";
			
		    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userID, userID);
		    for (Map<String, Object> row : rows) {
		    	result.add((Long) row.get("route_number"));
		    }
		    return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserFeedbackRepository\t Method: getAllRouteNumbersAvilableForFeedbackFromUser "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve route numbers available for feedback.", e);
		}
	}
	
	/**
	 * The method retrieves a list consists all the routeIDs that the given user has a booking for,
	 * and are not cancelled.
	 * @param userID- current user
	 * @return an ordered list which consists all the routeIDs, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Long> getAllRouteNumbersForUser(Long userID) throws IllegalArgumentException
	{
		List<Long> result = new ArrayList<>();
		try {
			String sql = "SELECT distinct b.route_number " +
						 "FROM booking b " +
						 "JOIN route r ON b.route_number = r.route_number " +
						 "JOIN flight f ON r.flight_id = f.id " +
						 "WHERE b.user_id = ? " +
						 "	AND f.status_id != " + Constants.FLIGHT_STATUS_CANCELED + " " +
						 "	AND b.is_canceled = 0 " +
						 "ORDER BY b.route_number; ";
			
		    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userID);
		    for (Map<String, Object> row : rows) {
		    	result.add((Long) row.get("route_number"));
		    }
		    return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserFeedbackRepository\t Method: getAllRouteNumbersForUser "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve routes for user.", e);
		}
	}

}
