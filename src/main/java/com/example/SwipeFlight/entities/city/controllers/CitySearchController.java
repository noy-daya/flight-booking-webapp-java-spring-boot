package com.example.SwipeFlight.entities.city.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SwipeFlight.entities.city.City;
import com.example.SwipeFlight.entities.city.CityService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The class displays views related to city searching (in searching bar).
 */
@Controller
public class CitySearchController {

	@Autowired // dependency injection
    private CityService cityService;
	
    /**
     * The method is responsible for displaying "home" page
     * @param selectedCityFromSuggestions- the keyboard as received from searching bar (defined as not required)
     * 				- if null 	-> all the cities will be shown by default
     * 				- otherwise -> display citiesList (each member is City Object)
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/home{selectedCityFromSuggestions}")
	public String viewHomePage(@RequestParam(name = "selectedCityFromSuggestions", required = false)
								String selectedCityFromSuggestions,
	                           ModelMap model, HttpServletRequest request) {

	    if (selectedCityFromSuggestions == null) // page will consist all the cities by default
	    	selectedCityFromSuggestions = "";
	    List<City> citiesList = cityService.getCitiesByKeyboard(selectedCityFromSuggestions);
	    model.addAttribute("citiesList", citiesList);
	    model.addAttribute("citySearch", selectedCityFromSuggestions);

	    return "home";
	}
	
	/**
	 * The method displays list (of strings) of the suggestions for matching results of auto complete.
	 * (the return value of this method = response body of the request)
	 * @param request
	 * @return list of matching results (each element is a string)
	 */
    @GetMapping("/citySearch/fetchSuggestionsForCitySearch")
    @ResponseBody
    public List<String> fetchSuggestionsForCitySearch(HttpServletRequest request) {
    	// term = the string that the user entered
        return cityService.getCityNamesByKeyboard(request.getParameter("term"));
    }
}
