package com.example.SwipeFlight.user_dashboard.feedback;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.entities.feedback.Feedback;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.server.utils.custom_exceptions.RequestAttributeNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The class displays views related to user's ability to fill a feedback form about his previous flights.
 */
@Controller
public class UserFeedbackController {

	// dependency injection
	@Autowired
	private UserFeedbackService userFeedbackService;
	
    /**
     * The method is responsible for displaying "userFeedbackAvailability" page
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     * @throws RequestAttributeNotFoundException
     */
    @GetMapping("/userFeedbackAvailability")
    public String viewUserFeedbackAvailabilityPage(Model model, HttpServletRequest request)
    											  throws RequestAttributeNotFoundException{
        
    	// get sessionUser from request
		User sessionUser = (User) request.getAttribute("sessionUser");
	    if (sessionUser == null) {
	    	System.err.println("\n*** Error ***\nClass: UserFeedbackController\t Method: viewUserFeedbackAvailabilityPage "
					+ "\nDetails: Request attribute 'sessionUser' is not found.");
	        throw new RequestAttributeNotFoundException("Request attribute 'sessionUser' is not found."); // handler in GlobalExceptionHandler.java
	    }
		
	    // get a map of user's routes and an indication of route's availability for feedback
		Map<Route, Boolean> routeFeedbackMap = userFeedbackService.mapUserRoutesAndFeedbackAvailability(sessionUser.getId());
		
		// display the map
	    model.addAttribute("routeFeedbackMap", routeFeedbackMap);
	    
        return "userFeedbackAvailability";
    }
    
    /**
     * The method is responsible for displaying "userFeedbackAdd" page, for a given routeNumber.
     * @param routeNumber
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
    @GetMapping("/userFeedbackAdd{routeNumber}")
    public String viewUserFeedbackAddPage(@RequestParam("routeNumber") Long routeNumber, Model model)
    {
        model.addAttribute("routeNumber", routeNumber);
        return "userFeedbackAdd";
    }

    /**
     * The method is responsible for processing the form in "userFeedbackAdd" page.
     * @param feedbackForm- the values entered by the user
     * @param routeNumber- of the route
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     */
    @PostMapping("/userFeedbackAdd/ProcessFeedbackForm{routeNumber}")
    public String processFeedbackForm(Feedback feedbackForm, @RequestParam("routeNumber") Long routeNumber,
    									ModelMap model, HttpServletRequest request)
    {
    	// get sessionUser from request
    	User sessionUser = (User) request.getAttribute("sessionUser");
	    if (sessionUser == null) {
	    	System.err.println("\n*** Error ***\nClass: UserFeedbackController\t Method: processFeedbackForm "
					+ "\nDetails: Request attribute 'sessionUser' is not found.");
	        throw new RequestAttributeNotFoundException("Request attribute 'sessionUser' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    if (feedbackForm.getCleaningRating() != 0 && feedbackForm.getConvenienceRating() != 0
	    	&& feedbackForm.getServiceRating() != 0)
	    {
	    	// insert the feedback and display a message
	    	userFeedbackService.insertFeedback(feedbackForm, sessionUser.getId(), routeNumber);
	    	model.addAttribute("success_message", "Thank you, your feedback was sent!");
	    }
	    else
	    {
	    	model.addAttribute("error_message", "Please rate all the categories");
	    }
	    model.addAttribute("routeNumber", routeNumber);
        return "userFeedbackAdd";
    }
    
}
