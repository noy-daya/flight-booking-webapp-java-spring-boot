package com.example.SwipeFlight.user_dashboard.feedback;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.feedback.Feedback;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class represents Service layer behind user's ability to fill a feedback form about his previous flights.
 */
@Service
public class UserFeedbackService {
	
	// dependency injections
	@Autowired
	private UserFeedbackRepository userFeedbackRepository;
	@Autowired
	private RouteService routeService;
	
	/**
	 * The method inserts an entry into Feedback table in the database.
	 * @param ratingForm - feedback's ratings
	 * @param userID - the user which commits the insertion
	 * @param routeNumber - of the route which the feedback relates to
	 * @throws IllegalArgumentException
	 */
	public void insertFeedback(Feedback ratingForm, Long userID, Long routeNumber) throws IllegalArgumentException
	{
		userFeedbackRepository.insertFeedback(ratingForm, userID, routeNumber);
	}
    
	/**
	 * The method retrieves a list consists all the routeIDs that the given user can fill a feedback for.
	 * @param userID
	 * @return an ordered list which consists all the routeIDs, or an empty list
	 * @throws IllegalArgumentException
	 */
	public List<Long> getAllRouteIDsAvilableForFeedbackFromUser(Long userID) throws IllegalArgumentException
	{
		return userFeedbackRepository.getAllRouteNumbersAvilableForFeedbackFromUser(userID);
	}
	
	/**
	 * The method retrieves a list consists all the routeIDs that the given user has a booking for,
	 * and are not cancelled.
	 * @param userID- current user
	 * @return an ordered list which consists all the routeIDs, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	 public List<Long> getAllRouteNumbersForUser(Long userID) throws IllegalArgumentException
	{
		 return userFeedbackRepository.getAllRouteNumbersForUser(userID);
	}
	 
	 /**
	  * The method returns a map<Route, Boolean> of all user's route, and an indication of route's availability for feedback.
	  * @param userID- current user
	  * @return routeFeedbackMap
	  */
	public Map<Route, Boolean> mapUserRoutesAndFeedbackAvailability(Long userID)
	{
		// get all the routes (ordered) which user has a booking for
		List<Long> userRoutes_all = getAllRouteNumbersForUser(userID);

		// get only the routes (ordered) which are available for feedback
		List<Long> userRoutes_withFeedbackAvailable = getAllRouteIDsAvilableForFeedbackFromUser(userID);

		// for each route in userRoutes_all, add an indication of its availability for feedback.
		Map<Route, Boolean> routeFeedbackMap = new LinkedHashMap<>(); // key: route, value: a flag saying if feedback is
																	  // 					available for feedback

		for (Long routeID : userRoutes_all) // since we are using LinkedHashMap, the order is the same order of their
											// original order.
		{
			boolean openForFeedback = userRoutes_withFeedbackAvailable.contains(routeID);
			Route route = routeService.getRouteByRouteNumber(routeID);
			routeFeedbackMap.put(route, openForFeedback);
		}
		return routeFeedbackMap;
	}
	
	
}
