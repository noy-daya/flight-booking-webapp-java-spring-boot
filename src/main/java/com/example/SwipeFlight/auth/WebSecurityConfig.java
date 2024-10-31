package com.example.SwipeFlight.auth;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The class handles security configuration, and defines the permitted web pages for users.
 * Reminder- Types of users in the web-app are:
 * - Not authenticated
 * - Authenticated:
 * 		- Has "ADMIN" authority
 * 		- Has "USER" authority
 */
@Configuration
@EnableWebSecurity // Enable Spring Security’s web security
public class WebSecurityConfig
{
    /**
     * The method customizes the security configuration for HTTP requests sent by users.
     * It defines which URL paths should be secured and which should not.
     * (It is a bean which is managed by Spring container)
     * @param http- an object which allows configuring web based security for specific http requests.
     * @return http after adding security configurations: http.build()
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)
    {
		try {
			http
				// different types of requests are specified along with their required authorities.
				.authorizeHttpRequests((requests) -> requests
					// only admin is allowed
			    	.requestMatchers("/admin**").hasAuthority("ADMIN")
			    	// only authenticated users are allowed
			    	.requestMatchers("/booking**").authenticated()
			    	.requestMatchers("/user**").hasAuthority("USER")
			    	// no restrictions, any user (authenticated or not) is allowed
			    	.anyRequest().permitAll()
				)
				// specify that the login page is a custom form "/login" page.
				// notation: When a user successfully logs in, they are redirected to home page.
				// 		it is implemented in AuthController class.
			    .formLogin(formLogin -> formLogin
			        .loginPage("/login")
			    )
			    // specify a custom access denied handler
			    .exceptionHandling(exceptionHandling -> exceptionHandling
			    	.accessDeniedHandler(accessDeniedHandler())
			    );
			return http.build();
		} catch (Exception e) {
			System.err.println("*** Error -- WebSecurityConfig -- security configuration error: " + e.getMessage());
			return null;
		}
	}
    
    /**
     * The method provides a custom implementation of the AccessDeniedHandler interface:
     * It Defines the actions which are taken when a user attempts to access a secured resource. (invoked each time)
     * It is a bean which is managed by Spring container.
     * @return anonymous inner class (which implements the AccessDeniedHandler interface)
     */
    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
        	// The method is called when a user attempts to access a secured resource
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            {
                try {
	                // user will be redirected to page "accessDenied.jsp"
					response.sendRedirect(request.getContextPath() + "/accessDenied");
				} catch (IOException error) {
			    	System.err.println("\n*** Error ***\nClass: WebSecurityConfig "
							+ "\nDetails: " + e.getMessage());
				}
            }
        };
    }
}
