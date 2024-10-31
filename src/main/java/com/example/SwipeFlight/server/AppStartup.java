package com.example.SwipeFlight.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The class is responsible for executing steps on application startup (only)
 * 1. Calls a method which initializes database- using DatabaseInitializer object.
 * 2. Calls a method which schedules a task to remove reserved seats from database- using ReservationCleanupService object.
 */
@Component
public class AppStartup implements CommandLineRunner {

	@Autowired // dependency injection
	private DatabaseInitializer databaseInitializer; 
	
	@Autowired // dependency injection
	private ReservationCleanupService reservationCleanupService;

	/**
	 *  The method is invoked automatically when application starts.
	 * (implements the method of CommandLineRunner interface)
	 * 1. Calls a method which initializes database (if necessary) and updates "initialized" as a result.
	 * 2. Calls a method which schedules a task to remove reserved seats from database- using ReservationCleanupService object.
	 */
	@Override
	public void run(String... args) {
		databaseInitializer.initializeDatabase();
		reservationCleanupService.scheduleCleanupTask();
	}
}
