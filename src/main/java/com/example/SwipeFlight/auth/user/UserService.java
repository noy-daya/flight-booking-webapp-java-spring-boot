package com.example.SwipeFlight.auth.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * The class represents Service layer of User.
 * (consists validation checks, interacts with Repository layer)
 */
@Service
public class UserService
{

	@Autowired // dependency injection
	private UserRepository userRepository;

	/**
	 * The method inserts a new User into the database. (password will be inserted encoded)
	 * @param user
	 * @throws IllegalArgumentException
	 */
	public void insertUser(User user) throws IllegalArgumentException {
		userRepository.insertUser(user);
	}

    /**
     * The method retrieves a user by its userName.
     * @param userName
     * @return user object, or null if no rows found.
     * @throws IllegalArgumentException
     */
	public User getUserByUserName(String userName) throws IllegalArgumentException {
		return userRepository.getUserByUserName(userName);
	}
	
	/**
	 * The method updates user's last seen field.
	 * @param userName
	 * @throws IllegalArgumentException
	 */
	public void updateLastSeen(String userName) throws IllegalArgumentException
	{
		userRepository.updateLastSeen(userName);
	}
	
	/**
	 * The method checks validity of registration form fields.
	 * @param user- the values entered by the user
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validateUserRegistration(User user, BindingResult result) {
	    String userName = user.getUserName();
	    String email = user.getEmail();
	    String password = user.getPassword();
	    
		// user name
		if (userName.isEmpty())
			result.rejectValue("userName", "error.user", "Field is required.");
		if(userRepository.getUserByUserName(userName) != null)
			result.rejectValue("userName", "error.user", "User name is already taken.");
		
		// email
		if(userRepository.getUserByEmail(email) != null)
			result.rejectValue("email", "error.user", "Email already exists in system.");
		if (email.isEmpty())
			result.rejectValue("email", "error.user", "Field is required.");
		else
		{
			// pattern validity
			String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(email);
			if (!matcher.matches())
				result.rejectValue("email", "error.email", "Illegal address.");
		}
		
		// password
		if (password.isEmpty())
			result.rejectValue("password", "error.user", "Field is required.");
		
		return result;
    }
	
	/**
	 * The method checks validity of login form fields.
	 * @param user- the values entered by the user (admin)
	 * @param result- BindingResult to attach errors to (if there are any)
	 * @return result- after adding rejected values
	 */
	public BindingResult validateUserLogin(User user, BindingResult result) {
	    String userName = user.getUserName();
	    String password = user.getPassword();
	    
		if (userName.isEmpty())
			result.rejectValue("userName", "error.user", "Field is required.");
		if (password.isEmpty())
			result.rejectValue("password", "error.user", "Field is required.");
		if (userRepository.getUserByUsernameAndPassword(userName, password) == null)
			result.reject("error.object", "User name or password is incorrect.");
		
        return result;
    }
}
