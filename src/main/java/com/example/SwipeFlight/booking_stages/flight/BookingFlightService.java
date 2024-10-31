package com.example.SwipeFlight.booking_stages.flight;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.booking.BookingService;
import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.passenger.PassengerService;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class represents Service layer of Flight Stage in booking process,
 */
@Service
public class BookingFlightService {

	// dependency injections
	@Autowired
	private RouteService routeService;
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PassengerService passengerService;

	/**
	 * The method retrieves all entries in Route table, that meet the requirements
	 * of the given parameters.
	 * According to the conditions described.
	 * @param departureAirportID
	 * @param arrivalAirportID
	 * @param numOfPassengers
	 * @param departureDate
	 * @return list of entries, or null if no rows exist.
	 */
	public List<Route> getRoutesForFlightRequest(Long departureAirportID, Long arrivalAirportID,
												 int numOfPassengers, Date departureDate) {
		
		List<Route> result = routeService.getRoutesByFilters(null, departureAirportID, arrivalAirportID,
																 departureDate, null);
		// for each route: set flights list, route price
		for (Route route: result)
		{
			routeService.setFlightsListOfRoute(route);
			route.setRoutePriceForSinglePassenger(bookingService.calculatePassengerTotalTicketPrice(route.getRouteNumber()));
		}
		
		// remove routes with insufficient amount of available seats
		// for numOfPassengers
		removeRoutesWithInsufficientSeats(result, numOfPassengers);
		
		// remove routes which are not allowed for booking
		removeRoutesNotAllowedBooking(result);
		
		return result;
	}
	
	/**
	 * The method removes from the given list routes with insufficient amount
	 * of available seats.
	 * @param result
	 * @param numOfPassengers - the required amount of seats
	 * @return result list, after filtering
	 */
    private List<Route> removeRoutesWithInsufficientSeats(List<Route> result, int numOfPassengers) {
        Iterator<Route> iterator = result.iterator();
        
        while (iterator.hasNext()) {
            Route route = iterator.next();
            boolean hasInsufficientSeats = false;

            // iterate through its flights
            for (Flight flight : route.getFlights()) {
                int totalSeatsAmount = flight.getPlane().getNumOfRows() * flight.getPlane().getNumOfSeatsPerRow();
                if (passengerService.getNumOfPassengersInFlight(flight.getId()) + numOfPassengers > totalSeatsAmount) {
                    hasInsufficientSeats = true;
                    break;
                }
            }
            if (hasInsufficientSeats) {
                iterator.remove();
            }
        }
        return result;
    }
    
	/**
	 * The method removes from the given list routes which are not allowed for booking.
	 * @param result
	 * @return result list, after filtering
	 */
    private List<Route> removeRoutesNotAllowedBooking(List<Route> result) {
        Iterator<Route> iterator = result.iterator();
        
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (!route.isBookingAllowed()) {
                iterator.remove();
            }
        }
        return result;
    }
}
