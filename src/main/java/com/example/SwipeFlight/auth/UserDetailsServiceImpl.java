package com.example.SwipeFlight.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.auth.user.UserService;

/**
 * The class is a custom implementation of UserDetailsService interface (of Spring Security)
 * It allows access to UserDetails of the user.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired // dependency injection
    private UserService userService; // to access user's details from database 

	/**
	 * The method returns the corresponding UserDetails for the given user name,
	 * 		based on the entry of User table in database. (or null- if user name does not exist)
	 * (implements the method of UserDetailsService interface)
	 */
    @Override
    public UserDetails loadUserByUsername(String username)
    {
        User user = userService.getUserByUserName(username); // get the User entry
        // user name exists
        if (user != null)
        {
        	UserDetails ud = new org.springframework.security.core.userdetails
            		.User(user.getUserName(), user.getPassword(), user.getAuthorities());
        	return ud;
        }
        // user name does not exist
        return null;
    }
}
