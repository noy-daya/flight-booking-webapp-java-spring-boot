package com.example.SwipeFlight.admin_dashboard.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer behind admin's ability to view average scores of user feedbacks for route.
 */
@Service
public class AdminFeedbackService {

	@Autowired // dependency injection
	private AdminFeedbackRepository adminFeedbackRepository;
	
	/**
	 * The method retrieves a list of average ratings (each element represents aspect), for the given routeNumber.
	 * - If there are no rows -> each grade will be 0
	 * @param routeNumber- the route which was selected by admin
	 */
    public List<Double> getAverageRatingsForRouteNum(Long routeNumber) {
        return adminFeedbackRepository.getAverageRatingsForRouteNum(routeNumber);
    }
    
    
}
