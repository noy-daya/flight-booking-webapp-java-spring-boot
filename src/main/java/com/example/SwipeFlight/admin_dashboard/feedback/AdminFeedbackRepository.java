package com.example.SwipeFlight.admin_dashboard.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer behind admin's ability to view average scores of user feedbacks for route.
 * (interacting with the database)
 */
@Repository
public class AdminFeedbackRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of average ratings (each element represents aspect), for the given routeNumber.
	 * @param routeNumber- the route which was selected by admin
	 * @return list of average ratings (If there are no rows -> each grade will be 0)
	 * @throws IllegalArgumentException
	 */
    public List<Double> getAverageRatingsForRouteNum(Long routeNumber) throws IllegalArgumentException {
    	List<Double> averageRatings = new ArrayList<>();
		try {
	        String sql = "SELECT COALESCE(AVG(cleaning_rating), 0) AS cleaning_avg, " +
	                			"COALESCE(AVG(convenience_rating), 0) AS convenience_avg, " +
	                			"COALESCE(AVG(service_rating), 0) AS service_avg " +
	                			"FROM feedback " +
	                			"WHERE route_number = ?";
	   
		   return jdbcTemplate.query(sql, (rs) -> {
		       if (rs.next()) {
		           double cleaningAvg = rs.getDouble("cleaning_avg");
		           double convenienceAvg = rs.getDouble("convenience_avg");
		           double serviceAvg = rs.getDouble("service_avg");
		           averageRatings.add(cleaningAvg);
		           averageRatings.add(convenienceAvg);
		           averageRatings.add(serviceAvg);
		       }
		       return averageRatings;
		   }, routeNumber);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AdminFeedbackRepository\t Method: getAverageRatingsForRouteNum "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve average ratings for routeNumber.", e);
		}
    }
		
}
