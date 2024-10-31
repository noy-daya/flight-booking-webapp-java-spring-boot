package com.example.SwipeFlight.entities.flight_status;

/**
 * The class represents Entity layer of FlightStatus (the properties match with Flight_Status table fields)
 */
public class FlightStatus {

	private Long id;
	private String description;
	
	/* The method returns value of id */
	public Long getId() {
		return id;
	}
	
	/* The method updates value of id */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* The method returns value of description */
	public String getDescription() {
		return description;
	}
	
	/* The method updates value of description */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
