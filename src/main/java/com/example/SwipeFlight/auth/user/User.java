package com.example.SwipeFlight.auth.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * The class represents Entity layer of User (the properties match with User table fields)
 * The class implements Serializable interface
 * 		(because session stores User object, so object conversion to byte array for storage is necessary)
 */
@SuppressWarnings("serial")
public class User implements Serializable
{
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Timestamp lastSeen;
    private boolean isAdmin;
    
    /* empty constructor */
    public User() {}
    
    /* Constructor */
    public User(String userName, String email, String password, boolean isAdmin)
    {
    	this.userName = userName;
    	this.email = email;
    	this.password = password;
    	this.isAdmin = isAdmin;
    }
    
    /* The method returns value of id */
    public Long getId() {
		return id;
	}

    /* The method updates value of id */
	public void setId(Long id) {
		this.id = id;
	}

    /* The method returns value of userName */
	public String getUserName() {
		return userName;
	}

	/* The method updates value of userName */
	public void setUserName(String userName) {
		this.userName = userName;
	}

    /* The method returns value of email */
	public String getEmail() {
		return email;
	}

	/* The method updates value of email */
	public void setEmail(String email) {
		this.email = email;
	}

    /* The method returns value of password */
	public String getPassword() {
		return password;
	}

	/* The method updates value of password */
	public void setPassword(String password) {
		this.password = password;
	}
	
    /* The method returns value of lastSeen */
    public java.sql.Timestamp getLastSeen() {
		return lastSeen;
	}

    /* The method updates value of lastSeen */
	public void setLastSeen(Timestamp lastSeen) {
		this.lastSeen = lastSeen;
	}

    /* The method returns value of isAdmin */
	public boolean isAdmin() {
		return isAdmin;
	}

	/* The method updates value of isAdmin */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	/*
	 * The method is necessary to retrieve user's authorities (in class UserDetailsServiceImpl),
	 * during authentication process.
	 * In the system, there are two types of authenticated users:
	 * 		1. admin -> will receive authority "ADMIN"
	 * 		2. user -> will receive authority "USER"
	 */
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (isAdmin)
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        else
            authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", lastSeen=" + lastSeen + ", isAdmin=" + isAdmin + "]";
	}
}
