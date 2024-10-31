package com.example.SwipeFlight.admin_dashboard.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SwipeFlight.entities.booking.BookingService;

/**
 * The class displays views related to admin's ability to view bookings.
 */
@Controller
public class AdminBookingsController {
	
	// dependency injections
	@Autowired
	private BookingService bookingService;
	
    /**
     * The method is responsible for displaying "adminBookings" page,
     * considering the given filter parameters (not required).
     * @param bookingID
     * @param userID
     * @param isCanceled
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
    @GetMapping("/adminBookings")
    public String viewAdminBookingsPage(@RequestParam(value = "bookingID", required = false) Long bookingID,
    									@RequestParam(value = "userID", required = false) Long userID,
    									@RequestParam(value = "isCanceled", required = false) Boolean isCanceled,
    									Model model) {
    	
        boolean isCanceledValue = (isCanceled != null) ? isCanceled : false;
    	
    	// display bookings for filters
    	model.addAttribute("bookingsList", bookingService.getBookingsByFilters(bookingID, userID, isCanceledValue));
    	
        return "adminBookings";
    }

}
