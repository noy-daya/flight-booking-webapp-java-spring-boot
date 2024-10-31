package com.example.SwipeFlight.entities.city;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of City.
 * (interacting with the database to perform operations on City table)
 */
@Repository
public class CityRepository
{
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method updates a City entry in the database.
	 * @param city - the city to update
	 * @param filePath
	 * @throws IllegalArgumentException
	 */
	public void updateCity(City city, String filePath) throws IllegalArgumentException {
		try {
			String sql;
			// a file was selected -> update img_url
			if (!filePath.isEmpty())
			{
				sql = "UPDATE city " +
					  "SET description = ?, img_url = ? " +
					  "WHERE id = ?; ";
				jdbcTemplate.update(sql, new Object[] { city.getDescription(), filePath, city.getId() });
			}
			// a file was not selected -> do not change the img_url
			else
			{
				sql = "UPDATE city " +
					  "SET description = ? " +
					  "WHERE id = ?; ";
				jdbcTemplate.update(sql, new Object[] { city.getDescription(), city.getId() });
			}
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CityRepository\t Method: updateCity "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to upate city.", e);
		}
	}

	/**
	 * The method retrieves a city by its id.
	 * @return city object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public City getCityByID(Long cityID) throws IllegalArgumentException {
		try {
			String sql = "SELECT * FROM city WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, cityID);
			List<City> result = new ArrayList<City>();
			if (rows.isEmpty()) {
				return null; // No rows found, return null
			}
			for (Map<String, Object> row : rows) {
				City city = createCityObjectFromRow(row);
				result.add(city);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CityRepository\t Method: getCityByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve city by id.", e);
		}
	}
	
	/**
	 * The method returns a list of all city names which begin by the given keyboard string.
	 * @return list of city names, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<String> getCityNamesByKeyboard(String keyword) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();
		try {
			String sql = "SELECT * FROM city " +
						 "WHERE name LIKE ? " +
						 "ORDER BY name ASC; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, keyword + "%");
			for (Map<String, Object> row : rows) {
				result.add((String) row.get("name"));
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CityRepository\t Method: getCityNamesByKeyboard "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve city names by keyboard.", e);
		}
	}
	
	/**
	 * The method returns a list of all city objects which begin by the given cityName.
	 * @return list of cities, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<City> getCitiesByKeyboard(String keyword) throws IllegalArgumentException {
		List<City> result = new ArrayList<City>();
		try {
			String sql = "SELECT * FROM city WHERE name LIKE ?";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, keyword + "%");
			for (Map<String, Object> row : rows) {
				City city = createCityObjectFromRow(row);
				result.add(city);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CityRepository\t Method: getCitiesByKeyboard "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve city objects by keyboard.", e);
		}
	}
	
	/**
	 * The method retrieves countryID for a given cityID.
	 * @return city object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getCountryIdOfCityByID(Long id) throws IllegalArgumentException {
		try {
			String sql = "SELECT country_id FROM city WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("country_id");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: CityRepository\t Method: getCountryIdOfCityByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve country id for city id.", e);
		}
	}
	
	/**
	 * The method creates a City object from a database entry.
	 * @param row- entry from the database
	 * @return city object, or null if no rows exist.
	 */
	private City createCityObjectFromRow(Map<String, Object> row) {
		City city = new City();
		city.setId((Long)row.get("id"));
		city.setName((String)row.get("name"));
		city.setDescription((String)row.get("description"));
		city.setImgUrl((String)row.get("img_url"));
		
		// country will be set from service layer
		
		return city;
	}
}
