package com.example.SwipeFlight.user_dashboard.booking.cancel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer behind user's ability to cancel an existing booking.
 */
@Service
public class UserBookingCancelService {

	@Autowired // dependency injection
	private UserBookingCancelRepository userbookingRepository;
	
	/**
	 * The method executes all the actions as a result of booking cancellation.
	 * @param bookingID
	 * @throws IllegalArgumentException
	 */
	public void cancelBooking(Long bookingID) throws IllegalArgumentException
	{
		userbookingRepository.cancelBooking(bookingID);
	}
}
