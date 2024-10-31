package com.example.SwipeFlight.user_dashboard.countdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;
import com.example.SwipeFlight.server.utils.custom_exceptions.RequestAttributeNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The class displays views related to user's ability to view a countDown until its next flight.
 */
@Controller
public class UserFlightCountdownController {
	
	// dependency injections
	@Autowired
	private UserFlightCountdownService userFlightCountdownService;
	@Autowired
	private RouteService routeService;
	
    /**
     * The method is responsible for displaying "userFlightCountdown" page
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     * @throws RequestAttributeNotFoundException
     */
    @GetMapping("/userFlightCountdown")
    public String viewUserFlightCountdownPage(Model model, HttpServletRequest request)
    {
    	// get sessionUser from request
		User sessionUser = (User)request.getAttribute("sessionUser");
	    if (sessionUser == null) {
	    	System.err.println("\n*** Error ***\nClass: UserFlightCountdownController\t Method: viewUserFlightCountdownPage "
					+ "\nDetails: Request attribute 'sessionUser' is not found.");
	        throw new RequestAttributeNotFoundException("request attribute 'sessionUser' is not found."); // handler in GlobalExceptionHandler.java
	    }
        
	    // display the closest route
		Long closestRouteNumber = userFlightCountdownService.getClosestRouteNumberForUser(sessionUser.getId());
		if (closestRouteNumber != null)
		{
			Route closestRoute = routeService.getRouteByRouteNumber(closestRouteNumber);
		    model.addAttribute("route", closestRoute);
		}
	    
        return "userFlightCountdown";
    }

}
