package com.example.SwipeFlight.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The class consists methods for casting different types.
 */
public class CastUtils {
	
	/**
	 * The method casts a collection of objects to a list of a specific type.
	 * For example:
	 * castList(Passenger.class, List<?> rawPassengerList) -> List<Passenger>
	 * @param <T>
	 * @param desiredClass
	 * @param rawCollection
	 * @return list of desiredClass objects
	 * @throws IllegalArgumentException
	 */
	public static <T> List<T> castList(Class<? extends T> desiredClass, Collection<?> rawCollection)
										throws IllegalArgumentException {
	    List<T> result = new ArrayList<>(rawCollection.size());
	    for (Object o : rawCollection) {
	        try {
	            result.add(desiredClass.cast(o));
	        } catch (ClassCastException e) {
	        	System.err.println("\n*** Error ***\nClass: CastUtils\t Method: castList "
						+ "\nDetails: " + e.getMessage());
	        	throw new IllegalArgumentException("Casting failure.", e);
	        }
	    }
	    return result;
	}

}
