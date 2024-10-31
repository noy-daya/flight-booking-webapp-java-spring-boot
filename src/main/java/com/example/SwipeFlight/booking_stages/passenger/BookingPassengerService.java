package com.example.SwipeFlight.booking_stages.passenger;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.SwipeFlight.entities.passenger.Passenger;

/**
 * The class represents Service layer of Passenger Stage in booking process,
 * including passengers insertion in case the booking was confirmed by the user.
 */
@Service
public class BookingPassengerService {
	
	@Autowired // dependency injection
	private BookingPassengerRepository bookingPassengerRepository;
	
	/**
	 * The method executes the actions related to inserting a new passenger, as a result of booking:
	 * - Inserts a single row to Passenger table.
	 * - Inserts a row for each flight in the booking, into Passenger_Flight table.
	 * @param passenger - to insert
	 * @param bookingID - reference to the booking in which the passenger appears in
	 */
	public void insertPassenger(Passenger passenger, Long bookingID)
	{
		// -------------------------------------------
		// insert an entry into Passenger table
		// ---------------------------------------------
		bookingPassengerRepository.insertIntoPassenger(passenger, bookingID);
		
		// -------------------------------------------
		// iterate through each flight (key of the map),
		// for each one:
		// 		1. retrieve passenger's seat (for example: "1-3")
		//      2. retrieve passenger's luggageIDs string (for example: "1,2,3")
		//		3. insert an entry to Passenger_flight table
		// -------------------------------------------
		// get passenger's fields
		String passportId = passenger.getPassportID();
		Map<Long, String> flightAndSeats = passenger.getFlightAndSeats();
		Map<Long, List<String>> flightAndLuggage = passenger.getFlightAndLuggage();

		// iterate through each flight (key of the map)
		for (Map.Entry<Long, String> entry : flightAndSeats.entrySet())
		{
			Long flightId = entry.getKey();
			
			// step (1)
			String seatStr = entry.getValue();
			
			// step (2)
			List<String> luggageIds = flightAndLuggage.getOrDefault(flightId, List.of());
			String luggageIdsStr = String.join(",", luggageIds);
			
			// step (3)
			bookingPassengerRepository.insertIntoPassengerFlight(bookingID, passportId, flightId, seatStr, luggageIdsStr);
		}
	}
	
	/**
	 * The method checks if the passenger is already booked for the requested route.
	 * @param passportID - of the passenger
	 * @param routeNum - the requested route
	 * @return True- the passenger has a booking, otherwise- False.
	 */
	public boolean isPassengerBookedToRouteNum(String passportID, Long routeNum)
	{
		return bookingPassengerRepository.isPassengerBookedToRouteNum(passportID, routeNum);
	}
	
	/**
	 * The method checks validity of passengers form entered by the user.
	 * According to the following conditions:
	 * @param passengerList - the values entered by the user, for each passenger
	 * @param routeNumber - the selected route number
	 * @param result - BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validatePassengers(List<Passenger> passengerList, Long routeNumber,
											BindingResult result)
	{
		// iterate through each passenger in the given list
		for (int i = 0; i < passengerList.size(); i++)
		{
	        Passenger passenger = passengerList.get(i);
	        
	        // first name
	        if (passenger.getFirstName().isEmpty()) {
	            result.rejectValue("passengerList[" + i + "].firstName", "error.passengerList", "field is required");
	        }
			else
			{
				String regex = "^[a-zA-Z]+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(passenger.getFirstName());
				if (!matcher.matches())
					result.rejectValue("passengerList[" + i + "].firstName", "error.passengerList", "field must consists only letters");
			}
	        
	        // last name
	        if (passenger.getLastName().isEmpty()) {
	            result.rejectValue("passengerList[" + i + "].lastName", "error.passengerList", "field is required");
	        }
			else
			{
				String regex = "^[a-zA-Z]+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(passenger.getLastName());
				if (!matcher.matches())
					result.rejectValue("passengerList[" + i + "].lastName", "error.passengerList", "field must consists only letters");
			}
	        
	        // gender
	        if (passenger.getGender().equals("0")) {
	            result.rejectValue("passengerList[" + i + "].gender", "error.passengerList", "field is required");
	        }
	        
	        // passportID
	        if (passenger.getPassportID().isEmpty()) {
	            result.rejectValue("passengerList[" + i + "].passportID", "error.passengerList", "field is required");
	        }
	        else if (passenger.getPassportID().length() != 9) {
	            result.rejectValue("passengerList[" + i + "].passportID", "error.passengerList", "must include 9 characters");
	        }
			else
			{
				String regex = "^[a-zA-Z0-9]+$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(passenger.getPassportID());
				if (!matcher.matches())
					result.rejectValue("passengerList[" + i + "].passportID", "error.passengerList", "field consist illegal characters.");
				else
				{
			        // there are no equal passport ids in this booking
			        for (int j = 0; j < passengerList.size(); j++)
					{
			        	if(i != j && passengerList.get(i).getPassportID().equals(passengerList.get(j).getPassportID()))
			        		result.rejectValue("passengerList[" + i + "].passportID", "error.passengerList", "passport ID must be unique");
					}
			        
			        // the passport id has not booked before, for the requested route
			        if (bookingPassengerRepository.isPassengerBookedToRouteNum(passenger.getPassportID(), routeNumber))
			        	result.rejectValue("passengerList[" + i + "].passportID", "error.passengerList", "passport ID is already booked for this route");
				}
			}
		}
		
		return result;
    }

}
