package com.example.SwipeFlight.user_dashboard.countdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer behind user's ability to view a countDown until its next flight.
 */
@Service
public class UserFlightCountdownService {

	@Autowired // dependency injection
	private UserFlightCountdownRepository userFlightCountdownRepository;
	
	/**
	 * The method retrieves the ID of the closest route, for the given userID.
	 * @param userID
	 * @return ID of the closest route
	 * @throws IllegalArgumentException
	 */
	 public Long getClosestRouteNumberForUser(Long userID) throws IllegalArgumentException
	{
		 return userFlightCountdownRepository.getClosestRouteNumberForUser(userID);
	}
}
