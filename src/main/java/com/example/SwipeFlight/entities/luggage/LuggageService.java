package com.example.SwipeFlight.entities.luggage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer of Luggage.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class LuggageService {
	
	@Autowired // dependency injection
	private LuggageRepository luggageRepository;
	
	/**
	 * The method retrieves a list of luggage objects existing in database.
	 * @return list of luggage objects, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Luggage> getLuggageTypes() throws IllegalArgumentException{
		return luggageRepository.getLuggageTypes();
	}
	
	/**
	 * The method retrieves price for a given luggageID.
	 * @param luggageID
	 * @return price for luggage, or 0 if no rows found.
	 * @throws IllegalArgumentException
	 */
	public double getLuggagePrice(Long luggageID) throws IllegalArgumentException
	{
		return luggageRepository.getLuggagePrice(luggageID);
	}

}
