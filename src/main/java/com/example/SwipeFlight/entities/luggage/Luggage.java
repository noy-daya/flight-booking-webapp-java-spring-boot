package com.example.SwipeFlight.entities.luggage;

/**
 * The class represents Entity layer of Luggage (the properties match with Luggage table fields)
 */
public class Luggage {
	
	private Long id;
	private String description;
	private int weightLimit;
	private double price;
	
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
	
	/* The method returns value of weightLimit */
	public int getWeightLimit() {
		return weightLimit;
	}
	
	/* The method updates value of weightLimit */
	public void setWeightLimit(int weightLimit) {
		this.weightLimit = weightLimit;
	}
	
	/* The method returns value of price */
	public double getPrice() {
		return price;
	}
	
	/* The method updates value of price */
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Luggage [id=" + id + ", description=" + description + ", weightLimit=" + weightLimit + ", price="
				+ price + "]";
	}

}
