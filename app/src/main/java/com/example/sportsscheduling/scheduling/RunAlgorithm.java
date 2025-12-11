package com.example.sportsscheduling.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * The type Run algorithm.
 */
public class RunAlgorithm {
    private static String outputCost = "";
    private static List<String> schedulingOutput;

    /**
     * Run string.
     *
     * @return the string
     */
    public static String run() {
        StringBuilder output = new StringBuilder();

        long seed = 123456;
        Random random = new Random(seed);

        RandomInstanceGenerator generator = new RandomInstanceGenerator(seed);
        Initialisation scheduler;

        int scale = 1;
        while (scale < 4) {
            for (int iteration = 0; iteration < 5; iteration++) {
                RandomInstanceGenerator.GeneratedProblem gp = generator.createRandomProblem(scale);

                scheduler = Initialisation.getInstance(
                        gp.teams,
                        gp.venues,
                        gp.times,
                        gp.totalDays,
                        gp.maxDaily,
                        gp.maxPerTime,
                        gp.datePreference,
                        gp.separationDay
                );
                scheduler.clearTimeSlot();

                output.append("\n \n" + "scale: "+ scale + " iteration: " + iteration + "\n");
                output.append("team num: " + gp.teams.size() + ", "+scheduler.getTeams().size()).append("\n");
                output.append(gp.datePreference).append("\n");

                scheduler.setGroupConstraint(random.nextBoolean());
                scheduler.setDateConstraint(random.nextBoolean());
                scheduler.setSeperationConstraint(random.nextBoolean());
                scheduler.setRestDifferenceConstraint(random.nextBoolean());
                scheduler.setDailyConstraint(random.nextBoolean());
                scheduler.setPreferenceConstraint(random.nextBoolean());

                List<Match> schedule = scheduler.generateInitialSolution();
                sortSchedule(schedule);

                SimulatedAnnealing sa = new SimulatedAnnealing(schedule);
                List<Match> solution = sa.schedule();
                sortSchedule(solution);

                String costs = "";
                if(scheduler.isGroupConstraint())  costs = costs +  "group cost: " + sa.groupConstraint(solution) +"; ";
                if(scheduler.isDateConstraint())  costs = costs +  "date cost: " + sa.dateConstraint(solution) +"; ";
                if(scheduler.isSeperationConstraint())  costs = costs +  "seperation cost: " + sa.separationConstraint(solution) +"; ";
                if(scheduler.isRestDifferenceConstraint())  costs = costs +  "rest diff cost: " + sa.restDifferenceConstraint(solution) +"; ";
                if(scheduler.isDailyConstraint())  costs = costs +  "daily cost: " + sa.dailyMatchesConstraint(solution) +"; ";
                if(scheduler.isPreferenceConstraint())  costs = costs +  "preference cost: " + sa.preferenceConstraint(solution) +"; \n";
                output.append(costs);

                output.append("Finial Schedule:\n");
                for (Match match : solution) {
                    output.append(match.toString()).append("\n");
                }

                output.append("cost: ").append(sa.logCost()).append("\nbest cost: ").append(sa.logBestCost());

            }
            scale++;
        }

        return output.toString();
    }

    /**
     * Run pilot string.
     *
     * @return the string
     */
    public static String runPilot() {
        outputCost = "";
        StringBuilder output = new StringBuilder();
        List<Team> teams = Arrays.asList(
                new Team("A", "", "09am"),
                new Team("B", "", "01pm"),
                new Team("C", "venue1", ""),
                new Team("D", "", ""),
                new Team("E", "", ""),
                new Team("F", "venue2", "01pm"),
                new Team("G", "", ""),
                new Team("H", "venue2", ""),
                new Team("I", "", ""),
                new Team("J", "", "09am")
        );
        List<Venue> venues = Arrays.asList(
                new Venue("venue1"),
                new Venue("venue2")
        );
        List<String> times = Arrays.asList("09am", "01pm");
        int totalDays = 14;

        boolean isGroupConstraint = false;
        boolean isDateConstraint = false;
        boolean isSeperationConstraint = false;
        boolean isRestDifferenceConstraint = false;
        boolean isDailyConstraint = true;
        boolean isPreferenceConstraint = true;

        int maxDaily = 1;
        int maxPerTime = 3;
        String datePreference = "A,B,<=,5;F,J,>=,3;G,I,>=,6";
        int seperationDay = 3;

        Initialisation scheduler = Initialisation.getInstance(teams, venues, times, totalDays, maxDaily, maxPerTime, datePreference, seperationDay);
        scheduler.setGroupConstraint(isGroupConstraint);
        scheduler.setDateConstraint(isDateConstraint);
        scheduler.setSeperationConstraint(isSeperationConstraint);
        scheduler.setRestDifferenceConstraint(isRestDifferenceConstraint);
        scheduler.setDailyConstraint(isDailyConstraint);
        scheduler.setPreferenceConstraint(isPreferenceConstraint);

        List<Match> schedule = scheduler.generateInitialSolution();
        sortSchedule(schedule);

        output.append("Initial Generated Schedule:\n");
        for (Match match : schedule) {
            output.append(match.toString()).append("\n");
        }

        SimulatedAnnealing sa = new SimulatedAnnealing(schedule);
        List<Match> solution = sa.schedule();
        sortSchedule(solution);

        String costs = "";
        if(scheduler.isGroupConstraint())  costs = costs +  "group cost: " + sa.groupConstraint(solution) +"; ";
        if(scheduler.isDateConstraint())  costs = costs +  "date cost: " + sa.dateConstraint(solution) +"; ";
        if(scheduler.isSeperationConstraint())  costs = costs +  "seperation cost: " + sa.separationConstraint(solution) +"; ";
        if(scheduler.isRestDifferenceConstraint())  costs = costs +  "rest diff cost: " + sa.restDifferenceConstraint(solution) +"; ";
        if(scheduler.isDailyConstraint())  costs = costs +  "daily cost: " + sa.dailyMatchesConstraint(solution) +"; ";
        if(scheduler.isPreferenceConstraint())  costs = costs +  "preference cost: " + sa.preferenceConstraint(solution) +"; \n";
        output.append(costs);

        output.append("Finial Schedule:\n");
        for (Match match : solution) {
            output.append(match.toString()).append("\n");
        }

        output.append("cost: ").append(sa.logCost()).append("\nbest cost: ").append(sa.logBestCost());
        outputCost = "cost: " + sa.logCost() + "\n\nbest cost: " + sa.logBestCost() + "\n\nprobability: " + sa.logProbability() + "\n\ntemperature: " + sa.logTemperatureTrack();

        return output.toString();
    }

    /**
     * Run scheduler string.
     *
     * @param teams                      the teams
     * @param venues                     the venues
     * @param times                      the times
     * @param isGroupConstraint          the is group constraint
     * @param isDateConstraint           the is date constraint
     * @param isSeperationConstraint     the is seperation constraint
     * @param isRestDifferenceConstraint the is rest difference constraint
     * @param isDailyConstraint          the is daily constraint
     * @param isPreferenceConstraint     the is preference constraint
     * @param totalDays                  the total days
     * @param maxDaily                   the max daily
     * @param maxPerTime                 the max per time
     * @param datePreference             the date preference
     * @param seperationDay              the seperation day
     * @return the string
     */
    public static String runScheduler(
            List<Team> teams,
            List<Venue> venues,
            List<String> times,
            boolean isGroupConstraint,
            boolean isDateConstraint,
            boolean isSeperationConstraint,
            boolean isRestDifferenceConstraint,
            boolean isDailyConstraint,
            boolean isPreferenceConstraint,
            int totalDays,
            int maxDaily,
            int maxPerTime,
            String datePreference,
            int seperationDay
    ) {
        outputCost = "";
        schedulingOutput = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        Initialisation scheduler = Initialisation.getInstance(teams, venues, times, totalDays, maxDaily, maxPerTime, datePreference, seperationDay);
        scheduler.setGroupConstraint(isGroupConstraint);
        scheduler.setDateConstraint(isDateConstraint);
        scheduler.setSeperationConstraint(isSeperationConstraint);
        scheduler.setRestDifferenceConstraint(isRestDifferenceConstraint);
        scheduler.setDailyConstraint(isDailyConstraint);
        scheduler.setPreferenceConstraint(isPreferenceConstraint);

        List<Match> schedule = scheduler.generateInitialSolution();
        sortSchedule(schedule);

        output.append("Initial Generated Schedule:\n");
        for (Match match : schedule) {
            output.append(match.toString()).append("\n");
        }

        SimulatedAnnealing sa = new SimulatedAnnealing(schedule);
        List<Match> solution = sa.schedule();
        sortSchedule(solution);

        String costs = "";
        if(scheduler.isGroupConstraint())  costs = costs +  "group cost: " + sa.groupConstraint(solution) +"; ";
        if(scheduler.isDateConstraint())  costs = costs +  "date cost: " + sa.dateConstraint(solution) +"; ";
        if(scheduler.isSeperationConstraint())  costs = costs +  "seperation cost: " + sa.separationConstraint(solution) +"; ";
        if(scheduler.isRestDifferenceConstraint())  costs = costs +  "rest diff cost: " + sa.restDifferenceConstraint(solution) +"; ";
        if(scheduler.isDailyConstraint())  costs = costs +  "daily cost: " + sa.dailyMatchesConstraint(solution) +"; ";
        if(scheduler.isPreferenceConstraint())  costs = costs +  "preference cost: " + sa.preferenceConstraint(solution) +"; \n";
        output.append(costs);

        output.append("Finial Schedule:\n");
        for (Match match : solution) {
            output.append(match.toString()).append("\n");
            schedulingOutput.add(match.toString());
        }

        output.append("cost: ").append(sa.logCost()).append("\nbest cost: ").append(sa.logBestCost());
        outputCost = "cost: " + sa.logCost() + "\n\nbest cost: " + sa.logBestCost() + "\n\nprobability: " + sa.logProbability() + "\n\ntemperature: " + sa.logTemperatureTrack();

        return output.toString();

    }

    /**
     * Sort schedule list.
     *
     * @param schedule the schedule
     * @return the list
     */
    public static List<Match> sortSchedule(List<Match> schedule) {
        schedule.sort(Comparator.comparingInt(Match::getDay)
                .thenComparing(m -> m.getTime().equals("09am") ? 0 : 1)  // 09am ranks ahead of 01pm
                .thenComparing(m -> m.getV().getName())); // Sort by venue name
        return schedule;
    }

    /**
     * Gets scheduling output.
     *
     * @return the scheduling output
     */
    public static List<String> getSchedulingOutput() {
        return schedulingOutput;
    }

    /**
     * Scheduling output string string.
     *
     * @return the string
     */
    public static String schedulingOutputString() {
        String schedulingString = "";
        for (String s : schedulingOutput){
            schedulingString = schedulingString + s + "\n";
        }

        return schedulingString;
    }
}
