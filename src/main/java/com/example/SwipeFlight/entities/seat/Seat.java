package com.example.SwipeFlight.entities.seat;

/*
 * The class represents a Seat in an airplane.
 */
public class Seat
{
    private int row;
    private char seat;
    private boolean available;

    /* Constructor */
    public Seat(int row, char seat, boolean available) {
        this.row = row;
        this.seat = seat;
        this.available = available;
    }

    /* The method returns value of row */
    public int getRow() {
        return row;
    }

    /* The method updates value of row */
    public void setRow(int row) {
        this.row = row;
    }

    /* The method returns value of seat */
    public char getSeat() {
        return seat;
    }

    /* The method updates value of seat */
    public void setSeat(char seat) {
        this.seat = seat;
    }

    /* The method returns value of available */
	public boolean isAvailable() {
		return available;
	}

	/* The method updates value of available */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Seat [row=" + row + ", seat=" + seat + ", available=" + available + "]";
	}
    
    
}