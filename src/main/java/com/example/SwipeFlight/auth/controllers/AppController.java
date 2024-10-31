package com.example.SwipeFlight.auth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for general views
 */
@Controller
public class AppController {

	@GetMapping("/template")
	public String viewTemplatePage() {
		return "template";
	}
	
	@GetMapping("/aboutUs")
	public String viewAboutUsPage()
	{
		return "aboutUs";
	}
	
	@GetMapping("/error")
	public String viewErrorPage()
	{
		return "error";
	}
}
