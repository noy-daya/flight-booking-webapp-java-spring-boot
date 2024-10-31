package com.example.SwipeFlight.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.auth.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The class represents authentication service:
 * It consists:
 * - function which checks if the user is authenticated (assuming that at this point, the syntax of the form fields is valid)
 * - functions which manages login success and failure.
 */
@Service
public class AuthService {

	// dependency injections
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImp; // to access UserDetails of the user
    @Autowired
    private UserService userService;
    
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // because passwords are stored encoded in database

	/**
	 * The function checks if the given parameters belong to an existing user in the system.
	 * @param username- as provided from the form (assuming its syntax is valid)
	 * @param password- as provided from the form (assuming its syntax is valid)
	 */
	public boolean authenticateUser(String userName, String password)
	{
		// get user details: user name, correct password (encrypted), authorities list.
		UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(userName);

		// check if the provided password matches the encoded password from user details
		if (userDetails == null)
			return false;
	
		// check if the provided password matches the encoded password from user details
		return passwordEncoder.matches(password, userDetails.getPassword());
	}
	
	/**
	 * The method manages login success, following the steps:
	 * 		1. get User entry for the given userName (which was already authenticated)
	 * 		2. update context
	 * 		3. save the updated context in session (by modifying SPRING_SECURITY_CONTEXT session attribute)
	 * 		4. update user's last seen field
	 * @param userName
	 * @param request
	 */
	public void manageLoginSuccess(String userName, HttpServletRequest request)
	{
		try {
			// get User entry for the given userName (which was already authenticated)
			User user = userService.getUserByUserName(userName);
			Authentication authentication = new UsernamePasswordAuthenticationToken
					(user.getUserName(), user.getPassword(), user.getAuthorities());
			// update context
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        // get the corresponding session for the request, and save context in session
	        HttpSession session = request.getSession(true);
	        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
	        // modify last seen
	        userService.updateLastSeen(userName);
		} catch (Exception e) {
			System.err.println("*** Error -- AuthService -- login success error:\t user " + userName + "\t" + e.getMessage());
		}
	}
	
	/**
	 * The method manages login failure or logout.
	 * It gets the corresponding session for the given request, and invalidates it.
	 * (as a result: the session and all the attributes under this session ID are removed from session, including sessionContext)
	 * @param request
	 */
	public void manageLoginFailureOrLogout(HttpServletRequest request)
	{
		try {
			request.getSession().invalidate();
		} catch (Exception e) {
			System.err.println("*** Error -- AuthService -- login failure or logout error: " + e.getMessage());
		}
	}
}
