package com.example.SwipeFlight.admin_dashboard.feedback;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SwipeFlight.entities.airport.AirportService;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class displays views related to admin's ability to view average scores of user feedbacks for route.
 */
@Controller
public class AdminFeedbackController {
	
	// dependency injections
	@Autowired
	private AdminFeedbackService adminFeedbackService;
	@Autowired
	private RouteService routeService;
	@Autowired
	private AirportService airportService;
	
    /**
     * The method is responsible for displaying "adminFeedbacks" page,
     * considering the given filter parameters (not required).
     * @param routeNumber
     * @param departureAirportID
     * @param arrivalAirportID
     * @param departureDate - default value is "1970-01-01" (in case it's null)
     * @param arrivalDate - default value is "1970-01-01" (in case it's null)
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
    @GetMapping("/adminFeedbacks")
    public String viewAdminFeedbacksPage(@RequestParam(value = "routeNumber", required = false) Long routeNumber,
				            @RequestParam(value = "departureAirportID", required = false) Long departureAirportID,
				            @RequestParam(value = "arrivalAirportID", required = false) Long arrivalAirportID,
				            @RequestParam(value = "departureDate", required = false, defaultValue = "1970-01-01") Date departureDate,
				            @RequestParam(value = "arrivalDate", required = false, defaultValue = "1970-01-01") Date arrivalDate,
				    		Model model) {
		
    	// display routes for filters
    	model.addAttribute("routesList", routeService.getRoutesByFilters(routeNumber, departureAirportID, arrivalAirportID, departureDate, arrivalDate));
    	// display airports
    	model.addAttribute("airportsList", airportService.getAllDepartureAirports());
    	
        return "adminFeedbacks";
    }
    
    /**
     * The method is responsible for displaying "adminFeedbackOfRoute" page
     * @param routeNumber- the selected routeNumber
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
    @GetMapping("/adminFeedbacks/adminFeedbackOfRoute{routeNumber}")
    public String viewadminFeedbackOfRoutePage(@RequestParam("routeNumber") Long routeNumber, Model model)
    {
    	model.addAttribute("routeNumber", routeNumber);
        return "adminFeedbackOfRoute";
    }
    
	/**
	 * The method displays list (of Double values) of the average ratings given to the routeNumber by users,
	 * 		in order to initialize the chart.
	 * (the return value of this method = response body of the request)
	 * @param routeNumber- the selected routeNumber
	 * @return list of ratings (each element is a Double)
	 */
    @GetMapping("/adminFeedbackOfRoute/chartData{routeNumber}")
    @ResponseBody
    public List<Double> getChartData(@RequestParam("routeNumber") Long routeNumber)
    {
    	List<Double> chart_yValues = adminFeedbackService.getAverageRatingsForRouteNum(routeNumber);
        return chart_yValues;
    }


}
