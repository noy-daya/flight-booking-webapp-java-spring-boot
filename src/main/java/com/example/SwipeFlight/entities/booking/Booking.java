package com.example.SwipeFlight.entities.booking;

import java.sql.Timestamp;

import com.example.SwipeFlight.entities.route.Route;

/**
 * The class represents Entity layer of Booking (the properties match with Booking table fields)
 */
public class Booking
{
	private Long id;
	private Long userID;
	private Route route;
	private int numOfTickets;
	private double totalPrice;
	private Timestamp bookingDate;
	private Timestamp lastModifyDate;
	private boolean isCanceled;
	
	/* The method returns value of id */
	public Long getId() {
		return id;
	}
	
	/* The method updates value of id */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* The method returns value of userID */
	public Long getUserID() {
		return userID;
	}
	
	/* The method updates value of userID */
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
	/* The method returns value of route */
	public Route getRoute() {
		return route;
	}
	
	/* The method updates value of route */
	public void setRoute(Route route) {
		this.route = route;
	}
	
	/* The method returns value of numOfTickets */
	public int getNumOfTickets() {
		return numOfTickets;
	}
	
	/* The method updates value of numOfTickets */
	public void setNumOfTickets(int numOfTickets) {
		this.numOfTickets = numOfTickets;
	}
	
	/* The method returns value of totalPrice */
	public double getTotalPrice() {
		return totalPrice;
	}
	
	/* The method updates value of totalPrice */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	/* The method returns value of bookingDate */
	public Timestamp getBookingDate() {
		return bookingDate;
	}
	
	/* The method updates value of bookingDate */
	public void setBookingDate(Timestamp bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	/* The method returns value of lastModifyDate */
	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}
	
	/* The method updates value of lastModifyDate */
	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
	/* The method returns value of isCanceled */
	public boolean isCanceled() {
		return isCanceled;
	}
	
	/* The method updates value of isCanceled */
	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}
	
	@Override
	public String toString() {
		return "Booking [id=" + id + ", userID=" + userID + ", route=" + route + ", numOfTickets=" + numOfTickets
				+ ", totalPrice=" + totalPrice + ", bookingDate=" + bookingDate + ", lastModifyDate=" + lastModifyDate
				+ ", isCanceled=" + isCanceled + "]";
	}

}
