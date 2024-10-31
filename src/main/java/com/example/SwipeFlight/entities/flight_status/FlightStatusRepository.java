package com.example.SwipeFlight.entities.flight_status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of FlightStatus.
 * (interacting with the database to perform operations on Flight_Status table)
 */
@Repository
public class FlightStatusRepository  {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of flight status objects.
	 * @return list of flight status objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<FlightStatus> getStatuses() throws IllegalArgumentException {
		List<FlightStatus> result = new ArrayList<FlightStatus>();
		try {
			String sql = "SELECT * FROM flight_status; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				FlightStatus status = new FlightStatus();
				status.setId((Long) row.get("id"));
				status.setDescription((String) row.get("description"));
				result.add(status);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: FlightStatusRepository\t Method: getStatuses "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve flights status.", e);
		}
	}
}
