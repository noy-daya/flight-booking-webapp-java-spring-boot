package com.example.SwipeFlight.entities.seat;

import java.util.List;

import com.example.SwipeFlight.entities.flight.Flight;

/**
 * The class is a helper, allowing us to bind a List object in Thymeleaf.
 * Explanation:
 * allowing us to retrieve a list of List<Seat> objects submitted from the view to the controller,
 * in which every Seat object was modified by the user.
 * 
 * In other words: it is a mapping between a flight and its seatMatrix.
 */
public class FlightDTO {

    private Flight flight;
    private List<List<Seat>> seatMatrix;

    public FlightDTO(Flight flight, List<List<Seat>> seatMatrix) {
        this.flight = flight;
        this.seatMatrix = seatMatrix;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<List<Seat>> getSeatMatrix() {
        return seatMatrix;
    }

    public void setSeatMatrix(List<List<Seat>> seatMatrix) {
        this.seatMatrix = seatMatrix;
    }
}
