package com.example.SwipeFlight.user_dashboard.booking.info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.booking_stages.passenger.PassengerListDTO;
import com.example.SwipeFlight.entities.booking.Booking;
import com.example.SwipeFlight.entities.booking.BookingService;
import com.example.SwipeFlight.entities.flight_status.FlightStatusService;
import com.example.SwipeFlight.entities.luggage.LuggageService;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.passenger.PassengerService;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.server.utils.custom_exceptions.RequestAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class represents Service layer behind user's ability to view the full details
 * of the bookings he submitted.
 */
@Controller
public class UserBookingInfoController {
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private LuggageService luggageService;
	
	@Autowired
	private FlightStatusService flightStatusService;
	
	/**
	 * The method saves the given bookingID in user's session.
	 * @param bookingID
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/userBookings/saveInSessionUserBookingID{bookingID}") 
	@ResponseBody
	public String saveInSessionUserBookingID(@RequestParam("bookingID") Long bookingID,
											Model model, HttpServletRequest request)
											throws SessionIsInvalidException
	{
		// -------------------------------------------
		// get session
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: UserBookingInfoController\t Method: saveInSessionUserBookingID "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
		// -------------------------------------------
		// save attributes in session
		// -------------------------------------------
		session.setAttribute("user_curBookingID", bookingID);
		
		return "userModifyLuggage";
	}
	
    /**
     * The method is responsible for displaying "userBookings" page
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     * @throws RequestAttributeNotFoundException
     */
	@GetMapping("/userBookings") 
	public String viewUserBookingsPage(ModelMap model, HttpServletRequest request)
										throws RequestAttributeNotFoundException
	{
    	// get sessionUser from request
		User sessionUser = (User)request.getAttribute("sessionUser");
	    if (sessionUser == null) {
	    	System.err.println("\n*** Error ***\nClass: UserBookingInfoController\t Method: viewUserBookingsPage "
					+ "\nDetails: Request attribute 'sessionUser' is not found.");
	        throw new RequestAttributeNotFoundException("request attribute 'sessionUser' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    List<Booking> bookings = bookingService.getBookingsForUserID(sessionUser.getId());
	    
	    // display user's bookings
	    model.addAttribute("bookingsList", bookings);
	    
	    
	    // display flight statuses
	 	model.addAttribute("FlightStatusList", flightStatusService.getStatuses());
	 		
	    return "userBookings";
	}
	
    /**
     * The method is responsible for displaying "userBookingInfo" page, for a given bookingID.
     * @param bookingID - to display its information
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/userBookingInfo{bookingID}") 
	public String viewUserBookingInfoPage(@RequestParam("bookingID") Long bookingID, ModelMap model)
	{
		Booking booking = bookingService.getBookingByID(bookingID);
		
		// display route
		Route route = booking.getRoute();
		model.addAttribute("route", route);
		
		// display passengers
		List<Passenger> passengerList = passengerService.getPassengersForBookingID(bookingID);
		PassengerListDTO dto = new PassengerListDTO();
		dto.setPassengerList(passengerList);
		model.addAttribute("passengerListDTO", dto);
		
        // iterate over each passenger and calculate total luggage price
        for (Passenger passenger : passengerList) {
            double totalLuggagePrice = bookingService.calculatePassengerTotalLuggagePrice(passenger); // so that the summary will appear for each passenger in jsp page
            // Add total luggage price for this passenger to the model
            model.addAttribute("totalLuggagePriceForPassenger_" + passenger.getPassportID(), totalLuggagePrice);
        }
        // display total tickets for passenger
        model.addAttribute("totalTicketPriceForPassenger", bookingService.calculatePassengerTotalTicketPrice(route.getRouteNumber()));
        
        // display total price
        model.addAttribute("totalPrice", booking.getTotalPrice());
	    
		// display luggage list
		model.addAttribute("LuggageList", luggageService.getLuggageTypes());
	    
		// display booking id
		model.addAttribute("bookingID", bookingID);
		
	    return "userBookingInfo";
	}
	
}
