package com.example.SwipeFlight.entities.plane;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * The class displays views related to פךשמקד.
 */
@Controller
public class PlaneController {
	
	@Autowired // dependency injection
	PlaneService planeService;
	
	/**
	 * The method displays planes list.
	 * (the return value of this method = response body of the request)
	 * @return planes list, in JSON format.
	 */
    @GetMapping(value="/plane/FetchPlanes", produces="application/json")
    @ResponseBody
    public List<Plane> fetchPlanes()
    {
    	return planeService.getAllPlanes();
    }

}
