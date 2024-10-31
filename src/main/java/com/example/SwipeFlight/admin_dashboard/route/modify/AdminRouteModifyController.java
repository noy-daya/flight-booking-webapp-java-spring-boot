package com.example.SwipeFlight.admin_dashboard.route.modify;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.plane.PlaneService;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;
import com.example.SwipeFlight.server.utils.DateTimeUtils;

/**
 * The class displays views related to admin's ability to modify a route:
 * modify flight departure date an time, of flights related to the route.
 */
@Controller
public class AdminRouteModifyController {

	// dependency injections
	@Autowired
	private AdminRouteModifyService adminRouteModifyService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private PlaneService planeService;
	@Autowired
	private RouteService routeService;
	
    /**
     * The method is responsible for displaying "adminRouteModify" page
     * @param routeNumber- the route which the user aims to modify
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminRouteModify{routeNumber}") 
	public String viewAdminModifyRoutePage(@RequestParam("routeNumber") Long routeNumber, ModelMap model)
	{
		// display all flights in route
		model.addAttribute("flightsList", flightService.getAllFlightsInRouteNum(routeNumber));
		
		model.addAttribute("planesList", planeService.getAllPlanes());
		
		Route route = routeService.getRouteByRouteNumber(routeNumber);
		model.addAttribute("is_booking_allowed", route.isBookingAllowed());
		
	    return "adminRouteModify";
	}
	
	/**
	 * The method is responsible for processing flights update (in route).
	 * @param flightIds- list of flightIDs in the route
	 * @param departureDates- list of corresponding departure dates
	 * @param departureTimes- list of corresponding departure times
	 * @return a JSON response (string)
	 */
    @PostMapping("/adminRouteModify/updateAllFlights")
    public ResponseEntity<String> updateAllFlights(@RequestParam("flightIds") List<Long> flightIds,
                                                   @RequestParam("departureDates") List<java.sql.Date> departureDates,
                                                   @RequestParam("departureTimes") List<Time> departureTimes,
                                                   @RequestParam("planeIDs") List<Long> planeIDs) {
        try {
        	
        	adminRouteModifyService.validateFlightsUpdate(flightIds, departureDates, departureTimes, planeIDs);
        	
            // iterate over the lists and update each flight
            for (int i = 0; i < flightIds.size(); i++) {
            	if (planeIDs.size() != 0)
            		adminRouteModifyService.updateFlight(flightIds.get(i), departureDates.get(i), departureTimes.get(i), planeIDs.get(i));
            }
            return ResponseEntity.ok().body("{\"success_message\": \"Flights information was updated successfully.\"}");
        } catch (Exception e) {
            // Return a JSON response with an error message
        	// {the browser -> console} will display an error 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error_message\": \"Error updating flights information: " + e.getMessage() + "\"}");
        }
    }

    /**
     * The method retrieves the updated arrival date and time.
     * @param flight- modified by admin.
     * @return response consisting attributes: arrivalDate, arrivalTime (post update)
     */
    @PostMapping("/adminRouteModify/fetchArrivalDateTime")
    public ResponseEntity<Map<String, Object>> fetchArrivalDateTime(@RequestBody Flight flight) {
    	
    	// calculate the updated arrivalDateTime of the flight
        LocalDateTime newArrivalDateTime = DateTimeUtils.calculateDateTimeAfterDuration(flight.getDepartureDate(), flight.getDepartureTime(), flight.getDuration());

        Map<String, Object> response = new HashMap<>();
        response.put("arrivalDate",  Date.valueOf(newArrivalDateTime.toLocalDate()));
        response.put("arrivalTime",  Time.valueOf(newArrivalDateTime.toLocalTime()));

        return ResponseEntity.ok(response);
    }
}
