package com.example.SwipeFlight.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SwipeFlight.auth.AuthService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The class displays logout views.
 */
@Controller
public class LogoutController {

    @Autowired // dependency injection
    private AuthService authService;
    
    /**
     * The method is responsible for processing logout.
     * @param request
     * @return "redirect:/login": indicating that the user should be redirected to "login" view.
     */
	@PostMapping("/logout/ProcessUserLogout")
	public String processUserLogout(HttpServletRequest request)
	{
		authService.manageLoginFailureOrLogout(request);
		return "redirect:/login";
	}
}
