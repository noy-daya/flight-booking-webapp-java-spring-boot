package com.example.SwipeFlight.entities.flight_status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer of FlightStatus.
 * (interacts with Repository layer)
 */
@Service
public class FlightStatusService {

	@Autowired // dependency injection
	private FlightStatusRepository flightStatusRepository;
	
	/**
	 * The method retrieves a list of flight status objects.
	 * @return list of flight status objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<FlightStatus> getStatuses() throws IllegalArgumentException {
		return flightStatusRepository.getStatuses();
	}
}
