package com.example.SwipeFlight.admin_dashboard.route.cancel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The class displays views related to admin's ability to cancel route.
 */
@Controller
public class AdminRouteCancelController {
	
	// dependency injections
	@Autowired
	private AdminRouteCancelService adminRouteCancelService;
	
	/**
	 * The method is responsible for processing route cancellation.
	 * @param routeNumber- the route which user aims to cancel
	 * @param model- to pass attributes to the view.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @param request
	 * @return the URL of the page which should be rendered.
	 */
	@PostMapping("/adminRoutes/ProcessRouteCancellation{routeNumber}")
	public String processRouteCancellation(@RequestParam("routeNumber") Long routeNumber, ModelMap model)
	{
		// cancel the flight (executes all consequences of the cancellation)
		adminRouteCancelService.cancelRoute(routeNumber);
		
		return "redirect:/adminRoutes";
	}

}
