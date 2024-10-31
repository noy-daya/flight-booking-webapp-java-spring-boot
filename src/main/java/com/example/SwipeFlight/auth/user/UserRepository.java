package com.example.SwipeFlight.auth.user;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * The class represents Repository layer of User.
 * (interacting with the database to perform operations on User table)
 */
@Repository
public class UserRepository
{
	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate; // allows database access and JDBC operations
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // encoding password before insert
	
	/**
	 * The method inserts a new User into the database. (password will be inserted encoded)
	 * @param user
	 * @throws IllegalArgumentException
	 */
	public void insertUser(User user) throws IllegalArgumentException
	{
		try {
			// encode password
			String hashedPassword = passwordEncoder.encode(user.getPassword());
			// query
			String sql = "INSERT INTO user (user_name, email, password, is_admin, last_seen) " +
		             	 "VALUES (?, ?, ?, ?, NOW()); ";
			jdbcTemplate.update(sql, new Object[]{
					user.getUserName(), user.getEmail(), hashedPassword, user.isAdmin()
			});
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserRepository\t Method: insertUser "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert user.", e);
		}
	}

    /**
     * The method retrieves a user by its userName.
     * @param userName
     * @return user object, or null if no rows found.
     * @throws IllegalArgumentException
     */
	public User getUserByUserName(String userName) throws IllegalArgumentException
	{
		try {
			String sql = "SELECT * FROM user WHERE user_name = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userName);

			if (rows.isEmpty()) {
				return null;
			}
			return createUserObjectFromRow(rows.get(0));
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserRepository\t Method: getUserByUserName "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to insert user.", e);
		}
	}

    /**
     * The method retrieves a user by its email.
     * @param email
     * @return user object, or null if no rows found.
     * @throws IllegalArgumentException
     */
	public User getUserByEmail(String email) throws IllegalArgumentException {
		try {
			String sql = "SELECT * FROM user WHERE email = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, email);

			if (rows.isEmpty()) {
				return null;
			}
			return createUserObjectFromRow(rows.get(0));
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserRepository\t Method: getUserByEmail "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve user by email.", e);
		}
	}

	/**
	 * The method retrieves a user by its userName and password.
	 * @param userName
	 * @param password
	 * @return user object, or null if no rows found.
	 * @throws IllegalArgumentException
	 */
	public User getUserByUsernameAndPassword(String userName, String password) throws IllegalArgumentException
	{
		try {
			String sql = "SELECT * FROM user WHERE user_name = ?; ";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userName);
			if (rows.isEmpty()) {
				return null;
			}
			for (Map<String, Object> row : rows) {
				String storedPassword = (String) row.get("password");
				if (passwordEncoder.matches(password, storedPassword)) {
					return createUserObjectFromRow(row);
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserRepository\t Method: getUserByUsernameAndPassword "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to retrieve user by username and password.", e);
		}
	}
	
	/**
	 * The method updates user's last seen field.
	 * @param userName
	 * @throws IllegalArgumentException
	 */
	public void updateLastSeen(String userName) throws IllegalArgumentException
	{
		try {
			String sql = "UPDATE user " +
						 "SET last_seen = NOW() " + 
						 "WHERE user_name = ?; ";
			jdbcTemplate.update(sql, new Object[]{ userName });
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: UserRepository\t Method: updateLastSeen "
					+ "\nDetails: " + e.getMessage());
			throw new IllegalArgumentException("Failed to update user's last seen field.", e);
		}
	}

   /**
    * The method creates a User object from a database entry.
    * @param row- entry from the database
    * @return user
    */
    private User createUserObjectFromRow(Map<String, Object> row) {
        User user = new User();
        user.setId((Long) row.get("id"));
        user.setUserName((String) row.get("user_name"));
        user.setEmail((String) row.get("email"));
        user.setPassword((String) row.get("password"));
        user.setAdmin((boolean) row.get("is_admin"));
        user.setLastSeen((Timestamp) row.get("last_seen"));
        return user;
    }
}
