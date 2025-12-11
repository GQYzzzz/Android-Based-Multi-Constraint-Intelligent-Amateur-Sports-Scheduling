package com.example.sportsscheduling.scheduling;

import java.util.*;

/**
 * The type Random instance generator.
 */
public class RandomInstanceGenerator {
    private Random random;

    /**
     * Instantiates a new Random instance generator.
     *
     * @param seed the seed
     */
    public RandomInstanceGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Create random problem generated problem.
     *
     * @param problemScale the problem scale
     * @return the generated problem
     */
    public GeneratedProblem createRandomProblem(int problemScale) {
        // Randomly decide the number of teams, venues and total days according to the problem scale
        int numberOfTeams = 0;
        int numberOfVenues = 0;
        int totalDays = 0;
        if (problemScale == 1) {
            numberOfTeams = random.nextInt(10 - 8 + 1) + 8;  // 8-10 teams
            numberOfVenues = 2;  // 2 venues
            totalDays = 14;      // 14 days
        } else if (problemScale == 2) {
            numberOfTeams = random.nextInt(16 - 12 + 1) + 12;  // 12-16 teams
            numberOfVenues = random.nextInt(4 - 3 + 1) + 3;  // 3-4 venues
            totalDays = 25;      // 25 days
        } else if (problemScale == 3) {
            numberOfTeams = 20;  // 20 teams
            numberOfVenues = 5;  // 5 venues
            totalDays = 30;      // 30 days
        }

        // Randomly generated teams and their preference
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            String teamName = String.valueOf((char) ('A' + i));  // e.g. A, B, C...
            // Random preference
            String preferredVenue = "";
            String preferredTime = "";
            double pVenue = random.nextDouble();
            // 40% of venue preference
            if (pVenue < 0.4) {
                int chosenVenue = random.nextInt(numberOfVenues - 1 + 1) + 1; // Choose venue from 1 to numberOfVenues
                preferredVenue = "venue" + chosenVenue;
            }
            double pTime = random.nextDouble();
            // 30% of time preference
            if (pTime < 0.3) {
                preferredTime = "09am";    // 30% prefer the morning
            } else if (pTime < 0.6) {
                preferredTime = "01pm";    // 30% prefer the afternoon
            }

            teams.add(new Team(teamName, preferredVenue, preferredTime));
        }

        // Randomly generated venues
        List<Venue> venues = new ArrayList<>();
        for (int v = 1; v <= numberOfVenues; v++) {
            String name = "venue" + v;
            venues.add(new Venue(name));
        }

        // Fixed times, assuming only "09am" and "01pm"
        List<String> times = Arrays.asList("09am", "01pm");

        int maxDaily = 1;
        int maxPerTime = 3;
        int separationDay = 3;
        String[] operators = {"<=", ">="};
        String datePreference = "";

        // Randomly generated 3 date preference and store them in the string
        for (int i = 0; i < 3; i++) {
            int idx1 = random.nextInt(numberOfTeams);
            int idx2 = random.nextInt(numberOfTeams);
            while (idx2 == idx1) {
                idx2 = random.nextInt(numberOfTeams);
            }
            Team t1 = teams.get(idx1);
            Team t2 = teams.get(idx2);
            String op = operators[random.nextInt(operators.length)];
            int dayLimit = random.nextInt(totalDays) + 1; // [1, totalDays]
            String s = t1.getName() + "," + t2.getName() + "," + op + "," + dayLimit;
            datePreference += s;
            if (i < 2) datePreference += ";";
        }

        return new GeneratedProblem(teams, venues, times, totalDays, maxDaily, maxPerTime, datePreference, separationDay);
    }

    /**
     * The type Generated problem.
     */
    public static class GeneratedProblem {
        /**
         * The Teams.
         */
        public List<Team> teams;
        /**
         * The Venues.
         */
        public List<Venue> venues;
        /**
         * The Times.
         */
        public List<String> times;
        /**
         * The Total days.
         */
        public int totalDays;
        /**
         * The Max daily.
         */
        public int maxDaily;
        /**
         * The Max per time.
         */
        public int maxPerTime;
        /**
         * The Date preference.
         */
        public String datePreference;
        /**
         * The Separation day.
         */
        public int separationDay;

        /**
         * Instantiates a new Generated problem.
         *
         * @param teams          the teams
         * @param venues         the venues
         * @param times          the times
         * @param totalDays      the total days
         * @param maxDaily       the max daily
         * @param maxPerTime     the max per time
         * @param datePreference the date preference
         * @param separationDay  the separation day
         */
        public GeneratedProblem(List<Team> teams, List<Venue> venues, List<String> times,
                                int totalDays, int maxDaily, int maxPerTime,
                                String datePreference, int separationDay) {
            this.teams = teams;
            this.venues = venues;
            this.times = times;
            this.totalDays = totalDays;
            this.maxDaily = maxDaily;
            this.maxPerTime = maxPerTime;
            this.datePreference = datePreference;
            this.separationDay = separationDay;
        }
    }
}