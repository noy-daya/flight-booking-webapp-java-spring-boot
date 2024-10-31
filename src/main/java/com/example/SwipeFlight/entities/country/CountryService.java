package com.example.SwipeFlight.entities.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer of Country.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class CountryService {

	@Autowired // dependency injection
	CountryRepository countryRepository;
	
	/**
	 * The method retrieves a country by its id.
	 * @param countryID
	 * @return country object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Country getCountryByID(Long countryID) throws IllegalArgumentException {
		return countryRepository.getCountryByID(countryID);
	}

}
