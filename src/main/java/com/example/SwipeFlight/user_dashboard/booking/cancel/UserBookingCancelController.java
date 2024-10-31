package com.example.SwipeFlight.user_dashboard.booking.cancel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The class displays views related to user's ability to cancel an existing booking.
 */
@Controller
public class UserBookingCancelController {

	@Autowired // dependency injection
	private UserBookingCancelService userBookingCancelService;
	
	/**
	 * The method is responsible for processing user's request to cancel the given booking.
	 * @param bookingID
	 * @return the URL of the page which should be rendered.
	 */
	@PostMapping("/userBookings/ProcessBookingCancellation{bookingID}")
	public String processBookingCancellation(@RequestParam("bookingID") Long bookingID)
	{
		userBookingCancelService.cancelBooking(bookingID);
		return "redirect:/userBookings";
	}
}
