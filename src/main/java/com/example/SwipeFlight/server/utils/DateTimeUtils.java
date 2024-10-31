package com.example.SwipeFlight.server.utils;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The class includes calculation methods associated with Date and Time.
 */
public class DateTimeUtils {
	
	/**
	 * The method receives date, time and duration, and returns the result of {date&time} + duration,
	 * 		converted to LocalDateTime.
	 * Cases in which the result extends past midnight are taken into account.
	 * 		- For example:
	 * 			date 2024-05-21, time 21:00, duration 6:00 hours -> result will be 2024-05-22 03:00
	 * @param departureDate- primary date
	 * @param departureTime- primary time
	 * @param duration- the amount of time to add
	 * @throws IllegalArgumentException
	 */
	public static LocalDateTime calculateDateTimeAfterDuration(Date startDate, Time startTime, Duration duration)
												throws IllegalArgumentException
	{
		try {
			LocalDateTime dateTimeAfterDuration = LocalDateTime.of(startDate.toLocalDate(), startTime.toLocalTime());
			return dateTimeAfterDuration.plus(duration);
		}
		catch (Exception e) {
        	System.err.println("\n*** Error ***\nClass: DateTimeUtils\t Method: calculateDateTimeAfterDuration "
					+ "\nDetails: " + e.getMessage());
        	throw new IllegalArgumentException("Calculation of localDateTime Failed.", e);
		}
	}
	
    /**
     * The method checks if the combination of current date and time and a specified number of days
     * in the future is before a given date and time. 
     * @param date
     * @param time
     * @param offsetDays
     * @return True - given date&time is more than offsetDays before now, otherwise- False
     * @throws IllegalArgumentException
     */
    public static boolean isNowBeforeGivenDateTimeWithOffset(Date date, Time time, int offsetDays)
    														throws IllegalArgumentException
    {
		try {
	        LocalDate localDate = date.toLocalDate(); // convert Date to LocalDate
	        LocalDateTime localDateTime = localDate.atTime(time.toLocalTime()); // convert Time to LocalDateTime
	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime offsetDateTime = now.plusDays(offsetDays); // Get the datetime for 'offsetDays' days from now
	
	        // compare now and offsetDateTime with localDateTime
	        return now.isBefore(localDateTime) && offsetDateTime.isBefore(localDateTime);
		} catch (Exception e) {
	        	System.err.println("\n*** Error ***\nClass: DateTimeUtils\t Method: isNowBeforeGivenDateTimeWithOffset "
						+ "\nDetails: " + e.getMessage());
	        	throw new IllegalArgumentException("Dates Comparisation Failed.", e);
		}
    }

	/**
	 * The method creates a combined timeStamp from separate Date and Time objects,
	 * and returns the timeStamp in milliseconds.
	 * @param date
	 * @param time
	 * @return time stamp
	 * @throws IllegalArgumentException
	 */
	public static long calculateTimestamp(Date date, Time time) throws IllegalArgumentException {
		try {
			// combine date and time into a single java.util.Date object
			java.util.Date combinedDateTime = new java.util.Date(date.getTime() + time.getTime());
			return combinedDateTime.getTime(); // timeStamp of the combined date and time
		} catch (Exception e) {
			System.err.println("\n*** Error ***\nClass: DateTimeUtils\t Method: calculateTimestamp " + "\nDetails: "
					+ e.getMessage());
			throw new IllegalArgumentException("Timestamp calculation Failed.", e);
		}
	}

}
