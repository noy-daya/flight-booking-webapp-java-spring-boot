package com.example.SwipeFlight.entities.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.city.City;
import com.example.SwipeFlight.entities.city.CityService;

/**
 * The class represents Service layer of Airport.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class AirportService {
	
	// dependency injections
	@Autowired
	private AirportRepository airportRepository;
	@Autowired 
	private CityService cityService; // to set the city attribute for each airport
	
	/**
	 * The method retrieves a list of all airports.
	 * The method sets the airport city property for each element, using cityService property
	 * @return list of airport objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Airport> getAllDepartureAirports() throws IllegalArgumentException
	{
		List<Airport> result = airportRepository.getAllDepartureAirports();
		for (Airport airport: result)
			setCityOfAirport(airport);
		return result;
	}
	
    /**
     * The method retrieves a list of all arrival airports for a given departureAirportID.
     * The method sets the airport city property for each element, using cityService property.
     * if no rows are found -> returns null
     * (= all airports, excluding the given departureAirportID)
     * @param departureAirportID
     * @return list of airport objects, or an empty list if no rows exist.
     * @throws IllegalArgumentException
     */
	public List<Airport> getArrivalAirportsByDepartureAirportID(Long departureCityID) throws IllegalArgumentException
	{
		List<Airport> result = airportRepository.getArrivalAirportsByDepartureAirportID(departureCityID);
		for (Airport airport: result)
			setCityOfAirport(airport);
		return result;
	}
	
	/**
	 * The method retrieves an airport by its id.
	 * The method sets the airport city property of the element, using cityService property.
	 * @param id
	 * @return airport object or null if no rows found.
	 * @throws IllegalArgumentException
	 */
	public Airport getAirportByID(Long id) throws IllegalArgumentException
	{
		Airport result = airportRepository.getAirportByID(id);
		setCityOfAirport(result);
		return result;
	}
	
	/**
	 * The method retrieves cityID for a given airportID.
	 * @param id
	 * @return cityID or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getCityIdOfAirportByID(Long id) throws IllegalArgumentException {
		return airportRepository.getCityIdOfAirportByID(id);
	}
	
	/**
	 * The method sets the city of the given airport, using cityService property
	 * @param airport
	 */
	private void setCityOfAirport(Airport airport)
	{
		Long cityID = airportRepository.getCityIdOfAirportByID(airport.getId());
		City city = (City)cityService.getCityByID(cityID);
		airport.setCity(city);
	}
}