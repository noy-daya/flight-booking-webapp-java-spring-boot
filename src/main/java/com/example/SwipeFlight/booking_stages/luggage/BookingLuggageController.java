package com.example.SwipeFlight.booking_stages.luggage;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SwipeFlight.booking_stages.passenger.PassengerListDTO;
import com.example.SwipeFlight.booking_stages.seat.BookingSeatService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.luggage.LuggageService;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.server.utils.CastUtils;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class displays views related to Luggage Stage in booking process.
 */
@Controller
public class BookingLuggageController {

	// dependency injections
	@Autowired
	private BookingLuggageService bookingLuggageService;
	@Autowired
	private LuggageService luggageService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private BookingSeatService bookingSeatService;
	
    /**
     * The method is responsible for displaying "bookingLuggage" page, given previous stage parameters
     * 	which define the routes that will be displayed.
     * @param model - to pass attributes to the view.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@GetMapping("/bookingLuggage")
	public String viewBookingLuggagePage(Model model, HttpServletRequest request)
										throws SessionIsInvalidException, SessionAttributeNotFoundException
	{
		// -------------------------------------------
		// get session and session's attributes
		// -------------------------------------------
		// get session (if exists)
		HttpSession session = request.getSession(false);
		if (session == null)
		{
	    	System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: viewBookingLuggagePage "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
			
		// get session attribute: booking_passengers
		List<?> rawPassengerList = (List<?>)session.getAttribute("booking_passengers");
	    if (rawPassengerList == null) {
			System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: viewBookingLuggagePage "
					+ "\nDetails: Session attribute 'booking_passengers' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'booking_passengers' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    List<Passenger> passengerList = CastUtils.castList(Passenger.class, rawPassengerList); // cast objects in list
	    
	    // get session attribute: booking_routeNumber
	    Long routeNumber = (Long) session.getAttribute("booking_routeNumber");
	    if (routeNumber == null) {
			System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: viewBookingLuggagePage "
					+ "\nDetails: Session attribute 'booking_routeNumber' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'booking_routeNumber' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
		// display passengerListDTO (for user to choose luggage)
		PassengerListDTO dto = new PassengerListDTO();
		dto.setPassengerList(passengerList);
		model.addAttribute("passengerListDTO", dto);
		
		// display LuggageList
		model.addAttribute("LuggageList", luggageService.getLuggageTypes());
		
		// display countDown for Seats step (for seats reservation)
	    List<Flight> flightList = flightService.getAllFlightsInRouteNum(routeNumber);
		Timestamp seatReservationTime = bookingSeatService
				.getReservedTimeForPassAndFlight(passengerList.get(0).getPassportID(), flightList.get(0).getId());
		if(seatReservationTime != null) // if the rows are already deleted: null
			model.addAttribute("SeatsReservationTime", seatReservationTime);
	    
	    return "bookingLuggage";
	}

    /**
     * The method is responsible for processing the form in "bookingLuggage" page.
     * @param feedbackForm - the values entered by the user
     * @param routeNumber - of the route
     * @param model - to pass attributes to the view.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@PostMapping("/bookingLuggage/ProcessBookingLuggage")
	public String processBookingLuggage(@ModelAttribute("passengerListDTO") PassengerListDTO passengerListDTO,
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
	    	System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: processBookingLuggage "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
		// get session attribute: booking_passengers
		List<?> rawPassengerList = (List<?>)session.getAttribute("booking_passengers");
	    if (rawPassengerList == null) {
			System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: processBookingLuggage "
					+ "\nDetails: Session attribute 'booking_passengers' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'booking_passengers' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    List<Passenger> passengerList = CastUtils.castList(Passenger.class, rawPassengerList); // cast objects in list

	    // get session attribute: booking_routeNumber
	    Long routeNumber = (Long) session.getAttribute("booking_routeNumber");
	    if (routeNumber == null) {
			System.err.println("\n*** Error ***\nClass: BookingLuggageController\t Method: processBookingLuggage "
					+ "\nDetails: Session attribute 'booking_routeNumber' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'booking_routeNumber' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
	    // get flights in the selected route
	    List<Flight> flightList = flightService.getAllFlightsInRouteNum(routeNumber);
	    
	    // iterate through passengerList and update flightAndLuggage for each passenger
	    for (Passenger passenger : passengerList) {
	    	// selected luggage for each passenger
	        String[] luggageIds = request.getParameterValues("passenger_" + passenger.getPassportID() + "_luggageIds");
	        bookingLuggageService.setPassengersLuggage(passenger, flightList, luggageIds);
	    }
	    
		// -------------------------------------------
		// store in session, continue to next step
		// -------------------------------------------
	    // modify the existing booking_passengers in session, so that now there are luggage details
	    session.setAttribute("booking_passengers", passengerList);
	    
	    return "redirect:/bookingSummary"; // pass to next step
	}
}
