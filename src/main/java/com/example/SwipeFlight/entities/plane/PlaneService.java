package com.example.SwipeFlight.entities.plane;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class represents Service layer of Plane.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class PlaneService {
	
	@Autowired // dependency injection
	PlaneRepository planeRepository;
	
	/**
	 * The method retrieves a list of available planes in the database.
	 * @return list of planes, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Plane> getAvailablePlanes() throws IllegalArgumentException {
		return planeRepository.getAvailablePlanes();
	}
	
	/**
	 * The method retrieves a list all planes in the database.
	 * @return list of planes, or an empty list if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public List<Plane> getAllPlanes() throws IllegalArgumentException {
		return planeRepository.getAllPlanes();
	}
	
	/**
	 * The method retrieves a plane by its id.
	 * @param planeID
	 * @return plane object, or null if no rows exist.
	 * @throws IllegalArgumentException
	 */
	public Plane getPlaneByID(Long planeID) throws IllegalArgumentException {
		return planeRepository.getPlaneByID(planeID);
	}
	
	/**
	 * The method updates availability of the given plane.
	 * adds (+1) or (-1) to the value of available_quantity, according to the state of isDecrement.
	 * @param planeID - the ID of the plane which is modified
	 * @param isDecrement - True -> (-1), False -> (+1)
	 * @throws IllegalArgumentException
	 */
	public void updateAvailableQuantity(Long planeID, boolean isDecrement) throws IllegalArgumentException {
		planeRepository.updateAvailableQuantity(planeID, isDecrement);
	}

}
