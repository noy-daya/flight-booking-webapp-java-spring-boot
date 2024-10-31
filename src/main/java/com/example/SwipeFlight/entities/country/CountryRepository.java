package com.example.SwipeFlight.entities.country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Country.
 * (interacting with the database to perform operations on Country table)
 */
@Repository
public class CountryRepository {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a country by its id.
	 * @param countryID
	 * @return country object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Country getCountryByID(Long countryID) throws IllegalArgumentException {
		List<Country> result = new ArrayList<Country>();
		try {
			String sql = "SELECT * FROM country WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, countryID);
			
			if (rows.isEmpty()) {
				return null; // No rows found, return null
			}
			for (Map<String, Object> row : rows) {
				Country country = new Country();
				country.setId((Long) row.get("id"));
				country.setName((String) row.get("name"));
				result.add(country);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CountryRepository\t Method: getCountryByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve country.", e);
		}
	}
}
