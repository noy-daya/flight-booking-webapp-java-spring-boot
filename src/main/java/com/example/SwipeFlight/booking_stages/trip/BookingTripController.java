package com.example.SwipeFlight.booking_stages.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.entities.airport.AirportService;
import com.example.SwipeFlight.server.utils.BinderClass;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class displays views related to Trip Stage in booking process.
 */
@Controller
public class BookingTripController {
	
	// dependency injections
    @Autowired
    private BinderClass binderClass;
	@Autowired
	private BookingTripService tripService;
	@Autowired
	private AirportService airportService;
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binderClass.initBinder(binder);
    }
    
    /**
     * The method is responsible for displaying "bookingTrip" page.
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/bookingTrip")
	public String viewBookingTrip(ModelMap model)
	{
		// display empty Trip form
		model.addAttribute("tripForm", new TripForm());
        
		// display airports
	    model.addAttribute("departureAirportsList", airportService.getAllDepartureAirports());
	    
		return "bookingTrip";
	}
	
	/**
	 * The method is responsible for processing new flight insertion.
	 * 		- if there are rejected values -> return to the form with the errors.
	 * 		- otherwise- fields are legal -> insert flight, and display a success message
	 * @param tripForm - the form entered by user
	 * @param model - to pass attributes to the view.
     * @param redirectAttributes- attributes for redirect scenarios
	 * @param result - BindingResult to attach errors to (if there are any)
	 * @param request
	 * @return the URL of the page which should be rendered. (accomplish Post-Redirect-Get pattern)
	 */
	@PostMapping("/bookingTrip/ProcessBookingTrip")
	public String processBookingTrip(@ModelAttribute("tripForm") TripForm tripForm,
									ModelMap model, RedirectAttributes redirectAttributes,
									BindingResult result, HttpServletRequest request)
									throws SessionIsInvalidException
	{
	    // Notation:
	    // The following request parameters are received as strings to this layer, and might be empty.
	    // initBinder() function receives each and changes their value to null, in case the string is null,
	    // before reaching the controller.
	    
		// -------------------------------------------
		// get session and session's attributes
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: BookingTripController\t Method: processBookingTrip "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
		// validate form
	    result = tripService.validateTripForm(tripForm, result);
	    
    	// for Post-Redirect-Get pattern
        // 		add errors and the object the errors relate to,
    	// 		to redirect attributes so they will be passed even during redirect.
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("tripForm", tripForm);
        
	    // failure:
	    if (result.hasErrors())
	    {
            return "redirect:/bookingTripRedirect"; // Post-Redirect-Get pattern
	    }
	    
	    // success:
		// -------------------------------------------
		// store in session
		// -------------------------------------------
    	session.setAttribute("booking_numOfPassengers", tripForm.getNumOfPassengers());
    	
    	return "redirect:/bookingTripRedirect"; // Post-Redirect-Get pattern
    }
	
    /**
     * The method is added due to Post-Redirect-Get pattern (to avoid form re-submission)
     * @param model- to pass attributes to the view.
     * @param redirectAttributes - to retrieve BindingResult the object is refers to, saved during POST
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/bookingTripRedirect")
	public String bookingTripRedirect(Model model, RedirectAttributes redirectAttributes)
	{
		TripForm tripForm = null;
		
		// if previous POST request saved the necessary elements 
        if (model.asMap().containsKey("result") && model.asMap().containsKey("tripForm"))
        {
        	// result
        	BindingResult result = (BindingResult)model.asMap().get("result");
            model.addAttribute("org.springframework.validation.BindingResult.tripForm",
                    model.asMap().get("result"));
            
            // tripForm
            tripForm = (TripForm)model.asMap().get("tripForm");
            model.addAttribute("tripForm", tripForm);
            
            if (!result.hasErrors())
            	// redirect to next step
	        	return "redirect:/bookingFlight?departureAirportID=" + tripForm.getDepartureAirportID() // redirect to the next step
	    						+ "&arrivalAirportID=" + tripForm.getArrivalAirportID()
	    						+ "&departureDate=" + tripForm.getDepartureDate()
	    						+ "&numOfPassengers=" + tripForm.getNumOfPassengers();
        }
        // continue actions of GET request
        // --------------------------------------------
        if (tripForm == null)
        	model.addAttribute("tripForm", new TripForm());
        else
        	model.addAttribute("tripForm", tripForm);
		// display airports
	    model.addAttribute("departureAirportsList", airportService.getAllDepartureAirports());
	    
        return "bookingTrip"; // Post-Redirect-Get pattern
        
	}

}
