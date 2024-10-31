package com.example.SwipeFlight.entities.city;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.entities.country.Country;
import com.example.SwipeFlight.entities.country.CountryService;

/**
 * The class represents Service layer of City.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class CityService {
	
	// dependency injections
	@Autowired
	private CityRepository cityRepository;
	@Autowired 
	private CountryService countryService;
	
	/**
	 * The method updates a City entry in the database.
	 * @param city - the city to update
	 * @param filePath
	 * @throws IllegalArgumentException
	 */
	public void updateCity(City city, String filePath) throws IllegalArgumentException {
		cityRepository.updateCity(city, filePath);
	}
	
	/**
	 * The method retrieves a city by its id.
	 * The method sets the airport city property of the element, using countryService property.
	 * @return city object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public City getCityByID(Long id) throws IllegalArgumentException {
		City result = cityRepository.getCityByID(id);
		setCountryOfCity(result);
		return result;
	}
	
	/**
	 * The method returns a list of all city names which begin by the given keyboard string.
	 * The method sets the airport city property for each element, using countryService property.
	 * @return list of city names, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<String> getCityNamesByKeyboard(String keyboard) throws IllegalArgumentException {
		return cityRepository.getCityNamesByKeyboard(keyboard);
	}
	
	/**
	 * The method returns a list of all city objects which begin by the given cityName.
	 * @return list of cities, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<City> getCitiesByKeyboard(String keyboard) throws IllegalArgumentException {
		List<City> result = cityRepository.getCitiesByKeyboard(keyboard);
		for (City city: result)
			setCountryOfCity(city);
		return result;
	}
	
	/**
	 * The method retrieves countryID for a given cityID.
	 * @return city object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Long getCountryIdOfCityByID(Long id) throws IllegalArgumentException {
		return cityRepository.getCountryIdOfCityByID(id);
	}
	
	/**
	 * The method sets the country of the given city
	 * @param airport
	 * @throws IllegalArgumentException
	 */
	private void setCountryOfCity(City city) throws IllegalArgumentException
	{
		Long countryID = cityRepository.getCountryIdOfCityByID(city.getId());
		Country country = (Country)countryService.getCountryByID(countryID);
		city.setCountry(country);
	}
}

