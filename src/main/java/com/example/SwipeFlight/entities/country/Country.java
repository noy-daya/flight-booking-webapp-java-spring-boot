package com.example.SwipeFlight.entities.country;

import java.io.Serializable;

/**
 * The class represents Entity layer of Country (the properties match with City table fields)
 */
public class Country implements Serializable{
	
    private Long id;
    private String name;
    
    /* empty constructor */
	public Country() {}
	
	/* The method returns value of id */
    public Long getId() {
		return id;
	}

    /* The method updates value of id */
	public void setId(Long id) {
		this.id = id;
	}

	/* The method returns value of name */
	public String getName() {
		return name;
	}

	/* The method updates value of name */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}
}
