package com.example.SwipeFlight.admin_dashboard.route.add;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class displays views related to admin's ability to add flights to an existing route.
 */
@Controller
public class AdminRouteAddFlightController {

	// dependency injections
	@Autowired
	private AdminRouteAddFlightService adminRouteAddFlightService;
	@Autowired
	private RouteService routeService;
	@Autowired
	private FlightService flightService;
	
    /**
     * The method is responsible for displaying "adminRouteAddFlight" page
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminRouteAddFlight") 
	public String viewAdminAddToRoutePage(Route route, ModelMap model)
	{
		// get all the full details of route
		route = routeService.getRouteByRouteNumber(route.getRouteNumber());
		
		// display all the direct flights which can be added to the flight sequence in route
		List<Flight> suggestedFlights = adminRouteAddFlightService.getSuggestedFlightsToAdd(route);
		if(!suggestedFlights.isEmpty())
			model.addAttribute("suggestedFlights", suggestedFlights);
		
	    return "adminRouteAddFlight";
	}
	
	/**
	 * The method is responsible for processing flight addition into an existing route.
	 * 		- if there are rejected values -> return to the form with the errors.
	 * 		- otherwise- fields are legal -> add flight to route, and display a success message
	 * @param route- with flights selected by user
	 * @param model- to pass attributes to the view.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @param request
	 * @return the URL of the page which should be rendered.
	 */
	@PostMapping("/adminRouteAddFlight/ProcessAddFlightToRoute")
	public String processAddFlightToRoute(Route route, ModelMap model, BindingResult result)
	{
	    result = adminRouteAddFlightService.validateAddFlightToRoute(route, result);
	    if (!result.hasErrors())
	    {
	    	// add the selected flight to the route
	    	adminRouteAddFlightService.addFlightToRoute(route);
	        model.addAttribute("success_message", "The flight was added to the route successfully.");
	    }
	    
	    // display the route with its flights
	    route = routeService.getRouteByRouteNumber(route.getRouteNumber());
	    model.addAttribute("flightsInRouteList", adminRouteAddFlightService.getSuggestedFlightsToAdd(route));
	    
	    return "adminRouteAddFlight";
	}
	
	/**
	 * The method displays list of all Flight objects related to the given routeNumber.
	 */
    @GetMapping("/adminRouteAddFlight/FetchFlightsInRouteNum{routeNumber}")
    @ResponseBody
    public List<Flight> fetchFlightsInRouteNum(@RequestParam("routeNumber") Long routeNumber) {
        List<Flight> flights = flightService.getAllFlightsInRouteNum(routeNumber);
        return flights;
    }
}
