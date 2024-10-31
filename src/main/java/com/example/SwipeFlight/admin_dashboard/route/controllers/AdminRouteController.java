package com.example.SwipeFlight.admin_dashboard.route.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.entities.airport.AirportService;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class displays views related to admin's ability to view upcoming routes.
 */
@Controller
public class AdminRouteController {
	
	@Autowired // dependency injection
	private RouteService routeService;
	
	@Autowired // dependency injection
	private AirportService airportService;
	
    /**
     * The method is responsible for displaying "adminRoutes" page,
     * considering the given filter parameters (not required).
     * @param routeNumber
     * @param departureAirportID
     * @param arrivalAirportID
     * @param departureDate - default value is "1970-01-01" (in case it's null)
     * @param arrivalDate - default value is "1970-01-01" (in case it's null)
     * @param model - to pass attributes to the view.
     * @param redirectAttributes - attributes which were passed through redirects.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminRoutes") 
	public String viewAdminRoutesPage(@RequestParam(value = "routeNumber", required = false) Long routeNumber,
						            @RequestParam(value = "departureAirportID", required = false) Long departureAirportID,
						            @RequestParam(value = "arrivalAirportID", required = false) Long arrivalAirportID,
						            @RequestParam(value = "departureDate", required = false, defaultValue = "1970-01-01") Date departureDate,
						            @RequestParam(value = "arrivalDate", required = false, defaultValue = "1970-01-01") Date arrivalDate,
						    		Model model, RedirectAttributes redirectAttributes) {
		// display routes for filters
    	model.addAttribute("routesList", routeService.getRoutesByFilters(routeNumber, departureAirportID, arrivalAirportID, departureDate, arrivalDate));
    	
    	// display airports
    	model.addAttribute("airportsList", airportService.getAllDepartureAirports());
	    
		// there is a redirection to this page from processFlightInsertion function,
		// display any error messages (added to flash and not model), if exist
	    String add_flight_success_message = (String) redirectAttributes.getFlashAttributes().get("add_flight_success_message");
	    if (add_flight_success_message != null) {
	        model.addAttribute("add_flight_success_message", add_flight_success_message);
	    }
	    
	    return "adminRoutes";
	}
	
}
