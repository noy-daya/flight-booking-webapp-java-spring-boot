package com.example.SwipeFlight.entities.plane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of Plane.
 * (interacting with the database to perform operations on Plane table)
 */
@Repository
public class PlaneRepository
{
	@Autowired // dependency injections
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * The method retrieves a list of available planes in the database.
	 * @return list of planes, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Plane> getAvailablePlanes() throws IllegalArgumentException {
		 /* definition: Available Plane = its available_quantity is greater than 0. */
		List<Plane> result = new ArrayList<Plane>();
		try {
			String sql = "SELECT * FROM plane " +
						 "WHERE available_quantity > 0; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Plane plane = createPlaneObjectFromRow(row);
				result.add(plane);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PlaneRepository\t Method: getAvailablePlanes "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve avilable planes.", e);
		}
	}
	
	/**
	 * The method retrieves a list all planes in the database.
	 * @return list of planes, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Plane> getAllPlanes() throws IllegalArgumentException {
		List<Plane> result = new ArrayList<Plane>();
		try {
			String sql = "SELECT * FROM plane; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Plane plane = createPlaneObjectFromRow(row);
				result.add(plane);
			}
			return result;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PlaneRepository\t Method: getAllPlanes "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve all planes.", e);
		}
	}
	
	/**
	 * The method retrieves a plane by its id.
	 * @param planeID
	 * @return plane object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Plane getPlaneByID(Long planeID) throws IllegalArgumentException {
		try {
			String sql = "SELECT * FROM plane WHERE id = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, planeID);
			List<Plane> result = new ArrayList<Plane>();
			if (rows.isEmpty()) {
				return null; // No rows found, return null
			}
			for (Map<String, Object> row : rows) {
				Plane plane = createPlaneObjectFromRow(row);
				result.add(plane);
			}
			return result.get(0);
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PlaneRepository\t Method: getPlaneByID "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve plane by id.", e);
		}
	}
	
	/**
	 * The method updates availability of the given plane.
	 * adds (+1) or (-1) to the value of available_quantity, according to the state of isDecrement.
	 * @param planeID - the ID of the plane which is modified
	 * @param isDecrement - True -> (-1), False -> (+1)
	 * @throws IllegalArgumentException
	 */
	public void updateAvailableQuantity(Long planeID, boolean isDecrement) throws IllegalArgumentException
	{
		try {
			String sql;
			// if decrement == true -> a plane was set for a flight -> decrease its available quantity
			if (isDecrement)
			{
				sql = 	"UPDATE plane " +
						"SET available_quantity = available_quantity - 1 " +
						"WHERE id = ?; " ;
			}
			// if decrement == false -> a plane was set for a flight but it got cancelled -> increase its available quantity
			else
			{
				sql = 	"UPDATE plane " +
						"SET available_quantity = available_quantity + 1 " +
						"WHERE id = ?; " ;
			}
			jdbcTemplate.update(sql, new Object[]{ planeID });
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: PlaneRepository\t Method: updateAvailableQuantity "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update plane available quantity.", e);
		}
	}
	
	/**
	 * The method creates a Plane object from a database entry.
	 * @param row - entry from the database
	 * @return plane
	 */
	private Plane createPlaneObjectFromRow(Map<String, Object> row) {
		Plane plane = new Plane();
		plane.setId((Long)row.get("id"));
		plane.setDescription((String)row.get("description"));
		plane.setNumOfRows((int)row.get("num_of_rows"));
		plane.setNumOfSeatsPerRow((int)row.get("num_of_seats_per_row"));
		plane.setTotalQuantity((int)row.get("total_quantity"));
		plane.setAvailableQuantity((int)row.get("available_quantity"));
		return plane;
	}
}
