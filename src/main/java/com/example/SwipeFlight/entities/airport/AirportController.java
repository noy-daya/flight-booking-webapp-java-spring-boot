package com.example.SwipeFlight.entities.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The class displays views related to airports.
 */
@Controller
public class AirportController {
	
	@Autowired // dependency injection
	private AirportService airportService;
	
	/**
	 * The method displays arrival airports list, for the given departureAirportID.
	 * (the return value of this method = response body of the request)
	 * @param departureAirportID
	 * @return arrival airports list, in JSON format.
	 */
    @GetMapping(value="/airport/FetchArrivalAirports{departureAirportID}", produces="application/json")
    @ResponseBody
    public List<Airport> fetchArrivalAirports(@RequestParam("departureAirportID") Long departureAirportID)
    {
        List<Airport> arrivalAirports = airportService.getArrivalAirportsByDepartureAirportID(departureAirportID);
        return arrivalAirports;
    }
}
