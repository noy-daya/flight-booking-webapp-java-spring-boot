package com.example.SwipeFlight.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SwipeFlight.auth.AuthService;
import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.auth.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The class displays registration views and handles the process of registration form.
 */
@Controller
public class RegistrationController {

	// dependency injections
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    
    /**
     * The method is responsible for displaying "registration" page
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/registration")
	public String viewRegisterPage(ModelMap model, HttpServletRequest request)
	{
		model.addAttribute("user", new User()); // initialize User object for the form
		return "registration";
	}
	
	/**
	 * The method is responsible for processing "login" form
	 * 		- if there are rejected values -> return to the form with the errors.
	 * 		- otherwise- fields are legal -> insert user, make him the logged in user
	 * @param user- the values entered by the user
	 * @param model- to pass attributes to the view.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @param request
	 * @return the URL of the page which should be rendered.
	 */
	@PostMapping("/registration/ProcessUserRegistration")
	public String processUserRegistration(@ModelAttribute("user") User user, ModelMap model,
											BindingResult result,
											HttpServletRequest request)
	{
		result = userService.validateUserRegistration(user, result);
		
    	// if there are any rejected values or general rejection -> not valid, return to the form
        if (result.hasErrors())
        	return "registration";
        
        // there are no rejected values (=valid) -> insert user, make him the logged in user
		userService.insertUser(user);
		authService.manageLoginSuccess(user.getUserName(), request);
		return "redirect:/home";
	}
}
