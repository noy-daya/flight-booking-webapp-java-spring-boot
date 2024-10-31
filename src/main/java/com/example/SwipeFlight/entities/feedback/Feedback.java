package com.example.SwipeFlight.entities.feedback;

/**
 * The class represents Entity layer of Feedback (the properties match with Feedback table fields)
 */
public class Feedback
{
    private int cleaningRating;
    private int convenienceRating;
    private int serviceRating;

    /* The method returns value of cleaningRating */
    public int getCleaningRating() {
        return cleaningRating;
    }

    /* The method updates value of cleaningRating */
    public void setCleaningRating(int cleaningRating) {
        this.cleaningRating = cleaningRating;
    }

    /* The method returns value of convenienceRating */
    public int getConvenienceRating() {
        return convenienceRating;
    }

    /* The method updates value of convenienceRating */
    public void setConvenienceRating(int convenienceRating) {
        this.convenienceRating = convenienceRating;
    }

    /* The method returns value of serviceRating */
    public int getServiceRating() {
        return serviceRating;
    }

    /* The method updates value of serviceRating */
    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }
}
