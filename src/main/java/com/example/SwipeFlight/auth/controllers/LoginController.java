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
 * The class displays login views and handles the process of login form.
 */
@Controller
public class LoginController {

	// dependency injections
    @Autowired
    private UserService userService; // Service for user logic.
    @Autowired
    private AuthService authService; // Service for authentication logic.	
    
    /**
     * The method is responsible for displaying "login" page
     * @param model- to pass attributes to the view.
     * @param request
     * @return the URL of the page which should be rendered.
     */
	@GetMapping("/login")
	public String viewLoginPage(ModelMap model, HttpServletRequest request)
	{
		model.addAttribute("user", new User()); // initialize User object for the form
		return "login";
	}
	
	/**
	 * The method is responsible for processing "login" form
	 * 		- if there are rejected values -> return to the form with the errors.
	 * 		- otherwise- fields are legal -> make him the logged in user
	 * @param user- the values entered by the user
	 * @param model- to pass attributes to the view.
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @param request
	 * @return the URL of the page which should be rendered.
	 */
    @PostMapping("/login/ProcessUserLogin")
    public String processUserLogin(@ModelAttribute("user") User user, ModelMap model,
                                   BindingResult result,
                                   HttpServletRequest request) {
    	
    	result = userService.validateUserLogin(user, result);
    	
    	// if there are any rejected values or general rejection -> not valid, return to the form
        if (result.hasErrors())
        	return "login";
        
        // if there are no rejected values (=valid), and the user is authenticated -> make him the logged in user
        if (authService.authenticateUser(user.getUserName(), user.getPassword()))
        {
        	authService.manageLoginSuccess(user.getUserName(), request);
            return "redirect:/home";
        }

        // there are no rejected values (=valid), but the user is not authenticated
        authService.manageLoginFailureOrLogout(request);

        return "login";
    }
    
}
