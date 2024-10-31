package com.example.SwipeFlight.server;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * The class represents a service which schedules and executes a cleanup task to
 * remove expired reservations from "reserved_seats" database table.
 */
@Service
public class ReservationCleanupService {

	@Autowired // dependency injection
	private JdbcTemplate jdbcTemplate; // allows database access and JDBC operations

	/**
	 * The method is called from AppStartup.
	 * Checks if the application has been initialized successfully. as a result
	 * it schedules a periodic cleanup task (implemented in "cleanupExpiredReservations" method) to run every 30 seconds.
	 * (it becomes enabled first after the given initial delay, and subsequently with the given period)
	 */
	public void scheduleCleanupTask() {
		try {
			// define the object which executes the task
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			// schedule the task: no initial delay (0), subsequently executed every 30
			// seconds.
			executorService.scheduleAtFixedRate(this::cleanupExpiredReservations, 0, 30, TimeUnit.SECONDS);
			System.out.println("==> Scheduled task for expired reservations executed successfully.");
		} catch (RejectedExecutionException e) {
			System.err.println("*** Error -- Execute -- task cannot be scheduled for execution.");
		} catch (NullPointerException e) {
			System.err.println("*** Error -- Execute -- " + e.getMessage());
		} catch (Exception e) {
			System.err.println("*** Error -- Execute -- execute error: " + e.getMessage());
		}
	}

	/**
	 * The method deletes expired reservations from "reservation_time" table in
	 * database. Expired reservation = an entry where the "reservation_time" field is older than 5 minutes.
	 */
	public void cleanupExpiredReservations() {
		try {
			String sql = "DELETE FROM reserved_seats WHERE reservation_time < ?";
			// the time stamp of 5 minutes ago
			LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
			jdbcTemplate.update(sql, fiveMinutesAgo);
		} catch (Exception e) {
			System.err.println("*** Error -- SQL -- " + e.getMessage());
		}
	}
}
