package com.example.SwipeFlight.entities.booking;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.luggage.LuggageService;
import com.example.SwipeFlight.entities.passenger.Passenger;
import com.example.SwipeFlight.entities.route.Route;
import com.example.SwipeFlight.entities.route.RouteService;

/**
 * The class represents Service layer of Booking.
 * (interacts with Repository layer)
 */
@Service
public class BookingService {
	
	// dependency injections
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private RouteService routeService;
	@Autowired
	private LuggageService luggageService;
	
	/**
	 * The method retrieves a list of all the bookings of the given user.
	 * The method sets the route property for each element.
	 * @param userID - owner of the bookings
	 * @return list of bookings, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Booking> getBookingsForUserID(Long userID) throws IllegalArgumentException
	{
		List<Booking> result = bookingRepository.getBookingsForUserID(userID);
		for (Booking booking: result)
			setRouteOfBooking(booking);
		return result;
	}
	
	/**
	 * The method retrieves a list of all bookings.
	 * @return list of bookings, or an empty list if no rows exist.
	 * @param bookingID
	 * @param userID
	 * @param isCanceled
	 * @throws IllegalArgumentException
	 */
	public List<Booking> getBookingsByFilters(Long bookingID, Long userID, Boolean isCanceled) throws IllegalArgumentException
	{
		return bookingRepository.getBookingsByFilters(bookingID, userID, isCanceled);
	}
	
	/**
	 * The method retrieves a booking by its id.
	 * The method sets the route property of the element.
	 * @return booking object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Booking getBookingByID(Long bookingID) throws IllegalArgumentException
	{
		Booking booking = bookingRepository.getBookingByID(bookingID);
		setRouteOfBooking(booking);
		return booking;
	}
	
	/**
	 * The method retrieves routeID for a given bookingID.
	 * @return routeID, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getRouteNumberOfBookingByID(Long bookingID) throws IllegalArgumentException {
		return bookingRepository.getRouteNumberOfBookingByID(bookingID);
	}
	
	/**
	 * The method retrieves the total price of the booking: all tickets and luggage.
	 * @param passengerList- list of passengers relating to the booking
	 * @param routeNumber- of the booking
	 * @return total booking price
	 */
	public double calculateBookingTotalPrice(List<Passenger> passengerList, Long routeNumber)
	{
		double total_luggage_price = 0, total_ticket_price = 0;
		for (Passenger passenger : passengerList) {
			total_luggage_price += calculatePassengerTotalLuggagePrice(passenger);
		}
		total_ticket_price += routeService.getRouteByRouteNumber(routeNumber).getRoutePriceForSinglePassenger() * passengerList.size();
		
		return total_luggage_price + total_ticket_price;
	}
	
	/**
	 * The method returns the total luggage price for a single passenger.
	 * @param passenger
	 * @return total luggage price
	 */
	public double calculatePassengerTotalLuggagePrice(Passenger passenger)
	{
		double total_luggage_price = 0;
		
		Map<Long, List<String>> flightAndLuggage = passenger.getFlightAndLuggage();

		// Iterate through passenger's flight IDs
		for (Map.Entry<Long, List<String>> entry : flightAndLuggage.entrySet())
		{
			Long flightId = entry.getKey();
			
			// get passenger's luggage IDs, relating to flightId
			List<String> luggageIds = flightAndLuggage.getOrDefault(flightId, List.of());
			
			// Iterate through passenger's luggage IDs
			for (String luggageId : luggageIds) {
				Long longLuggageId = Long.parseLong(luggageId);
				total_luggage_price = total_luggage_price + luggageService.getLuggagePrice(longLuggageId);
			}
		}
		return total_luggage_price;
	}
	
	/**
	 * The method returns the total ticket price for a single passenger, in the given route.
	 * 		if such route does not exist- return 0
	 * @param routeNumber- of the booking
	 * @return total tickets price
	 */
	public double calculatePassengerTotalTicketPrice(Long routeNumber)
	{
		Route route = routeService.getRouteByRouteNumber(routeNumber);
		if (route != null)
			return route.getRoutePriceForSinglePassenger();
		return 0;
	}
	
	/**
	 * The method sets the route of the given booking
	 * @param booking
	 */
	private void setRouteOfBooking(Booking booking)
	{
		Long routeNumber = bookingRepository.getRouteNumberOfBookingByID(booking.getId());
		Route route = (Route)routeService.getRouteByRouteNumber(routeNumber);
		booking.setRoute(route);
	}
	
	/**
	 * The method updates the total price of the given booking ID.
	 * @param bookingID
	 * @param newTotalPrice
	 * @throws throws IllegalArgumentException
	 */
	public void updateTotalPrice(Long bookingID, double newTotalPrice) throws IllegalArgumentException
	{
		bookingRepository.updateTotalPrice(bookingID, newTotalPrice);
	}
	
	/**
	 * The method last_modify_date for a given bookingID.
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void updateLastModify(Long bookingID) throws IllegalArgumentException
	{
		bookingRepository.updateLastModify(bookingID); 
	}
	
}
