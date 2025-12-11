package com.example.sportsscheduling.scheduling;

/**
 * The type Team.
 */
public class Team {
    private String name;
    private String preferredVenue;
    private String preferredTime;

    /**
     * Instantiates a new Team.
     *
     * @param name           the name
     * @param preferredVenue the preferred venue
     * @param preferredTime  the preferred time
     */
    public Team(String name, String preferredVenue, String preferredTime) {
        this.name = name;
        this.preferredVenue = preferredVenue;
        this.preferredTime = preferredTime;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets preferred venue.
     *
     * @return the preferred venue
     */
    public String getPreferredVenue() {
        return preferredVenue;
    }

    /**
     * Gets preferred time.
     *
     * @return the preferred time
     */
    public String getPreferredTime() {
        return preferredTime;
    }

    /**
     * Sets preferred venue.
     *
     * @param preferredVenue the preferred venue
     */
    public void setPreferredVenue(String preferredVenue) {
        this.preferredVenue = preferredVenue;
    }

    /**
     * Sets preferred time.
     *
     * @param preferredTime the preferred time
     */
    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String toString(){
        return "name: " + name + ",venue: " + preferredVenue + ",time: " + preferredTime;
    }
}
