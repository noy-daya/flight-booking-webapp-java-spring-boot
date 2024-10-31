package com.example.SwipeFlight.entities.airport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Airport.
 * (interacting with the database to perform operations on Airport table)
 */
@Repository
public class AirportRepository
{
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate;

	/**
	 * The method retrieves a list of all airports
	 * @return list of airport objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Airport> getAllDepartureAirports() throws IllegalArgumentException {
		List<Airport> result = new ArrayList<Airport>();
		try {
			String sql = "SELECT * FROM airport ORDER BY code; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Airport airport = createAirportObjectFromRow(row);
				result.add(airport);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AirportRepository\t Method: getAllDepartureAirports "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve departure airports.", e);
		}
	}

    /**
     * The method retrieves a list of all arrival airports for a given departureAirportID.
     * if no rows are found -> returns null
     * (= all airports, excluding the given departureAirportID)
     * @param departureAirportID
     * @return list of airport objects, or an empty list if no rows exist.
     * @throws IllegalArgumentException
     */
	public List<Airport> getArrivalAirportsByDepartureAirportID(Long departureAirportID)
								throws IllegalArgumentException
	{
		List<Airport> result = new ArrayList<Airport>();
		try {
			String sql = "SELECT * FROM airport WHERE id <> ? ORDER BY code; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, departureAirportID);
			for (Map<String, Object> row : rows) {
				Airport airport = createAirportObjectFromRow(row);
				result.add(airport);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AirportRepository\t Method: getArrivalAirportsByDepartureAirportID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve arrival airports.", e);
		}
	}
	
	/**
	 * The method retrieves an airport by its id.
	 * @param id
	 * @return airport object or null if no rows found.
	 * @throws IllegalArgumentException
	 */
	public Airport getAirportByID(Long id) throws IllegalArgumentException {
		List<Airport> result = new ArrayList<Airport>();
		try {
			String sql = "SELECT * FROM airport WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
			if (rows.isEmpty()) {
				return null; // No rows found, return null
			}
			for (Map<String, Object> row : rows) {
				Airport airport = createAirportObjectFromRow(row);
				result.add(airport);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AirportRepository\t Method: getAirportByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve airport by id.", e);
		}
	}

	/**
	 * The method retrieves cityID for a given airportID.
	 * @param id
	 * @return cityID or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getCityIdOfAirportByID(Long id) throws IllegalArgumentException {
		try {
			String sql = "SELECT city_id FROM airport WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
			if (rows.isEmpty()) {
				return null;
			}
			return (Long) rows.get(0).get("city_id");
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: AirportRepository\t Method: getCityIdOfAirportByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve city id for airport.", e);
		}
	}

	/**
	 * The method creates an Airport object from a database entry.
	 * @param row- entry from the database
	 * @return user
	 */
	private Airport createAirportObjectFromRow(Map<String, Object> row) {
		Airport airport = new Airport();
		airport.setId((Long) row.get("id"));
		airport.setCode((String) row.get("code"));
		airport.setName((String) row.get("name"));
		// city will be set from service layer
		return airport;
	}
}
