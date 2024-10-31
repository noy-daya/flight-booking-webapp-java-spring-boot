package com.example.SwipeFlight.user_dashboard.booking.modify.seat;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.booking_stages.passenger.PassengerListDTO;
import com.example.SwipeFlight.entities.booking.Booking;
import com.example.SwipeFlight.entities.booking.BookingService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.passenger.PassengerService;
import com.example.SwipeFlight.entities.seat.FlightDTO;
import com.example.SwipeFlight.entities.seat.SeatService;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class displays views related to user's ability to modify seats in an existing booking.
 */
@Controller
public class UserBookingModifySeatController {
	
	// dependency injections
	@Autowired
	private UserBookingModifySeatService userBookingModifySeatService;
	@Autowired
	private PassengerService passengerService;
	@Autowired
	private FlightService flightService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private SeatService seatService;
	
    /**
     * The method is responsible for displaying "userModifySeats" page
     * @param model - to pass attributes to the view.
     * @param redirectAttributes - attributes which were passed through redirects.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@GetMapping("/userModifySeats") 
	public String viewUserModifySeatsPage(ModelMap model, RedirectAttributes redirectAttributes,
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
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifySeatController\t Method: viewUserModifySeatsPage "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
	    // get session attribute: user_curBookingID
	    Long curBookingID = (Long) session.getAttribute("user_curBookingID");
	    if (curBookingID == null) {
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifySeatController\t Method: viewUserModifySeatsPage "
					+ "\nDetails: Session attribute 'user_curBookingID' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'user_curBookingID' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
		Booking booking = bookingService.getBookingByID(curBookingID);
		Long routeNumber = booking.getRoute().getRouteNumber();
		int numOfPassengers = booking.getNumOfTickets();
	    List<Flight> flightList = flightService.getAllFlightsInRouteNum(routeNumber);
	    
	    // iterate through flights:
	    // 		1. set its seats matrix
	    // 		2. add the pair (flight, seats) to flightDTOList
		List<FlightDTO> flightDTOList = new ArrayList<>();
		List<Passenger> passengerList = passengerService.getPassengersForBookingID(curBookingID); // including their flightAndSeats
		for (Flight flight : flightList)
		{
			FlightDTO flightDTO = seatService.setFlightAndSeats(flight);
			flightDTOList.add(flightDTO);
		}
		
		StringJoiner initSelectedSeatsJoiner = new StringJoiner(",");

		for (Passenger passenger : passengerList) {
		    for (Flight flight : flightList) {
		        String seat = passenger.getFlightAndSeats().get(flight.getId());
		        if (seat != null && !seat.isEmpty()) {
		            initSelectedSeatsJoiner.add(seat);
		        }
		    }
		}

		String initSelectedSeats = initSelectedSeatsJoiner.toString();
		model.addAttribute("initSelectedSeats", initSelectedSeats);
		model.addAttribute("numOfPassengers", numOfPassengers);
		model.addAttribute("numOfFlights", flightList.size());
		
	    // display passengerListDTO (for user to choose seats for each)
		PassengerListDTO dto = new PassengerListDTO();
		dto.setPassengerList(passengerList);
		model.addAttribute("passengerListDTO", dto);
		// display flightDTOList
		model.addAttribute("flightDTOList", flightDTOList);
		// display flightList
		model.addAttribute("flightList", flightList);
		// display numOfPassengers (=how many seats should be selected)
		model.addAttribute("numOfPassengers", numOfPassengers);
		
		// there is a redirection to this page from processModifySeats function,
		// display any error messages (added to flash and not model), if exist
	    String errorMessage = (String) redirectAttributes.getFlashAttributes().get("error_message");
	    if (errorMessage != null) {
	        model.addAttribute("error_message", errorMessage);
	    }
	    String successMessage = (String) redirectAttributes.getFlashAttributes().get("success_message");
	    if (successMessage != null) {
	        model.addAttribute("success_message", successMessage);
	    }
	    
	    return "userModifySeats";
	}
	
    /**
     * The method is responsible for processing the form in "userModifySeats" page.
     * @param selectedSeats - its structure is: 
     * 			"{seat for pass1 in flight1}, {seat for pass1 in flight2},
     * 			{seat for pass2 in flight1}, {seat for pass2 in flight2}"
     * @param model - to pass attributes to the view.
     * @param redirectAttributes - attributes which were passed through redirects.
     * @param request - to get its session
     * @return the URL of the page which should be rendered.
     * @throws SessionIsInvalidException, SessionAttributeNotFoundException
     */
	@PostMapping("/userModifySeats/ProcessModifySeats") 
	public String processModifySeats(@RequestParam("selectedSeats") String selectedSeats,
									ModelMap model, RedirectAttributes redirectAttributes,
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
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifySeatController\t Method: processModifySeats "
					+ "\nDetails: Session is invalid.");
			throw new SessionIsInvalidException("Session is invalid."); // handler in GlobalExceptionHandler.java
		}
	    // get session attribute: user_curBookingID
	    Long curBookingID = (Long) session.getAttribute("user_curBookingID");
	    if (curBookingID == null) {
	    	System.err.println("\n*** Error ***\nClass: UserBookingModifySeatController\t Method: processModifySeats "
					+ "\nDetails: Session attribute 'user_curBookingID' is not found.");
	        throw new SessionAttributeNotFoundException("Session attribute 'user_curBookingID' is not found."); // handler in GlobalExceptionHandler.java
	    }
	    
	    // -------------------------------------------
	    // actions
	    // -------------------------------------------
		List<Passenger> passengerList = passengerService.getPassengersForBookingID(curBookingID);
		Booking booking = bookingService.getBookingByID(curBookingID);
		Long routeNumber = booking.getRoute().getRouteNumber();
	    List<Flight> flightList = flightService.getAllFlightsInRouteNum(routeNumber);

	    // make sure that user selected all seats, otherwise show error message
	    String[] selectedSeatArray = selectedSeats.split(","); // get individual selected seats
	    if (selectedSeatArray.length != passengerList.size() * flightList.size())
	    {
	    	redirectAttributes.addFlashAttribute("error_message", "Please select a seat for each passenger, on each flight");
	    	return "redirect:/userModifySeats";
	    }
	    
	    // update selected seats, if not legal- display error message
	    boolean isUpdateLegal = userBookingModifySeatService.updateSelectedSeats(passengerList, flightList, selectedSeatArray, curBookingID);
	    if (!isUpdateLegal)
	    {
			redirectAttributes.addFlashAttribute("error_message", "Some of the seats are now taken, please select again");
			return "redirect:/userModifySeats";
	    }
	    
	    // display success message
		redirectAttributes.addFlashAttribute("success_message", "Seats were saved successfuly");
		
	    return "redirect:/userModifySeats";
	}
}
