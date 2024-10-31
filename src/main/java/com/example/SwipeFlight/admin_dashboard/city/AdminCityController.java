package com.example.SwipeFlight.admin_dashboard.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SwipeFlight.entities.city.City;
import com.example.SwipeFlight.entities.city.CityService;
import com.example.SwipeFlight.entities.country.CountryService;
import com.example.SwipeFlight.server.utils.FileUploadService;

/**
 * The class displays views related to city modification.
 */
@Controller
public class AdminCityController {

	// dependency injections
    @Autowired
    private CityService cityService;
    @Autowired
    private FileUploadService flightUploadService;
	@Autowired
    private CountryService countryService;
	
    /**
     * The method is responsible for displaying "city" page
     * @param cityID- the ID of the city to display
     * @param model- to pass attributes to the view.
     * @return the URL of the page which should be rendered.
     */
	@PostMapping("/adminCity{cityID}")
	public String viewAdminCityPage(@RequestParam("cityID") Long cityID,
									ModelMap model, RedirectAttributes redirectAttributes)
	{
        City city = cityService.getCityByID(cityID); // retrieve the City object
        model.addAttribute("city", city);
		return "adminCity";
	}
	
    /**
     * The method is responsible for processing city modification form.
     * @param fileInput- file selected by the user
     * @param city- the modified fields of the city
     * @param model- to pass attributes to the view.
     * @param result- binding result for validation errors
     * @param redirectAttributes- attributes for redirect scenarios
     * @return the URL of the page which should be rendered. (accomplish Post-Redirect-Get pattern)
     */
    @PostMapping("/adminCity/ProcessCityModification")
    public String processCityModification(@RequestParam("fileInput") MultipartFile fileInput, City city,
                                          ModelMap model, RedirectAttributes redirectAttributes, BindingResult result) {
        String filePath = "";

        // if user selected a file from a location in their device
        if (fileInput != null) {
            // check legality, upload file to relative path of the project
            filePath = flightUploadService.fileUpload(fileInput, "City", result);
        }
        
    	// for Post-Redirect-Get pattern
        // 		add errors and the object the errors relate to,
    	// 		to redirect attributes so they will be passed even during redirect.
        redirectAttributes.addFlashAttribute("result", result);
        redirectAttributes.addFlashAttribute("city", city);

        // failure:
        if (result.hasErrors()) {
            return "redirect:/adminCity/cityRedirect?cityID=" + city.getId(); // Post-Redirect-Get pattern
        }
        // success:
        cityService.updateCity(city, filePath);
        redirectAttributes.addFlashAttribute("success_message", "City was modified successfully.");

        return "redirect:/adminCity/cityRedirect?cityID=" + city.getId(); // Post-Redirect-Get pattern
    }
    
    /**
     * The method is added due to Post-Redirect-Get pattern (to avoid form re-submission)
     * @param cityID- the ID of the city to display
     * @param model- to pass attributes to the view.
     * @param redirectAttributes - to retrieve BindingResult saved during POST
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/adminCity/cityRedirect{cityID}")
	public String viewCityPageRedirect(@RequestParam("cityID") Long cityID,
								Model model, RedirectAttributes redirectAttributes)
	{
        if (model.asMap().containsKey("result") && model.asMap().containsKey("city"))
        {
        	// result
        	BindingResult result = (BindingResult)model.asMap().get("result");
            model.addAttribute("org.springframework.validation.BindingResult.city",
                    model.asMap().get("result"));
            
            // city
            City city = (City)model.asMap().get("city");
            city.setCountry(countryService.getCountryByID(cityService.getCountryIdOfCityByID(cityID)));
            city.setImgUrl(cityService.getCityByID(cityID).getImgUrl());
            model.addAttribute("city", city);
        }
        // continue actions of viewCityPage request
        // --------------------------------------------
        else
        {
	        City city = cityService.getCityByID(cityID); // retrieve the City object
	        model.addAttribute("city", city);
        }
        
		return "adminCity";
	}
}