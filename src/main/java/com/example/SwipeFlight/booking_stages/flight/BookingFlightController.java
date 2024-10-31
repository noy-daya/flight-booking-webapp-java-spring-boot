package com.example.SwipeFlight.booking_stages.flight;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class displays views related to Flight Stage in booking process.
 */
@Controller
public class BookingFlightController {
	
	@Autowired // dependency injection
	private BookingFlightService bookingFlightService;
    
    /**
     * The method is responsible for displaying "bookingFlight" page, given previous stage parameters
     * 	which define the routes that will be displayed.
     * @param departureAirportID - from previous stage
     * @param arrivalAirportID - from previous stage
     * @param departureDate - from previous stage
     * @param numOfPassengers - from previous stage
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/bookingFlight")
	public String viewBookingFlightPage(@RequestParam("departureAirportID") Long departureAirportID, // from previous step
	                                   @RequestParam("arrivalAirportID") Long arrivalAirportID,
	                                   @RequestParam("departureDate") Date departureDate,
	                                   @RequestParam("numOfPassengers") int numOfPassengers,
	                                   ModelMap model) {
		
		// get routes that meet the requirements of the given parameters.
		model.addAttribute("routesList", bookingFlightService.getRoutesForFlightRequest(departureAirportID, arrivalAirportID,
																			numOfPassengers, departureDate));
		return "bookingFlight";
	}
	
    /**
     * The method is responsible for processing the form in "bookingFlight" page.
     * @param routeNumber - the selected route
     * @param model - to pass attributes to the view.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException
     */
	@PostMapping("/bookingFlight/ProcessBookingFlight{routeNumber}")
	public String processBookingFlight(@RequestParam("routeNumber") Long routeNumber,
	                            		ModelMap model, HttpServletRequest request)
	                            			throws SessionIsInvalidException
	{
		
		// -------------------------------------------
		// get session and session's attributes
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: BookingFlightController\t Method: processBookingFlight "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
		
		// -------------------------------------------
		// store in session, continue to next step
		// -------------------------------------------
		session.setAttribute("booking_routeNumber", routeNumber);
		
		return "redirect:/bookingPassengers"; // pass to next step
    }

}
