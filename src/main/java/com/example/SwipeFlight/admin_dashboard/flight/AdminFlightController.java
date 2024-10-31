package com.example.SwipeFlight.admin_dashboard.flight;


import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.entities.airport.AirportService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.plane.PlaneService;
import com.example.SwipeFlight.server.utils.BinderClass;

/**
 * The class displays views related to admin's ability add a new flight.
 */
@Controller
public class AdminFlightController {

	// dependency injections
    @Autowired
    private BinderClass binderClass; // register custom editors for converting strings to specific types during data binding.
	@Autowired
	private AdminFlightService adminFlightService;
	@Autowired
	private PlaneService planeService;
	@Autowired
	private AirportService airportService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binderClass.initBinder(binder);
    }
	
    /**
     * The method is responsible for displaying "adminAddFlight" page
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminAddFlight") 
	public String viewAdminAddFlightPage(ModelMap model)
	{
		// initialize a new flight
		model.addAttribute("flight", new Flight());
		// display all planes
	    model.addAttribute("planesList", planeService.getAllPlanes());
	    // display airports
	    model.addAttribute("departureAirportsList", airportService.getAllDepartureAirports());
	    
	    return "adminAddFlight";
	}
	
	/**
	 * The method is responsible for processing new flight insertion.
	 * 		- if there are rejected values -> return to the form with the errors.
	 * 		- otherwise- fields are legal -> insert flight, and display a success message
	 * @param flight- the flight entered by user
	 * @param model- to pass attributes to the view.
	 * @param redirectAttributes - attributes which were passed through redirects.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return the URL of the page which should be rendered. (accomplish Post-Redirect-Get pattern)
	 */
	@PostMapping("/adminAddFlight/ProcessFlightInsertion")
	public String processFlightInsertion(Flight flight, ModelMap model,
									RedirectAttributes redirectAttributes, BindingResult result)
	{
	    // Notation:
	    // The following request parameters are received as strings to this layer, and might be empty.
	    // initBinder() function receives each and changes their value to null, in case the string is null,
	    // before reaching the controller.
	    Date departureDate = flight.getDepartureDate();
	    Time departureTime = flight.getDepartureTime();
	    double ticketPrice = flight.getTicketPrice();
	    Duration duration = flight.getDuration();
	    
		if (departureDate == null)
			result.rejectValue("departureDate", "error.flight", "Field is required.");
		if (departureTime == null)
			result.rejectValue("departureTime", "error.flight", "Field is required.");
	    if (ticketPrice <= 0)
	    	result.rejectValue("ticketPrice", "error.flight", "Invalid value.");
	    
	    if (duration == null)
	    	result.rejectValue("duration", "error.flight", "Field is required.");
	    else if (duration.isZero())
	    	result.rejectValue("duration", "error.flight", "Please enter duration according to the correct format.");
	    
	    result = adminFlightService.validateFlightInsertion(flight, result);
	    
    	// for Post-Redirect-Get pattern
        // 		add errors and the object the errors relate to,
    	// 		to redirect attributes so they will be passed even during redirect.
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("flight", flight);
        
        // failure:
	    if (result.hasErrors()) {
	    	return "redirect:/adminAddFlightRedirect"; // Post-Redirect-Get pattern
	    }
	    // success:
    	// insert flight
    	adminFlightService.insertFlight(flight);

		return "redirect:/adminAddFlightRedirect"; // Post-Redirect-Get pattern
	}
	
    /**
     * The method is added due to Post-Redirect-Get pattern (to avoid form re-submission)
     * @param model- to pass attributes to the view.
     * @param redirectAttributes - to retrieve BindingResult the object is refers to, saved during POST
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminAddFlightRedirect") 
	public String adminAddFlightRedirect(Model model, RedirectAttributes redirectAttributes)
	{
		Flight flight = null;
		
		// if previous POST request saved the necessary elements 
        if (model.asMap().containsKey("result") && model.asMap().containsKey("flight"))
        {
        	// result
        	BindingResult result = (BindingResult)model.asMap().get("result");
            model.addAttribute("org.springframework.validation.BindingResult.flight",
                    model.asMap().get("result"));
            
            // flight
            flight = (Flight)model.asMap().get("flight");
            model.addAttribute("flight", flight);
            
            if (!result.hasErrors())
            {
            	redirectAttributes.addFlashAttribute("add_flight_success_message", "Flight was added successfully.");
            	return "redirect:/adminRoutes";
            }
        }
        
        // continue actions of GET request
        // --------------------------------------------
        if(flight == null)
			// initialize a new flight
			model.addAttribute("flight", new Flight());
        else
        	model.addAttribute("flight", flight);
        
		// display all planes
	    model.addAttribute("planesList", planeService.getAllPlanes());
	    // display airports
	    model.addAttribute("departureAirportsList", airportService.getAllDepartureAirports());
	    
	    return "adminAddFlight"; // Post-Redirect-Get pattern
	}
}
