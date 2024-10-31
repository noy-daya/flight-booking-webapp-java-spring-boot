package com.example.SwipeFlight.admin_dashboard.route.modify;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.flight.Flight;
import com.example.SwipeFlight.entities.flight.FlightService;
import com.example.SwipeFlight.entities.plane.Plane;
import com.example.SwipeFlight.entities.plane.PlaneService;
import com.example.SwipeFlight.server.utils.Constants;
import com.example.SwipeFlight.server.utils.DateTimeUtils;

/**
 * The class represents Service layer behind admin's ability view and
 * modify flights in route.
 */
@Service
public class AdminRouteModifyService {

	// dependency injections
	@Autowired
	private AdminRouteModifyRepository adminRouteRepository;
	@Autowired
	private FlightService flightService;
	@Autowired
	private PlaneService planeService;
	
	/**
	 * The method updates fields: departure_date, departure_time, arrival_date, arrival_time, status
	 * of the given flight.
	 * @param flightId
	 * @param newDepartureDate
	 * @param newDepartureTime
	 * @param newPlaneID
	 * @throws IllegalArgumentException
	 */
    public void updateFlight(Long flightId, Date newDepartureDate, Time newDepartureTime, Long newPlaneID)
    							throws IllegalArgumentException {
    	// get flight object
    	Flight flight = flightService.getFlightByID(flightId);
    	
    	// indicate flight status: delayed on brought forward
    	int status = indicateStatus(flight, newDepartureDate, newDepartureTime);
    	
	    // update planes availability (old and new)
    	Long oldPlaneID = flight.getPlane().getId();
	    planeService.updateAvailableQuantity(oldPlaneID, false); // availability ++
	    planeService.updateAvailableQuantity(newPlaneID, true);  // availability --

    	
    	// update flights
    	LocalDateTime newArrivalDateTime =  DateTimeUtils.calculateDateTimeAfterDuration(newDepartureDate, newDepartureTime, flight.getDuration());
    	adminRouteRepository.updateFlight(flight, newDepartureDate, newDepartureTime, newArrivalDateTime, status, newPlaneID);
    	
    }
    
	// in case of flight modification (date and time)
	// we can only update to a date&time later than the old date&time
    /**
     * The method returns the corresponding constant of the flight status (delayed or brought forward).
     * (Is called after admin modified its departure date and/or departure time).
     * @param flight- to retrieve the old date and time
     * @param newDepartureDate
     * @param newDepartureTime
     * @return corresponding constant of the flight status
     */
	private int indicateStatus(Flight flight, java.sql.Date newDepartureDate, Time newDepartureTime)
	{
        java.sql.Date oldDepartureDate = flight.getDepartureDate();
        Time oldDepartureTime = flight.getDepartureTime();
        
        LocalDateTime oldDepartureDateTime = LocalDateTime.of(
        		oldDepartureDate.toLocalDate(),
        		oldDepartureTime.toLocalTime()
        );
        
        LocalDateTime newDepartureDateTime = LocalDateTime.of(
        		newDepartureDate.toLocalDate(),
        		newDepartureTime.toLocalTime()
        );
        
        if(oldDepartureDateTime.isBefore(newDepartureDateTime))
        	return Constants.FLIGHT_STATUS_DELAYED;
        return Constants.FLIGHT_STATUS_BROUGHT_FORWARD;
	}

	/**
	 * The method checks validity of updating a flight in route, relative to the other flights in the route.
	 * @param flightIds- list of flightIDs in the route
	 * @param departureDates- list of corresponding departure dates
	 * @param departureTimes- list of corresponding departure times
	 * @param planeIDs - list of corresponding plane ids
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
    public void validateFlightsUpdate(List<Long> flightIds, List<Date> departureDates, List<Time> departureTimes, List<Long> planeIDs)
    {
    	if (flightIds.size() > 1)
    	{
    		// -------------------------------
    		// date + time
    		// -------------------------------
	    	 // if not all lists have the same size
	        if (flightIds.size() != departureDates.size() || flightIds.size() != departureTimes.size()) {
	            throw new IllegalArgumentException("Number of flight IDs, departure dates, and departure times must be equal.");
	        }
	        
	        LocalDateTime flight1ArrivalDateTime =  DateTimeUtils.calculateDateTimeAfterDuration(
	                departureDates.get(0), departureTimes.get(0), flightService.getFlightByID(flightIds.get(0)).getDuration());
	
	        LocalDateTime flight2DepartureDateTime = LocalDateTime.of(
	                departureDates.get(1).toLocalDate(), departureTimes.get(1).toLocalTime());
	
	        // if flight_2_departure is not after flight_1_arrival
	        if (!flight2DepartureDateTime.isAfter(flight1ArrivalDateTime)) {
	            throw new IllegalArgumentException("The departure time of Flight 2 must be later than the arrival time of Flight 1.");
	        }
	
	        // if flight_2_departure is more than 8 hours after flight_1_arrival
	        if (flight2DepartureDateTime.isAfter(flight1ArrivalDateTime.plus(Duration.ofHours(8)))) {
	            throw new IllegalArgumentException("The departure time of Flight 2 must occur within 8 hours following the arrival time of Flight 1.");
	        }
	        
    		// -------------------------------
    		// planes
    		// -------------------------------
	        if (planeIDs.contains(0L))
	        	throw new IllegalArgumentException("Please select a plane for each flight.");
	        
	        Plane flight1NewPlane = planeService.getPlaneByID(planeIDs.get(0));
	        Plane flight2NewPlane = planeService.getPlaneByID(planeIDs.get(1));
	        Plane flight1OldPlane = planeService.getPlaneByID(flightService.getFlightByID(flightIds.get(0)).getPlane().getId());
	        Plane flight2OldPlane = planeService.getPlaneByID(flightService.getFlightByID(flightIds.get(1)).getPlane().getId());
	        
	        // a different plane was selected
	        if((flight1NewPlane.getId() != flight1OldPlane.getId()) || (flight2NewPlane.getId() != flight2OldPlane.getId()))
	        {
		        if((planeIDs.get(0) == planeIDs.get(1))
		        		&& (flight1NewPlane.getAvailableQuantity() < 2))
		        	throw new IllegalArgumentException("Please select an available plane both flights.");
		        
		        // size of planes must be equal
		        if(flight1NewPlane.getNumOfRows() * flight1NewPlane.getNumOfSeatsPerRow()
						!= flight2NewPlane.getNumOfRows() * flight2NewPlane.getNumOfSeatsPerRow())
		        	throw new IllegalArgumentException("Both planes must include an equal number of seats.");
	        }
    	}
    	else
    	{
    		// -------------------------------
    		// plane
    		// -------------------------------
    		Plane flight1NewPlane = planeService.getPlaneByID(planeIDs.get(0));
	        Plane flight1OldPlane = planeService.getPlaneByID(flightService.getFlightByID(flightIds.get(0)).getPlane().getId());
	        
	        // a different plane was selected
	        if((flight1NewPlane.getId() != flight1OldPlane.getId()))
	        {
		        // planes are not chosen
		        if (planeIDs.contains(0L))
		        	throw new IllegalArgumentException("Please select a plane for the flight.");
		        
		        // size of planes
		        if(flight1NewPlane.getAvailableQuantity() < 1)
		        	throw new IllegalArgumentException("Please select an available plane for the flight.");
	        }
    	}
        
    }
}
