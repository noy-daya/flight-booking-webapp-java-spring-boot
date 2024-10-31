package com.example.SwipeFlight.entities.city;

import java.io.Serializable;

import com.example.SwipeFlight.entities.country.Country;

/**
 * The class represents Entity layer of City (the properties match with City table fields)
 */
public class City implements Serializable {
	
    private Long id;
    private String name;
    private String description;
	private String imgUrl;
    private Country country;
    
    /* empty constructor */
	public City() { }
    
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
	
	/* The method returns value of description */
    public String getDescription() {
		return description;
	}

    /* The method updates value of description */
	public void setDescription(String description) {
		this.description = description;
	}

	/* The method returns value of imgURL */
	public String getImgUrl() {
		return imgUrl;
	}

	/* The method updates value of imgUrl */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/* The method returns value of country */
	public Country getCountry() {
		return country;
	}

	/* The method updates value of country */
	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", description=" + description + ", imgUrl=" + imgUrl
				+ ", country=" + country + "]";
	}

}

