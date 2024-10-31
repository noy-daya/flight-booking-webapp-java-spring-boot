package com.example.SwipeFlight.user_dashboard.booking.modify.luggage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.booking_stages.luggage.BookingLuggageService;
import com.example.SwipeFlight.booking_stages.passenger.PassengerListDTO;
import com.example.SwipeFlight.entities.booking.Booking;
import com.example.SwipeFlight.entities.booking.BookingService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.luggage.LuggageService;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.passenger.PassengerService;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class displays views related to user's ability to modify luggage of existing booking.
 */
@Controller
public class UserBookingModifyLuggageController {
	
	// dependency injection
	@Autowired
	private UserBookingModifyLuggageService userBookingModifyLuggageService;
	@Autowired
	private BookingLuggageService bookingLuggageService;
	@Autowired
	private PassengerService passengerService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private LuggageService luggageService;
	
    /**
     * The method is responsible for displaying "userModifySeats" page
     * @param model - to pass attributes to the view.
     * @param redirectAttributes - attributes which were passed through redirects.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@GetMapping("/userModifyLuggage") 
	public String viewUserModifyLuggagePage(ModelMap model, RedirectAttributes redirectAttributes,
											HttpServletRequest request)
											throws SessionIsInvalidException, SessionAttributeNotFoundException
	{
		// -------------------------------------------
		// get session and session's attributes
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifyLuggageController\t Method: viewUserModifyLuggagePage "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
	    // get session attribute: user_curBookingID
	    Long curBookingID = (Long) session.getAttribute("user_curBookingID");
	    if (curBookingID == null) {
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifyLuggageController\t Method: viewUserModifyLuggagePage "
					+ "\nDetails: Session attribute 'user_curBookingID' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'user_curBookingID' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
	    // display passengerListDTO (for user to choose seats for each)
		List<Passenger> passengerList = passengerService.getPassengersForBookingID(curBookingID);
		PassengerListDTO dto = new PassengerListDTO();
		dto.setPassengerList(passengerList);
		model.addAttribute("passengerListDTO", dto);
	    
		// display luggage list
		model.addAttribute("LuggageList", luggageService.getLuggageTypes());
	    
		// there is a redirection to this page from processModifyLuggage function,
		// display any error messages (added to flash and not model), if exist
	    String success_message = (String) redirectAttributes.getFlashAttributes().get("success_message");
	    if (success_message != null) {
	        model.addAttribute("success_message", success_message);
	    }
		
	    return "userModifyLuggage";
	}
	
    /**
     * The method is responsible for processing luggage modification in booking,
     * in "ProcessModifyLuggage" page.
     * @param model - to pass attributes to the view.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@PostMapping("/userModifyLuggage/ProcessModifyLuggage")
	public String processModifyLuggage(@ModelAttribute("passengerListDTO") PassengerListDTO passengerListDTO,
										RedirectAttributes redirectAttributes, HttpServletRequest request) 
										throws SessionIsInvalidException, SessionAttributeNotFoundException {
		// -------------------------------------------
		// get session and session's attributes
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifyLuggageController\t Method: processModifyLuggage "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
		// get session attribute: user_curBookingID
	    Long bookingID = (Long) session.getAttribute("user_curBookingID");
	    if (bookingID == null) {
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifyLuggageController\t Method: processModifyLuggage "
					+ "\nDetails: Session attribute 'user_curBookingID' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'user_curBookingID' is not found."); // handler in GlobalExceptionHandler.java
	    }

		// -------------------------------------------
	    // actions
	    // -------------------------------------------
		Booking booking = bookingService.getBookingByID(bookingID);
		Long routeNumber = booking.getRoute().getRouteNumber();
		
	    // get flights in route related to the booking
	    List<Flight> flightList = flightService.getAllFlightsInRouteNum(routeNumber);
	    
	    // iterate through passengerList and determine flightAndLuggage for each passenger
		List<Passenger> passengerList = passengerService.getPassengersForBookingID(bookingID);
	    for (Passenger passenger : passengerList) {
	    	// selected luggage for each passenger
	        String[] luggageIds = request.getParameterValues("passenger_" + passenger.getPassportID() + "_luggageIds");
	        bookingLuggageService.setPassengersLuggage(passenger, flightList, luggageIds);
	    }
	    
		// update luggage for each passenger in passengerList
		for (Passenger passenger: passengerList)
			userBookingModifyLuggageService.updateLuggageForPassenger(passenger, bookingID);
		
		// update total price in booking
		double newTotalPrice = bookingService.calculateBookingTotalPrice(passengerList, routeNumber);
		bookingService.updateTotalPrice(bookingID, newTotalPrice);
		bookingService.updateLastModify(bookingID);
		
	    redirectAttributes.addFlashAttribute("success_message", "changes were saved successfuly.");
	    return "redirect:/userModifyLuggage";
	}
}
