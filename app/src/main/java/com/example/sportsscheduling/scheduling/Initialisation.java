package com.example.sportsscheduling.scheduling;

import java.util.*;

/**
 * The type Initialisation.
 */
public class Initialisation {
    private static Initialisation instance;
    private List<Team> teams;
    private List<Venue> venues;
    private List<String> times;
    /**
     * The Time slot.
     */
    List<String> timeSlot = new ArrayList<>();
    private int totalDays;
    private int maxDaily;
    private int maxPerTime;
    private String datePreference;
    private int seperationDay;
    private double proportion;
    private boolean isGroupConstraint;
    private boolean isDateConstraint;
    private boolean isSeperationConstraint;
    private boolean isRestDifferenceConstraint;
    private boolean isDailyConstraint;
    private boolean isPreferenceConstraint;
    private Random random = new Random(1234567);

    // Use the Singleton pattern to ensure the only instance of initialisation
    private Initialisation(List<Team> teams, List<Venue> venues, List<String> times, int totalDays, int maxDaily, int maxPerTime, String datePreference, int seperationDay) {
        this.teams = teams;
        this.venues = venues;
        this.times = times;
        this.totalDays = totalDays;
        this.maxDaily = maxDaily;
        this.maxPerTime = maxPerTime;
        this.datePreference = datePreference;
        this.seperationDay = seperationDay;
    }

    /**
     * Gets instance.
     *
     * @param teams          the teams
     * @param venues         the venues
     * @param times          the times
     * @param totalDays      the total days
     * @param maxDaily       the max daily
     * @param maxPerTime     the max per time
     * @param datePreference the date preference
     * @param seperationDay  the seperation day
     * @return the instance
     */
// Get the instance of initialisation with parameters
    public static Initialisation getInstance(List<Team> teams, List<Venue> venues, List<String> times, int totalDays, int maxDaily, int maxPerTime, String datePreference, int seperationDay) {
        instance = new Initialisation(teams, venues, times, totalDays, maxDaily, maxPerTime, datePreference, seperationDay);

        return instance;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
// Get the instance of initialisation without parameters
    public static Initialisation getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Initialisation has not been initialized. Call getInstance() with parameters first.");
        }
        return instance;
    }

    /**
     * Generate initial solution list.
     *
     * @return the list
     */
    public List<Match> generateInitialSolution() {
        List<Match> solution = new ArrayList<>();
        List<String> teamSchedule = new ArrayList<>();  // record the day and the time that each team will play
        int currentDay;
        Venue currentVenue = null;
        String currentTime;

        for (int i = 1; i <= totalDays; i++) {
            for (int k = 0; k < venues.size(); k++) {
                for (int j = 0; j < times.size(); j++) {
                    String slot = i + "," + venues.get(k).getName() + "," + times.get(j);
                    timeSlot.add(slot);
                }
            }
        }

        int numAssigned = 0;
        int numTotal = timeSlot.size();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team team1 = teams.get(i);
                Team team2 = teams.get(j);

                int rdm = random.nextInt(timeSlot.size());
                String[] slotArr = timeSlot.get(rdm).split(",");
                currentDay = Integer.parseInt(slotArr[0]);
                currentTime = slotArr[2];

                // Avoid having one team play multiple games on the same day at the same time
                String team_1_Schedule = team1.getName() + "," + currentDay + "," + currentTime;
                String team_2_Schedule = team2.getName() + "," + currentDay + "," + currentTime;
                while (teamSchedule.contains(team_1_Schedule) || teamSchedule.contains(team_2_Schedule)) {
                    rdm = random.nextInt(timeSlot.size());
                    slotArr = timeSlot.get(rdm).split(",");
                    currentDay = Integer.parseInt(slotArr[0]);
                    currentTime = slotArr[2];
                    team_1_Schedule = team1.getName() + "," + currentDay + "," + currentTime;
                    team_2_Schedule = team2.getName() + "," + currentDay + "," + currentTime;
                }
                teamSchedule.add(team_1_Schedule);
                teamSchedule.add(team_2_Schedule);

                for (Venue v : venues) {
                    if (v.getName().equals(slotArr[1])) {
                        currentVenue = v;
                    }
                }

                solution.add(new Match(team1, team2, currentVenue, currentTime, currentDay));
                timeSlot.remove(rdm);
                numAssigned++;
            }
        }
        proportion = (double) numAssigned / numTotal;
        return solution;
    }

    /**
     * Gets max daily.
     *
     * @return the max daily
     */
    public int getMaxDaily() {
        return maxDaily;
    }

    /**
     * Sets max daily.
     *
     * @param maxDaily the max daily
     */
    public void setMaxDaily(int maxDaily) {
        this.maxDaily = maxDaily;
    }

    /**
     * Gets time slot.
     *
     * @return the time slot
     */
    public List<String> getTimeSlot() {
        return timeSlot;
    }

    /**
     * Gets venues.
     *
     * @return the venues
     */
    public List<Venue> getVenues() {
        return venues;
    }

    /**
     * Gets max per time.
     *
     * @return the max per time
     */
    public int getMaxPerTime() {
        return maxPerTime;
    }

    /**
     * Sets max per time.
     *
     * @param maxPerTime the max per time
     */
    public void setMaxPerTime(int maxPerTime) {
        this.maxPerTime = maxPerTime;
    }

    /**
     * Gets date preference.
     *
     * @return the date preference
     */
    public String getDatePreference() {
        return datePreference;
    }

    /**
     * Sets date preference.
     *
     * @param datePreference the date preference
     */
    public void setDatePreference(String datePreference) {
        this.datePreference = datePreference;
    }

    /**
     * Gets seperation day.
     *
     * @return the seperation day
     */
    public int getSeperationDay() {
        return seperationDay;
    }

    /**
     * Sets seperation day.
     *
     * @param seperationDay the seperation day
     */
    public void setSeperationDay(int seperationDay) {
        this.seperationDay = seperationDay;
    }

    /**
     * Gets proportion.
     *
     * @return the proportion
     */
    public double getProportion() {
        return proportion;
    }

    /**
     * Is group constraint boolean.
     *
     * @return the boolean
     */
    public boolean isGroupConstraint() {
        return isGroupConstraint;
    }

    /**
     * Sets group constraint.
     *
     * @param groupConstraint the group constraint
     */
    public void setGroupConstraint(boolean groupConstraint) {
        isGroupConstraint = groupConstraint;
    }

    /**
     * Is date constraint boolean.
     *
     * @return the boolean
     */
    public boolean isDateConstraint() {
        return isDateConstraint;
    }

    /**
     * Sets date constraint.
     *
     * @param dateConstraint the date constraint
     */
    public void setDateConstraint(boolean dateConstraint) {
        isDateConstraint = dateConstraint;
    }

    /**
     * Is seperation constraint boolean.
     *
     * @return the boolean
     */
    public boolean isSeperationConstraint() {
        return isSeperationConstraint;
    }

    /**
     * Sets seperation constraint.
     *
     * @param seperationConstraint the seperation constraint
     */
    public void setSeperationConstraint(boolean seperationConstraint) {
        isSeperationConstraint = seperationConstraint;
    }

    /**
     * Is rest difference constraint boolean.
     *
     * @return the boolean
     */
    public boolean isRestDifferenceConstraint() {
        return isRestDifferenceConstraint;
    }

    /**
     * Sets rest difference constraint.
     *
     * @param restDifferenceConstraint the rest difference constraint
     */
    public void setRestDifferenceConstraint(boolean restDifferenceConstraint) {
        isRestDifferenceConstraint = restDifferenceConstraint;
    }

    /**
     * Is daily constraint boolean.
     *
     * @return the boolean
     */
    public boolean isDailyConstraint() {
        return isDailyConstraint;
    }

    /**
     * Sets daily constraint.
     *
     * @param dailyConstraint the daily constraint
     */
    public void setDailyConstraint(boolean dailyConstraint) {
        isDailyConstraint = dailyConstraint;
    }

    /**
     * Is preference constraint boolean.
     *
     * @return the boolean
     */
    public boolean isPreferenceConstraint() {
        return isPreferenceConstraint;
    }

    /**
     * Sets preference constraint.
     *
     * @param preferenceConstraint the preference constraint
     */
    public void setPreferenceConstraint(boolean preferenceConstraint) {
        isPreferenceConstraint = preferenceConstraint;
    }

    /**
     * Gets teams.
     *
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }


    /**
     * Clear time slot.
     */
    public void clearTimeSlot(){
        timeSlot.clear();
    }
}
