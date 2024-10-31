package com.example.SwipeFlight.entities.luggage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Luggage.
 * (interacting with the database to perform operations on Luggage table)
 */
@Repository
public class LuggageRepository {
	
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of luggage objects existing in database.
	 * @return list of luggage objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Luggage> getLuggageTypes() throws IllegalArgumentException {
		List<Luggage> result = new ArrayList<Luggage>();
		try {
			String sql = "SELECT * FROM luggage; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Luggage luggage = createLuggageObjectFromRow(row);
				result.add(luggage);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: LuggageRepository\t Method: getLuggageTypes "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve luggage types.", e);
		}
	}
	
	/**
	 * The method retrieves price for a given luggageID.
	 * @param luggageID
	 * @return price for luggage, or 0 if no rows found.
	 * @throws IllegalArgumentException
	 */
	public double getLuggagePrice(Long luggageID) throws IllegalArgumentException {
		try {
			String sql = "SELECT price FROM luggage WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, luggageID);
			if (rows.isEmpty()) {
				return 0;
			}
			return (double) rows.get(0).get("price");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: LuggageRepository\t Method: getLuggagePrice "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve luggage price.", e);
		}
	}
	
	/**
	 * The method creates a Luggage object from a database entry.
	 * @param row- entry from the database
	 * @return luggage
	 */
	private Luggage createLuggageObjectFromRow(Map<String, Object> row) {
		Luggage luggage = new Luggage();
		luggage.setId((Long) row.get("id"));
		luggage.setDescription((String) row.get("description"));
		luggage.setWeightLimit((int) row.get("weight_limit"));
		luggage.setPrice((double) row.get("price"));
		return luggage;
	}

}
