package com.example.SwipeFlight.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import com.example.SwipeFlight.auth.user.UserService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class is a custom servlet filter (an object that performs filtering tasks on either the request to a resource).
 * The filter adds attribute named "sessionUser" to the attributes of every request from the user,
 * as long as the user is authenticated.
 */
@Component
public class SessionUserFilter implements Filter {

    @Autowired // dependency injection
    private UserService userService; // to receive the user's details from database

    /**
     * The method contains the main logic of the filter and is called for each request that goes through the filter. 
     * (implements the method of Filter interface)
     * Makes sure that the user in the corresponding session is authenticated (=not anonymous),
     * and as a result adds "sessionUser" to the attributes of every request from the user.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // get the existing session of the request, or a new one
        HttpSession session = httpRequest.getSession(true);
        // retrieve SPRING_SECURITY_CONTEXT from session, which contains security information about the current authentication.
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null)
        {
            Authentication authentication = securityContext.getAuthentication();
            // make sure that the user is not anonymous
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                // add "sessionUser" to the request attribute
            	// (getting user's full details from User table in database)
                request.setAttribute("sessionUser", userService.getUserByUserName(authentication.getName()));
            }
        }
        try {
        	// pass the request and response to the next filter in the chain.
        	// next filter can be another filter for the request, or the final target resource.
			chain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("*** Error *** " + e.getMessage());
			//System.err.println("*** Error -- SessionUserFilter -- IO error: " + e.getMessage());
		} catch (ServletException e) {
			throw new IllegalArgumentException("*** Error *** " + e.getMessage());
		} catch (Exception e) {
		throw new IllegalArgumentException("*** Error *** " + e.getMessage());
	}
    }
}
