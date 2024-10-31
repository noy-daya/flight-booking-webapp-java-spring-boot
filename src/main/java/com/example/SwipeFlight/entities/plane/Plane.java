package com.example.SwipeFlight.entities.plane;

import java.io.Serializable;

/**
 * The class represents Entity layer of Plane (the properties match with Plane table fields)
  * The class implements Serializable interface
 * 		(because session stores some of the fields, so object conversion to byte array for storage is necessary)
 */
public class Plane implements Serializable {
	
    private Long id;
    private String description;
    private int numOfRows;
	private int numOfSeatsPerRow;
	private int totalQuantity;
	private int availableQuantity;

	/* empty constructor */
	public Plane() {}
    
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

	/* The method returns value of numOfRows */
	public int getNumOfRows() {
		return numOfRows;
	}

	/* The method updates value of numOfRows */
	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	/* The method returns value of numOfSeatsPerRow */
	public int getNumOfSeatsPerRow() {
		return numOfSeatsPerRow;
	}

	/* The method updates value of numOfSeatsPerRow */
	public void setNumOfSeatsPerRow(int numOfSeatsPerRow) {
		this.numOfSeatsPerRow = numOfSeatsPerRow;
	}
	
	/* The method returns value of totalQuantity */
	public int getTotalQuantity() {
		return totalQuantity;
	}

	/* The method updates value of totalQuantity */
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	/* The method returns value of availableQuantity */
	public int getAvailableQuantity() {
		return availableQuantity;
	}

	/* The method updates value of availableQuantity */
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	@Override
	public String toString() {
		return "Plane [id=" + id + ", description=" + description + ", numOfRows=" + numOfRows + ", numOfSeatsPerRow="
				+ numOfSeatsPerRow + ", totalQuantity=" + totalQuantity + ", availableQuantity=" + availableQuantity
				+ "]";
	}

}
