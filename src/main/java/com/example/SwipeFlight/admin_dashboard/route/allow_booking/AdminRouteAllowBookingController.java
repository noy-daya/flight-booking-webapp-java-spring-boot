package com.example.SwipeFlight.admin_dashboard.route.allow_booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class displays views related to admin's ability to allow bookings for route.
 */
@Controller
public class AdminRouteAllowBookingController {

	@Autowired
	private RouteService routeService;
	/**
	 * The method is responsible for processing route booking permission.
	 * @param routeNumber- the route which user aims to allow bookings for
	 * @param model- to pass attributes to the view.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return the URL of the page which should be rendered.
	 */
	@PostMapping("/adminRoutes/ProcessRouteAllowBooking{routeNumber}")
	public String processRouteCancellation(@RequestParam("routeNumber") Long routeNumber, ModelMap model)
	{
		// cancel the flight (executes all consequences of the cancellation)
		routeService.updateAllowBooking(routeNumber, true);
		
		return "redirect:/adminRoutes";
	}
}
